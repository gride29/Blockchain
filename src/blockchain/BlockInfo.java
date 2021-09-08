package blockchain;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

public class BlockInfo implements Serializable {

    private final BigInteger id;
    private final BigInteger timestamp;
    private final String prevHash;
    private List<Message> messages;
    private final List<Transaction> transactions;

    public BlockInfo(BigInteger id, BigInteger timestamp, String prevHash, List<Transaction> transactions) {
        this.id = id;
        this.timestamp = timestamp;
        this.prevHash = prevHash;
        this.transactions = transactions;
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
        return messages.isEmpty() ? "no messages" : "\n" + transactions.stream().map(Transaction::toString).collect(Collectors.joining("\n"));
    }

    public String getTransactions() {
        return transactions.isEmpty() ? "no transactions" : "\n" + transactions.stream().map(Transaction::getData).collect(Collectors.joining("\n"));
    }

    public List<Message> getListOfMessages() {
        return messages;
    }

    public List<Transaction> getListOfTransactions() { return transactions; }
}
