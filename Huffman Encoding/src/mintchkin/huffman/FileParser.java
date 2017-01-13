package mintchkin.huffman;

import java.util.Scanner;
import java.io.*;

class FileParser {
    public static final int DEFAULT_STOP_COUNT = 10;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String text;
        while (true) {
            System.out.println("Input file path:");
            System.out.print(">>> ");
            try {
                text = new Scanner(new File(sc.nextLine())).useDelimiter("\\Z").next();
                break;
            } catch (Exception e) {
                System.out.println("Invalid File");
            }
        }
        FrequencyTree ftree = new FrequencyTree(text);
        WordHeap heap = ftree.toWordHeap();
        HuffmanTree htree = ftree.toHuffmanTree();
        for (int i = 0; i < DEFAULT_STOP_COUNT; i++) {
            System.out.println(heap.remove());
        }

        while (true) {
            System.out.println("Input word:");
            System.out.print(">>> ");
            String word = sc.next();
            System.out.println("Code: " + htree.getCode(word));
        }
    }
}