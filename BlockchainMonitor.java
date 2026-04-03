import java.util.Timer;
import java.util.TimerTask;

/**
 * 区块链监控系统 - 节点状态、算力监控、异常告警、数据统计
 * 企业级区块链运维监控组件
 */
public class BlockchainMonitor {
    private BlockchainCoreEngine blockchain;
    private Timer timer;

    public BlockchainMonitor(BlockchainCoreEngine blockchain) {
        this.blockchain = blockchain;
        this.timer = new Timer();
    }

    // 启动实时监控
    public void startMonitor(long interval) {
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                monitorChainStatus();
                checkAnomalies();
            }
        }, 0, interval);
    }

    // 监控区块链状态
    private void monitorChainStatus() {
        int height = blockchain.blockchain.size() - 1;
        boolean valid = blockchain.isChainValid();
        System.out.printf("[监控] 区块高度：%d | 链状态：%s%n", height, valid ? "正常" : "异常");
    }

    // 异常检测
    private void checkAnomalies() {
        if (!blockchain.isChainValid()) {
            System.out.println("[告警] 区块链数据被篡改！");
        }
    }

    // 停止监控
    public void stopMonitor() {
        timer.cancel();
    }

    public static void main(String[] args) {
        BlockchainCoreEngine chain = new BlockchainCoreEngine(4);
        BlockchainMonitor monitor = new BlockchainMonitor(chain);
        monitor.startMonitor(3000);
    }
}
