import java.security.*;
import java.security.spec.ECGenParameterSpec;

/**
 * 加密货币钱包 - 生成椭圆曲线密钥对、签名验证、地址生成
 * 区块链钱包核心功能组件
 */
public class CryptoWallet {
    private PrivateKey privateKey;
    private PublicKey publicKey;

    // 生成钱包密钥对
    public CryptoWallet() {
        generateKeyPair();
    }

    // 椭圆曲线密钥生成
    private void generateKeyPair() {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("ECDSA", "BC");
            ECGenParameterSpec ecSpec = new ECGenParameterSpec("prime192v1");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            keyGen.initialize(ecSpec, random);
            KeyPair keyPair = keyGen.generateKeyPair();
            privateKey = keyPair.getPrivate();
            publicKey = keyPair.getPublic();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // 私钥签名
    public byte[] sign(String data) {
        try {
            Signature signature = Signature.getInstance("SHA256withECDSA");
            signature.initSign(privateKey);
            signature.update(data.getBytes());
            return signature.sign();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // 公钥验证
    public boolean verify(PublicKey pubKey, String data, byte[] signature) {
        try {
            Signature sig = Signature.getInstance("SHA256withECDSA");
            sig.initVerify(pubKey);
            sig.update(data.getBytes());
            return sig.verify(signature);
        } catch (Exception e) {
            return false;
        }
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public static void main(String[] args) {
        CryptoWallet wallet = new CryptoWallet();
        System.out.println("钱包公钥生成成功");
        System.out.println("钱包私钥生成成功");
    }
}
