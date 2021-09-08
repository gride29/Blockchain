package blockchain;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class BlockManager {

    private final List<Block> blockList;
    private final List<Transaction> transactions;
    private final int averageTime;
    private int zeros;
    private BlockInfo blockInfo;
    private long startTime;
    private Block prevBlock;

    public BlockManager(int zeros, int averageTime) {
        this.zeros = zeros;
        this.averageTime = averageTime;
        this.blockList = Collections.synchronizedList(new ArrayList<>());
        this.transactions = Collections.synchronizedList(new ArrayList<>());
        this.startTime = System.nanoTime();
        this.blockInfo = createBlockInfo(null);
    }

    //  verify magic number and add a new block to blockchain if it is VALID
    //  return TRUE, if operation is success
    public synchronized boolean createBlock(String magic, Miner miner) {
        if (blockInfo != null) {
            String hash = Util.applySha256(blockInfo, magic);
            if (Util.validate(hash, zeros) && Util.verify(blockInfo.getListOfTransactions())) {

                prevBlock = new Block(blockInfo, hash, magic);
                blockList.add(prevBlock);

                int time = (int) ((System.nanoTime() - startTime) / 1_000_000_000L);
                printInfo(prevBlock, time, miner);
                updateZeros(time);
                startTime = System.nanoTime();

                blockInfo = null;
                return true;
            }
        }
        return false;
    }

    //  create template for next block
    //  return NEW TEMPLATE for block with transactions
    private synchronized BlockInfo createBlockInfo(Block block) {
        BigInteger id = block == null ? BigInteger.ONE : block.getId().add(BigInteger.ONE);
        BigInteger timestamp = BigInteger.valueOf(new Date().getTime());
        String prevHash = block == null ? "0" : block.getHash();
        List<Transaction> copy = new ArrayList<>(transactions);
        transactions.clear();
        return new BlockInfo(id, timestamp, prevHash, copy);
    }

    private synchronized void updateZeros(int time) {
        if (averageTime > time) {
            zeros++;
            System.out.println("N was increased to " + zeros);
        } else if (averageTime == time) {
            System.out.println("N stays the same");
        } else {
            // avoid number of zeros below 0
            this.zeros = zeros == 0 ? 0 : zeros - 1;
            System.out.println("N was decreased by " + zeros);
        }
    }

    public synchronized void add(Transaction transaction) {
        transactions.add(transaction);
        if (blockInfo == null) {
            blockInfo = createBlockInfo(prevBlock);
        }
    }

    public int size() {
        return blockList.size();
    }

    public int getZeros() {
        return zeros;
    }

    public synchronized BlockInfo getBlockInfo() {
        return blockInfo;
    }

    private synchronized void printInfo(Block block, int time, Miner miner) {
        System.out.println("\nBlock:");
        System.out.println("Created by: " + miner.getUser().getName());
        System.out.println(miner.getUser().getName() + " gets 100 VC");
        System.out.println(block);
        System.out.println("Block was generating for " + time + " seconds");
    }
}
