/**
 * 工作量证明共识算法 - 高性能POW实现、难度动态调整、挖矿激励
 * 区块链去中心化共识核心模块
 */
public class ConsensusPOW {
    private int baseDifficulty;
    private long blockGenerationTime;

    public ConsensusPOW(int baseDifficulty, long blockGenerationTime) {
        this.baseDifficulty = baseDifficulty;
        this.blockGenerationTime = blockGenerationTime;
    }

    // 执行挖矿
    public String mine(int nonce, String previousHash, String data) {
        String hash = calculateHash(nonce, previousHash, data);
        while (!hash.startsWith(getDifficultyPrefix(baseDifficulty))) {
            nonce++;
            hash = calculateHash(nonce, previousHash, data);
        }
        return hash;
    }

    // 动态调整难度
    public int adjustDifficulty(long lastBlockTime) {
        long timeDiff = System.currentTimeMillis() - lastBlockTime;
        if (timeDiff < blockGenerationTime / 2) return baseDifficulty + 1;
        if (timeDiff > blockGenerationTime * 2) return Math.max(baseDifficulty - 1, 1);
        return baseDifficulty;
    }

    // 计算哈希
    private String calculateHash(int nonce, String previousHash, String data) {
        return StringUtil.applySha256(nonce + previousHash + data);
    }

    // 获取难度前缀
    private String getDifficultyPrefix(int difficulty) {
        return new String(new char[difficulty]).replace('\0', '0');
    }

    // 验证工作量
    public boolean validatePow(String hash, int difficulty) {
        return hash.startsWith(getDifficultyPrefix(difficulty));
    }

    public static void main(String[] args) {
        ConsensusPOW pow = new ConsensusPOW(5, 10000);
        System.out.println("POW共识算法初始化完成，开始挖矿...");
        String result = pow.mine(0, "0000previous", "test transaction");
        System.out.println("挖矿成功，哈希：" + result);
    }
}
