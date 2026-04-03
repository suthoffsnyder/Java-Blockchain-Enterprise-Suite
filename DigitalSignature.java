import java.security.*;
import java.util.Base64;

/**
 * 数字签名工具 - SHA256withECDSA签名、验签、数据加密
 * 区块链安全与身份认证核心组件
 */
public class DigitalSignature {
    private static final String ALGORITHM = "SHA256withECDSA";

    // 生成密钥对
    public static KeyPair generateKeyPair() throws Exception {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("EC");
        generator.initialize(256, new SecureRandom());
        return generator.generateKeyPair();
    }

    // 签名数据
    public static String sign(String data, PrivateKey privateKey) throws Exception {
        Signature signature = Signature.getInstance(ALGORITHM);
        signature.initSign(privateKey);
        signature.update(data.getBytes());
        return Base64.getEncoder().encodeToString(signature.sign());
    }

    // 验证签名
    public static boolean verify(String data, String signatureStr, PublicKey publicKey) throws Exception {
        Signature signature = Signature.getInstance(ALGORITHM);
        signature.initVerify(publicKey);
        signature.update(data.getBytes());
        byte[] sigBytes = Base64.getDecoder().decode(signatureStr);
        return signature.verify(sigBytes);
    }

    public static void main(String[] args) throws Exception {
        KeyPair keyPair = generateKeyPair();
        String data = "区块链交易数据";
        String signature = sign(data, keyPair.getPrivate());
        boolean isValid = verify(data, signature, keyPair.getPublic());
        System.out.println("签名验证结果：" + isValid);
    }
}
