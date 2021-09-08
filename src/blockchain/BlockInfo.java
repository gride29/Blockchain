package blockchain;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

public class BlockInfo implements Serializable {

    private final BigInteger id;
    private final BigInteger timestamp;
    private final String prevHash;
    private final List<Message> messages;

    public BlockInfo(BigInteger id, BigInteger timestamp, String prevHash, List<Message> messages) {
        this.id = id;
        this.timestamp = timestamp;
        this.prevHash = prevHash;
        this.messages = messages;
    }

    public BigInteger getId() {
        return id;
    }

    public BigInteger getTimestamp() {
        return timestamp;
    }

    public String getPrevHash() {
        return prevHash;
    }

    public String getMessages() {
        return messages.isEmpty() ? "no messages" : messages.stream().map(Message::toString).collect(Collectors.joining("\n"));
    }

    public List<Message> getListOfMessages() {
        return messages;
    }
}
