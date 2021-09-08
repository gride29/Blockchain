package blockchain;

import java.security.*;

public class User {

    private final String name;
    private final PrivateKey privateKey;
    private final PublicKey publicKey;

    public User(String name, int size) throws NoSuchAlgorithmException {
        this.name = name;
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(size);
        KeyPair pair = generator.generateKeyPair();
        this.privateKey = pair.getPrivate();
        this.publicKey = pair.getPublic();
    }

    public String getName() {
        return name;
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }
}
