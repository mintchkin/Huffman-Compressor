package mintchkin.huffman;

import java.io.FileInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

public class Decoder {
    private File file;
    private Encoding encoding;
    private String binaryDump;
    private String decodeDump;

    public Decoder(File file) throws IOException {
        this.file = file;
        this.binaryDump = read();
        System.out.println(binaryDump.length());
        this.encoding = getEncoding();
        this.decodeDump = decode();
    }

    private String read() throws IOException {
        byte[] bytearray = new byte[(int) file.length()];
        try (FileInputStream input = new FileInputStream(file)) {
            input.read(bytearray);
        }

        StringBuilder output = new StringBuilder();
        for (byte b : bytearray) {
            for (int i = Byte.SIZE - 1; i >= 0; i--) {
                output.append((b >>> i) & 1);
            }
        }
        return output.toString();
    }

/**
* Header in the format:
*   (32-bit) Int representing length of serialized HashMap, non-inclusive
*   Serialized HashMap:
*       (16-bit) Char representing symbol
*       (8-bit)  Int representing weight
*   (32-bit) Int representing length of encode, non-inclusive
*/
    private Encoding getEncoding() {
        HashMap<Character, Integer> weights = new HashMap<Character, Integer>();
        int size = 0;
        for (int i = 0; i < Integer.SIZE; i++) {
            size = (size << 1) | (binaryDump.charAt(i) == '1' ? 1 : 0);
        }
        int i = Integer.SIZE;
        while (i < (Integer.SIZE + size)) {
            char symbol = (char) Integer.parseInt(binaryDump.substring(i, i += Character.SIZE), 2);
            int weight = Integer.parseInt(binaryDump.substring(i, i += Byte.SIZE), 2);
            weights.put(symbol, weight);
        }
        return new HuffmanTree(weights);
    }

    private String decode() {
        StringBuilder code = new StringBuilder();
        StringBuilder decode = new StringBuilder();
        int start = Integer.parseInt(binaryDump.substring(0, Integer.SIZE), 2) + Integer.SIZE;
        int end = Integer.parseInt(binaryDump.substring(start, start + Integer.SIZE), 2) + start;
        start += Integer.SIZE;
        end += Integer.SIZE;
        // System.out.println("Start: " + start + "\nEnd: " + end);
        String encode = binaryDump.substring(start, end);
        for (char c : encode.toCharArray()) {
            code.append(c);
            try {
                char symbol = encoding.getSymbol(code.toString());
                decode.append(symbol);
                code.delete(0, code.length());
            } catch (Encoding.NoSuchCodeException e) {
                continue;
            }
        }
        return decode.toString();
    }

    public void dump() {
        System.out.println(decodeDump);
    }

    public void write(File file) throws IOException {
        try (FileWriter fout = new FileWriter(file)) {
            fout.write(decodeDump);
        }
    }

    @Override
    public String toString() {
        if (decodeDump.length() < 500) {
            return decodeDump;
        } else {
            return decodeDump.substring(0, 500) + " [...]";
        }
    }

    public static void main(String[] args) {
        java.util.Scanner sc = new java.util.Scanner(System.in);
        System.out.println("Input file:");
        System.out.print(">>> ");
        String inpath = sc.nextLine();
        System.out.println("Output file:");
        System.out.print(">>> ");
        String outpath = sc.nextLine();

        try {
            File infile = new File(inpath);
            File outfile = new File(outpath);
            Decoder dec = new Decoder(infile);
            System.out.println(dec);
            dec.write(outfile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
