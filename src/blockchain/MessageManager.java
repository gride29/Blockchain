package blockchain;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;

public class MessageManager {

    private final Cipher cipher;

    public MessageManager() throws NoSuchPaddingException, NoSuchAlgorithmException {
        this.cipher = Cipher.getInstance("RSA");
    }

    public String encrypt(String message, PrivateKey key) throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        this.cipher.init(Cipher.ENCRYPT_MODE, key);
        //  doFinal() - Finishes a multiple-part encryption or decryption operation, depending on how this cipher was initialized.
        return Base64.encodeBase64String(cipher.doFinal(message.getBytes(StandardCharsets.UTF_8)));
    }

    public String decrypt(String message, PrivateKey key) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        this.cipher.init(Cipher.DECRYPT_MODE, key);
        return new String(cipher.doFinal(Base64.decodeBase64(message)), StandardCharsets.UTF_8);
    }

    public static PrivateKey getPrivateKey(String key) throws NoSuchAlgorithmException, InvalidKeySpecException {
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(
                Base64.encodeBase64(key.getBytes(StandardCharsets.UTF_8)));
        KeyFactory factory = KeyFactory.getInstance("RSA");
        return factory.generatePrivate(spec);
    }

    public static PublicKey getPublicKey(String key) throws NoSuchAlgorithmException, InvalidKeySpecException {
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(
                Base64.encodeBase64(key.getBytes(StandardCharsets.UTF_8)));
        KeyFactory factory = KeyFactory.getInstance("RSA");
        return factory.generatePublic(spec);
    }

    public static byte[] sign(String message, PrivateKey privateKey) throws NoSuchAlgorithmException, SignatureException, InvalidKeySpecException, InvalidKeyException {
        Signature rsa = Signature.getInstance("SHA1withRSA");
        rsa.initSign(privateKey);
        rsa.update(message.getBytes());
        return rsa.sign();
    }

    public static boolean verify(Message message) throws SignatureException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException {
        Signature rsa = Signature.getInstance("SHA1withRSA");
        rsa.initVerify(message.getUser().getPublicKey());
        rsa.update(message.getData().getBytes());
        return rsa.verify(message.getSignature());
    }
}
