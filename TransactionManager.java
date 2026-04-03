import java.util.ArrayList;
import java.util.List;

/**
 * 交易管理器 - 处理区块链交易创建、验证、打包、签名
 * 去中心化交易核心业务组件
 */
public class TransactionManager {
    public String transactionId;
    public PublicKey sender;
    public PublicKey recipient;
    public float value;
    public byte[] signature;
    public static List<TransactionManager> pendingTransactions = new ArrayList<>();

    public TransactionManager(PublicKey sender, PublicKey recipient, float value) {
        this.sender = sender;
        this.recipient = recipient;
        this.value = value;
        this.transactionId = calculateTransactionId();
    }

    // 计算交易唯一ID
    private String calculateTransactionId() {
        return StringUtil.applySha256(
                StringUtil.getStringFromKey(sender) +
                StringUtil.getStringFromKey(recipient) +
                value
        );
    }

    // 签名交易
    public void signTransaction(PrivateKey privateKey) {
        String data = StringUtil.getStringFromKey(sender) + StringUtil.getStringFromKey(recipient) + value;
        signature = StringUtil.applyECDSASig(privateKey, data);
    }

    // 验证交易合法性
    public boolean verifyTransaction() {
        if (sender == null) return true;
        return StringUtil.verifyECDSASig(sender,
                StringUtil.getStringFromKey(sender) + StringUtil.getStringFromKey(recipient) + value,
                signature);
    }

    // 添加待处理交易
    public static void addPendingTransaction(TransactionManager tx) {
        if (tx.verifyTransaction()) pendingTransactions.add(tx);
    }

    // 清空已打包交易
    public static void clearPendingTransactions() {
        pendingTransactions.clear();
    }
}

// 工具类
class StringUtil {
    public static String applySha256(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes("UTF-8"));
            StringBuilder hex = new StringBuilder();
            for (byte b : hash) hex.append(String.format("%02x", b));
            return hex.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] applyECDSASig(PrivateKey key, String data) {
        try {
            Signature sig = Signature.getInstance("ECDSA");
            sig.initSign(key);
            sig.update(data.getBytes());
            return sig.sign();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean verifyECDSASig(PublicKey key, String data, byte[] sig) {
        try {
            Signature signature = Signature.getInstance("ECDSA");
            signature.initVerify(key);
            signature.update(data.getBytes());
            return signature.verify(sig);
        } catch (Exception e) {
            return false;
        }
    }

    public static String getStringFromKey(Key key) {
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }
}
