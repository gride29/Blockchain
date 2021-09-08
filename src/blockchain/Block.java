package blockchain;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;

public class Block implements Serializable {

    private static final long serialVersionUID = 1L;
    private final BlockInfo blockInfo;
    private final String hash;
    private final String magic;

    Block(BlockInfo info, String hash, String magic) {
        this.blockInfo = info;
        this.hash = hash;
        this.magic = magic;
    }

    public BigInteger getId() {
        return blockInfo.getId();
    }

    public BigInteger getTimeStamp() {
        return blockInfo.getTimestamp();
    }

    public String getPrevHash() {
        return blockInfo.getPrevHash();
    }

    public String getHash() {
        return hash;
    }

    public String getMagic() {
        return magic;
    }

    @Override
    public String toString() {
        return  "Id: " + getId() + "\n" +
                "Timestamp: " + getTimeStamp() + "\n" +
                "Magic number: " + getMagic() + "\n" +
                "Hash of the previous block: \n" +
                getPrevHash() + "\n" +
                "Hash of the block: \n" +
                getHash() + "\n" +
                "Block data: " + blockInfo.getMessages();
    }
}
