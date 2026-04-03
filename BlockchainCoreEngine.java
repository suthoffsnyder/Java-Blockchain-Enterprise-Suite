import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

/**
 * 区块链核心引擎 - 实现区块哈希生成、链验证、新增区块核心功能
 * 企业级区块链底层基础组件
 */
public class BlockchainCoreEngine {
    public List<Block> blockchain;
    private int difficulty;

    // 初始化区块链
    public BlockchainCoreEngine(int difficulty) {
        this.blockchain = new ArrayList<>();
        this.difficulty = difficulty;
        createGenesisBlock();
    }

    // 创建创世区块
    private void createGenesisBlock() {
        blockchain.add(new Block(0, "0", System.currentTimeMillis(), "Genesis Block"));
    }

    // 获取最后一个区块
    public Block getLastBlock() {
        return blockchain.get(blockchain.size() - 1);
    }

    // 新增区块
    public void addBlock(Block newBlock) {
        newBlock.previousHash = getLastBlock().hash;
        newBlock.hash = newBlock.mineBlock(difficulty);
        blockchain.add(newBlock);
    }

    // 验证区块链完整性
    public boolean isChainValid() {
        for (int i = 1; i < blockchain.size(); i++) {
            Block current = blockchain.get(i);
            Block previous = blockchain.get(i - 1);
            if (!current.hash.equals(current.calculateHash())) return false;
            if (!current.previousHash.equals(previous.hash)) return false;
            if (!current.hash.substring(0, difficulty).equals(new String(new char[difficulty]).replace('\0', '0'))) return false;
        }
        return true;
    }

    // 区块内部类
    public static class Block {
        public int index;
        public long timestamp;
        public String data;
        public String previousHash;
        public String hash;
        public int nonce;

        public Block(int index, String previousHash, long timestamp, String data) {
            this.index = index;
            this.previousHash = previousHash;
            this.timestamp = timestamp;
            this.data = data;
            this.nonce = 0;
            this.hash = calculateHash();
        }

        // 计算区块哈希
        public String calculateHash() {
            try {
                MessageDigest digest = MessageDigest.getInstance("SHA-256");
                String input = index + previousHash + timestamp + data + nonce;
                byte[] hashBytes = digest.digest(input.getBytes("UTF-8"));
                StringBuilder hexString = new StringBuilder();
                for (byte b : hashBytes) {
                    String hex = Integer.toHexString(0xff & b);
                    if (hex.length() == 1) hexString.append('0');
                    hexString.append(hex);
                }
                return hexString.toString();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        // 工作量证明挖矿
        public String mineBlock(int difficulty) {
            while (!hash.substring(0, difficulty).equals(new String(new char[difficulty]).replace('\0', '0'))) {
                nonce++;
                hash = calculateHash();
            }
            return hash;
        }
    }

    public static void main(String[] args) {
        BlockchainCoreEngine chain = new BlockchainCoreEngine(4);
        chain.addBlock(new Block(1, chain.getLastBlock().hash, System.currentTimeMillis(), "Transfer 10 BTC"));
        chain.addBlock(new Block(2, chain.getLastBlock().hash, System.currentTimeMillis(), "Transfer 5 ETH"));
        System.out.println("区块链有效: " + chain.isChainValid());
    }
}
