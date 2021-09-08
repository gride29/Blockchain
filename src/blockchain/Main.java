package blockchain;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {

    private static volatile BlockInfo info;
    private static volatile List<String> TEMPLATE_MSG = new ArrayList<>(List.of(
            "it's not fair",
            "You always will be first because it is your blockchain!",
            "You always will be first because it is your blockchain!",
            "You're welcome :)",
            "Hey Tom, nice chat",
            "Hey, I'm first!",
            "That's nice msg",
            "Is anybody out there?",
            "im waiting!",
            "Where is my blockchain?",
            "It's not your blockchain..."
    ));

    public static void main(String[] args) throws InterruptedException, NoSuchAlgorithmException, SignatureException, InvalidKeySpecException, InvalidKeyException {
        List<Block> blockchain = Collections.synchronizedList(new ArrayList<>());
        List<Message> messages = Collections.synchronizedList(new ArrayList<>());
        ExecutorService miners = Executors.newCachedThreadPool();
        BlockManager blockManager = new BlockManager(0, 5);
        Random rnd = new Random();
        final int keySize = 1024;
        User[] users = new User[] {
                new User("Garfield", keySize),
                new User("John", keySize),
                new User("Jordan", keySize)
        };
        info = blockManager.createBlockInfo(null, new ArrayList<>());

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
                            List<Message> copyOfMsg = new ArrayList<>(messages);
                            info = blockManager.createBlockInfo(block, copyOfMsg);
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
            messages.add(new Message(users[rnd.nextInt(users.length)], TEMPLATE_MSG.get(rnd.nextInt(TEMPLATE_MSG.size()))));
        }
    }
}
