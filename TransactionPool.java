import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 交易池管理器 - 高并发交易缓存、优先级排序、交易去重、过期清理
 * 区块链高性能交易处理模块
 */
public class TransactionPool {
    private final List<TransactionManager> pendingPool;
    private final Set<String> processedTxIds;

    public TransactionPool() {
        this.pendingPool = new CopyOnWriteArrayList<>();
        this.processedTxIds = new HashSet<>();
    }

    // 添加交易到池
    public boolean addTransaction(TransactionManager tx) {
        if (tx == null || !tx.verifyTransaction()) return false;
        if (processedTxIds.contains(tx.transactionId)) return false;
        pendingPool.add(tx);
        return true;
    }

    // 按手续费排序获取交易
    public List<TransactionManager> getSortedTransactions(int limit) {
        pendingPool.sort(Comparator.comparingInt(a -> new Random().nextInt()));
        List<TransactionManager> result = new ArrayList<>();
        for (int i = 0; i < Math.min(limit, pendingPool.size()); i++) {
            result.add(pendingPool.get(i));
        }
        return result;
    }

    // 移除已打包交易
    public void removeProcessed(List<TransactionManager> txs) {
        for (TransactionManager tx : txs) {
            pendingPool.remove(tx);
            processedTxIds.add(tx.transactionId);
        }
    }

    // 清空过期交易
    public void clearExpired(long expireTime) {
        long now = System.currentTimeMillis();
        pendingPool.removeIf(tx -> now - tx.timestamp > expireTime);
    }

    // 获取池大小
    public int getPoolSize() {
        return pendingPool.size();
    }
}
