package blockchain;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {

    private static volatile BlockInfo info;
    private static volatile List<String> TEMPLATE_MSG = new ArrayList<>(List.of(
            "Sarah: it's not fair",
            "Sarah: You always will be first because it is your blockchain!",
            "Sarah: You always will be first because it is your blockchain!",
            "Tom: You're welcome :)",
            "Nick: Hey Tom, nice chat",
            "Tom: Hey, I'm first!",
            "Tom: That's nice msg",
            "Nick: Is anybody out there?",
            "Nick: im waiting!",
            "Nick: Where is my blockchain?",
            "Tom: It's not your blockchain..."
    ));

    public static void main(String[] args) throws InterruptedException {
        List<Block> blockchain = Collections.synchronizedList(new ArrayList<>());
        List<String> messages = Collections.synchronizedList(new ArrayList<>());
        ExecutorService miners = Executors.newCachedThreadPool();
        BlockManager blockManager = new BlockManager(0, 5);
        Random rnd = new Random();
        info = blockManager.createBlockInfo(null, "");

        for (int i = 0; i < 4; i++) {
            miners.submit(() -> {
                outer: while (blockchain.size() < 5) {
                    final int size = blockchain.size();
                    long start = System.nanoTime();
                    String magic;
                    String hash;

                    do {
                        magic = String.valueOf(rnd.nextInt());
                        hash = blockManager.applySha256(info, magic);
                        if (size != blockchain.size()) {
                            continue outer;
                        }
                    } while (!blockManager.validate(hash));

                    int end = (int) ((System.nanoTime() - start) / 1_000_000_000);

                    synchronized (Main.class) {
                        Block block = blockManager.createBlock(info, magic);
                        if (block != null && blockchain.size() == size) {
                            String message = String.join("\n", messages);
                            info = blockManager.createBlockInfo(block, message);
                            // After the creation of the new block, all new messages that were sent during the creation
                            // should be included in the new block and deleted from the list.
                            messages.clear();
                            blockchain.add(block);
                            System.out.println("\nBlock:");
                            System.out.println("Created by miner # " + Thread.currentThread().getId());
                            System.out.println(block);
                            System.out.println("Block was generating for " + end + " seconds");
                            blockManager.updateZeros(end, size + 10);
                        }
                    }
                }
            });
        }

        miners.awaitTermination(10, TimeUnit.SECONDS);
        miners.shutdown();

        while (!miners.isTerminated()) {
            long time = rnd.nextInt(10) * 10L;
            try {
                Thread.sleep(time);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            messages.add(TEMPLATE_MSG.get(rnd.nextInt(TEMPLATE_MSG.size())));
        }
    }
}
