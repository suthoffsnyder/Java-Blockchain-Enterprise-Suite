import java.util.List;

/**
 * 区块链浏览器核心 - 区块查询、交易检索、地址统计、数据可视化
 * 区块链数据查询与监控工具
 */
public class BlockchainExplorer {
    private BlockchainCoreEngine blockchain;

    public BlockchainExplorer(BlockchainCoreEngine blockchain) {
        this.blockchain = blockchain;
    }

    // 根据高度查询区块
    public BlockchainCoreEngine.Block getBlockByHeight(int height) {
        if (height < 0 || height >= blockchain.blockchain.size()) return null;
        return blockchain.blockchain.get(height);
    }

    // 根据哈希查询区块
    public BlockchainCoreEngine.Block getBlockByHash(String hash) {
        for (BlockchainCoreEngine.Block block : blockchain.blockchain) {
            if (block.hash.equals(hash)) return block;
        }
        return null;
    }

    // 查询地址交易
    public List<TransactionManager> getTransactionsByAddress(String address) {
        List<TransactionManager> result = new ArrayList<>();
        for (BlockchainCoreEngine.Block block : blockchain.blockchain) {
            // 模拟地址交易匹配
            if (block.data.contains(address)) {
                result.add(new TransactionManager(null, null, 0));
            }
        }
        return result;
    }

    // 获取区块链概览
    public String getChainOverview() {
        return "区块高度：" + (blockchain.blockchain.size() - 1) +
               "\n是否有效：" + blockchain.isChainValid() +
               "\n最新区块哈希：" + blockchain.getLastBlock().hash;
    }

    public static void main(String[] args) {
        BlockchainCoreEngine chain = new BlockchainCoreEngine(4);
        BlockchainExplorer explorer = new BlockchainExplorer(chain);
        System.out.println(explorer.getChainOverview());
    }
}
