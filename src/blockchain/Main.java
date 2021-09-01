package blockchain;

import java.util.Scanner;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) {
        /* Block[] blockchain = new Block[5];
        blockchain[0] = new Block("0");
        for (int i = 1; i < blockchain.length; i++) {
            blockchain[i] = new Block(blockchain[i - 1].getHash());
        }
        Stream.of(blockchain).forEach(block -> System.out.println(block + "\n")); */
        Block block = null;
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter how many zeros the hash must start with: ");
        BlockManager manager = new BlockManager(scanner.nextInt(), block);
        Block b1 = manager.createBlock();
        Block b2 = manager.createBlock();
        Block b3 = manager.createBlock();
        Block b4 = manager.createBlock();
        Block b5 = manager.createBlock();
    }
}
