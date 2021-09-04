package blockchain;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {

    private static volatile BlockInfo info;

    public static void main(String[] args) throws InterruptedException {
        List<Block> blockchain = Collections.synchronizedList(new ArrayList<>());
        ExecutorService miners = Executors.newCachedThreadPool();
        BlockManager blockManager = new BlockManager(0, 0);

        info = blockManager.createBlockInfo(null);
        for (int i = 0; i < 4; i++) {
            miners.submit(() -> {
                while (blockchain.size() < 5) {
                    final int size = blockchain.size();
                    long start = System.nanoTime();

                    String magic;
                    String hash;
                    Random rnd = new Random();

                    do {
                        magic = String.valueOf(rnd.nextInt());
                        hash = blockManager.applySha256(info, magic);
                    } while (!blockManager.validate(hash));

                    int end = (int) ((System.nanoTime() - start) / 1_000_000_000);

                    synchronized (Main.class) {
                        Block block = blockManager.createBlock(info, magic);
                        if (block != null && blockchain.size() == size) {
                            info = blockManager.createBlockInfo(block);
                            blockchain.add(block);
                            System.out.println("\nBlock:");
                            System.out.println("Created by miner # " + Thread.currentThread().getId());
                            System.out.println(block);
                            System.out.println("Block was generating for " + end + " seconds");
                            blockManager.updateZeros(end, size + 1);
                        }
                    }
                }
            });
        }
        miners.awaitTermination(10, TimeUnit.SECONDS);
        miners.shutdown();
    }
}
