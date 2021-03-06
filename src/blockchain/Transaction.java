package blockchain;

import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;

public class Transaction {

    private final String data;
    private final PublicKey publicKey;
    private final byte[] signature;

    public Transaction(User from, User to, BigInteger cash) throws NoSuchAlgorithmException, SignatureException, InvalidKeySpecException, InvalidKeyException {
        this.data = from.getName() + " sent " + cash + " VC to " + to.getName();
        this.publicKey = from.getPublicKey();
        this.signature = TransactionManager.sign(data, from.getPrivateKey());
    }

    public String getData() {
        return data;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public byte[] getSignature() {
        return signature;
    }
}
