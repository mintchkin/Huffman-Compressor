package mintchkin.huffman;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Map;

public class Encoder {
    private File file;
    private Encoding encoding;
    private int filesize;
    private String encode;
    private String header;
    private String footer;

    public Encoder(File file, Encoding encoding) throws IOException {
        this.file = file;
        this.encoding = encoding;
        this.encode = buildEncode();
        this.header = buildHeader();
        this.footer = buildFooter();
        System.out.println(encode.length() + header.length() + footer.length());
    }

    private String buildEncode() throws IOException {
        try (BufferedReader input = new BufferedReader(new FileReader(file))) {
            this.filesize = 0;
            StringBuilder codeDump = new StringBuilder();
            while (input.ready()) {
                char c = (char) input.read();
                codeDump.append(encoding.getCode(c));
                filesize += 1;
            }
            return codeDump.toString();
        }
    }

    /**
    * Builds a header in the format:
    *   (32-bit) Int representing length of serialized HashMap, non-inclusive
    *   Serialized HashMap:
    *       (16-bit) Char representing symbol
    *       (8-bit)  Int representing weight
    *   (32-bit) Int representing length of encode, non-inclusive
    */
    private String buildHeader() {
        StringBuilder header = new StringBuilder();
        // TWEAKED:
        for (Map.Entry<Character, Integer> entry : encoding.getWeightMap().entrySet()) {
            String symbol = Integer.toBinaryString(entry.getKey());
            symbol = String.format("%" + Character.SIZE + "s", symbol).replace(' ', '0');
            header.append(symbol);
            String weight = Integer.toBinaryString(entry.getValue());
            weight = String.format("%" + Byte.SIZE + "s", weight).replace(' ', '0');
            header.append(weight);
        }
        String headersize = Integer.toBinaryString(header.length());
        headersize = String.format("%" + Integer.SIZE + "s", headersize).replace(' ', '0');
        header.insert(0, headersize);
        String filesize = Integer.toBinaryString(encode.length());
        filesize = String.format("%" + Integer.SIZE + "s", filesize).replace(' ', '0');
        header.append(filesize);
        return header.toString();
    }

    private String buildFooter() {
        StringBuilder output = new StringBuilder();
        for (int i = 0; (encode.length() + header.length() + i) % Byte.SIZE != 0; i++) {
            output.append("0");
        }
        return output.toString();
    }

    public void dump() {
        System.out.println(encode);
    }

    private byte[] toByteArray(String binary) {
        byte[] output = new byte[binary.length() / Byte.SIZE];
        for (int i = 0; i < output.length; i++) {
            String bytestring = binary.substring(i * Byte.SIZE, i * Byte.SIZE + Byte.SIZE);
            output[i] = 0;
            for (char c : bytestring.toCharArray()) {
                output[i] = (byte) ((output[i] << 1) | (c == '1' ? 1 : 0));
            }
        }
        return output;
    }

    public void write(File fout) throws IOException {
        try (FileOutputStream writer = new FileOutputStream(fout)) {
            writer.write(toByteArray(header + encode + footer));
        }
    }

    public String toString() {
        StringBuilder output = new StringBuilder();
        int origFilesize = filesize * Byte.SIZE;
        int encFilesize = header.length() + encode.length() + footer.length();
        double percChange = (double) encFilesize / origFilesize * 100;
        output.append("Original filesize: " + origFilesize);
        output.append("\nEncoded filesize: " + encFilesize + " (" + String.format("%.2f", percChange) + "%)");
        output.append("\n\nHeader: " + header);
        output.append("\n\nEncode: ");
        for (int i = 0; i < encode.length(); i++) {
            if (i > 300) {
                output.append(" [...]");
                break;
            }
            output.append(encode.charAt(i));
        }
        output.append("\n\nFooter: " + footer);
        return output.toString();
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
            HuffmanTree htree = new HuffmanTree(HuffmanTree.getWeights(infile));
            Encoder en = new Encoder(infile, htree);
            System.out.println(en);
            en.write(outfile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}