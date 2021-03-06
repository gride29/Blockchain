package blockchain;

import java.io.Serializable;
import java.math.BigInteger;

public class Block implements Serializable {

    private static final long serialVersionUID = 1L;

    private final BlockInfo info;
    private final String hash;
    private final String magic;

    Block(BlockInfo info, String hash, String magic) {
        this.info = info;
        this.hash = hash;
        this.magic = magic;
    }

    public BigInteger getId() {
        return info.getId();
    }

    public String getHash() {
        return hash;
    }

    @Override
    public String toString() {
        return "Id: " + info.getId() + "\n" +
                "Timestamp: " + info.getTimestamp() + "\n" +
                "Magic number: " + magic + "\n" +
                "Hash of the previous block: \n" + info.getPrevHash() + "\n" +
                "Hash of the block: \n" + hash + "\n" +
                "Block data: " + info.getTransactions();
    }
}
