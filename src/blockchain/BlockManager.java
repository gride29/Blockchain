package blockchain;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Date;
import java.util.Random;

public class BlockManager {

    private final int zeros;
    private Block block;

    public BlockManager(int zeros, Block block) {
        this.zeros = zeros;
        this.block = block == null
                ? new Block(BigInteger.ZERO, null, null, "0", null) : block;
    }

    public Block createBlock() {
        long start = System.nanoTime();
        BigInteger id = block.getId().add(BigInteger.ONE);
        BigInteger timestamp = BigInteger.valueOf(new Date().getTime());
        String magic, hash;
        Random rnd = new Random();
        do {
            magic = String.valueOf(rnd.nextInt());
            hash = applySha256(block.getHash(), id, timestamp, magic);
        } while (!hash.startsWith("0".repeat(zeros)));
        block = new Block(id, timestamp, block.getHash(), hash, magic);
        long end = System.nanoTime() - start;
        System.out.println("\n" + block);
        System.out.println("Block was generating for " + end / 1_000_000_000 + " seconds");
        return block;
    }

    private String applySha256(String prevHash, BigInteger id, BigInteger timestamp, String magic) {
        String data = prevHash + id + timestamp + magic;
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
            throw new RuntimeException();
        }
    }
}
