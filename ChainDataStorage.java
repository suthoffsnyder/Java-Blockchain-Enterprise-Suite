import java.io.*;
import java.util.List;

/**
 * 区块链数据持久化 - 本地文件存储、数据序列化、快照备份、恢复
 * 区块链数据落盘与容灾模块
 */
public class ChainDataStorage {
    private final String filePath;

    public ChainDataStorage(String filePath) {
        this.filePath = filePath;
    }

    // 保存区块链到文件
    public void saveBlockchain(List<BlockchainCoreEngine.Block> chain) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(chain);
        }
    }

    // 从文件加载区块链
    @SuppressWarnings("unchecked")
    public List<BlockchainCoreEngine.Block> loadBlockchain() throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            return (List<BlockchainCoreEngine.Block>) ois.readObject();
        }
    }

    // 创建数据快照
    public void createSnapshot() throws IOException {
        File src = new File(filePath);
        File dest = new File(filePath + ".snapshot." + System.currentTimeMillis());
        try (InputStream in = new FileInputStream(src); OutputStream out = new FileOutputStream(dest)) {
            byte[] buffer = new byte[1024];
            int len;
            while ((len = in.read(buffer)) > 0) out.write(buffer, 0, len);
        }
    }

    // 检查数据文件是否存在
    public boolean isStorageExists() {
        return new File(filePath).exists();
    }
}
