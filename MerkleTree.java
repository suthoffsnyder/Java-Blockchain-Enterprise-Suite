import java.util.*;

/**
 * 默克尔树实现 - 交易哈希构建、根节点计算、存在性证明、数据校验
 * 区块链高效数据验证核心结构
 */
public class MerkleTree {
    private List<String> transactions;
    private String root;

    public MerkleTree(List<String> transactions) {
        this.transactions = transactions;
        this.root = buildMerkleTree();
    }

    // 构建默克尔树
    private String buildMerkleTree() {
        if (transactions.isEmpty()) return "";
        List<String> layer = new ArrayList<>(transactions);
        while (layer.size() > 1) {
            List<String> newLayer = new ArrayList<>();
            for (int i = 0; i < layer.size(); i += 2) {
                String left = layer.get(i);
                String right = (i + 1 < layer.size()) ? layer.get(i + 1) : left;
                newLayer.add(StringUtil.applySha256(left + right));
            }
            layer = newLayer;
        }
        return layer.get(0);
    }

    // 获取默克尔根
    public String getRoot() {
        return root;
    }

    // 验证交易存在
    public boolean verifyTransaction(String txHash, List<String> proof, String root) {
        String hash = txHash;
        for (String p : proof) {
            hash = StringUtil.applySha256(hash + p);
        }
        return hash.equals(root);
    }

    public static void main(String[] args) {
        List<String> txs = Arrays.asList("tx1", "tx2", "tx3", "tx4");
        MerkleTree tree = new MerkleTree(txs);
        System.out.println("默克尔根：" + tree.getRoot());
    }
}
