package mintchkin.huffman;

import java.io.IOException;

public class FileBitWriter {
    private byte[] byteArray;

    public FileBitWriter(String binaryIn) throws IOException {
        byteArray = toByteArray(binaryIn);
    }

    private byte[] toByteArray(String binaryIn) throws IOException {
        if (binaryIn.length() % Byte.SIZE != 0) {
            throw new IOException("Malformed Input");
        }
        byte[] output = new byte[binaryIn.length() / Byte.SIZE];
        for (int i = 0; i < output.length; i++) {
            String bytestring = binaryIn.substring(i * Byte.SIZE, i * Byte.SIZE + Byte.SIZE);
            output[i] = 0;
            for (char c : bytestring.toCharArray()) {
                output[i] = (byte) ((output[i] << 1) | (c == '1' ? 1 : 0));
            }
        }
        return output;
    }

    public static void main(String[] args) {
        try {
            String banana = "11111100 01010101 01001101 11111011 00111110";
            FileBitWriter fbw = new FileBitWriter(banana);
            for (byte b : fbw.byteArray) {
                for (int i = 1; i <= Byte.SIZE; i++) {
                    System.out.print((b >>> (Byte.SIZE - i)) & 1);
                }
                System.out.print(" ");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}