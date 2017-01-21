package mintchkin.huffman;

import java.util.HashMap;

public abstract class Encoding {
    public class NoSuchCodeException extends Exception {};

    HashMap<Character, Integer> weights;

    public Encoding(HashMap<Character, Integer> weights) {
        this.weights = weights;
    }

    public abstract String getCode(char c);

    public abstract char getSymbol(String code) throws NoSuchCodeException;

    public abstract HashMap<Character, String> getCodeMap();

    public HashMap<Character, Integer> getWeightMap() {
        return weights;
    }
}