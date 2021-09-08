package blockchain;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;
import java.util.stream.Collectors;

public class Util {

    public static String applySha256(BlockInfo info, String magic) {
        return applySha256(info.getPrevHash() +
                magic + info.getId() +
                info.getTimestamp() +
                info.getListOfTransactions().stream().map(Transaction::getData).collect(Collectors.joining())
        );
    }

    public static String applySha256(String data) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(data.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexes = new StringBuilder();
            for (byte elem : hash) {
                String hex = Integer.toHexString(0xff & elem);
                if (hex.length() == 1)  hexes.append('0');
                hexes.append(hex);
            }
            return hexes.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //  method to validate hash
    public static boolean validate(String hash, int zeros) {
        return hash.startsWith("0".repeat(zeros));
    }

    //  method to verify transaction
    public static boolean verify(List<Transaction> transactions) {
        for (var transaction : transactions) {
            try {
                if (!TransactionManager.verify(transaction)) {
                    return false;
                }
            } catch (SignatureException | NoSuchAlgorithmException | InvalidKeySpecException | InvalidKeyException e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }
}
