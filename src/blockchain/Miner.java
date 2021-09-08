package blockchain;

import java.util.Random;

public class Miner {

    private final User user;
    private final BlockManager blockManager;
    private final Random rnd;

    public Miner(User user, BlockManager blockManager) {
        this.user = user;
        this.blockManager = blockManager;
        this.rnd = new Random();
    }

    public String mine() {
        final int size = blockManager.size();
        String magic, hash;
        do {
            magic = String.valueOf(rnd.nextInt());
            BlockInfo blockInfo = blockManager.getBlockInfo();
            if (size != blockManager.size() || blockInfo == null) {
                return null;
            }
            hash = Util.applySha256(blockInfo, magic);
        } while (!Util.validate(hash, blockManager.getZeros()));

        return magic;
    }

    public User getUser() {
        return user;
    }

    public BlockManager getBlockManager() {
        return blockManager;
    }

    public Random getRnd() {
        return rnd;
    }
}
