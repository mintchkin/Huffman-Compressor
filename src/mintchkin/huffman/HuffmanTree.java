package mintchkin.huffman;

import java.util.HashMap;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.PriorityQueue;
import java.io.File;
import java.util.Map.Entry;

public class HuffmanTree extends Encoding {
    private abstract class Node implements Comparable<Node> {
        private int weight;

        public int compareTo(Node n) {
            return this.weight - n.weight;
        }

        public Node getLeft() {
            return null;
        }

        public Node getRight() {
            return null;
        }

        public abstract boolean isLeaf();
    }

    private class LeafNode extends Node {
        private char symbol;

        public LeafNode(char symbol, int weight) {
            this.symbol = symbol;
            super.weight = weight;
        }

        @Override
        public String toString() {
            return "{" + symbol + ": " + super.weight + "}";
        }

        public boolean isLeaf() {
            return true;
        }
    }

    private class BranchNode extends Node {
        private Node left;
        private Node right;

        public BranchNode(int weight, Node left, Node right) {
            super.weight = weight;
            this.left = left;
            this.right = right;
        }

        @Override
        public String toString() {
            return "{" + super.weight + "}";
        }

        @Override
        public Node getLeft() {
            return left;
        }

        @Override
        public Node getRight() {
            return right;
        }

        public boolean isLeaf() {
            return false;
        }
    }

    private Node root;
    private HashMap<Character, String> codeMap;

    public HuffmanTree(HashMap<Character, Integer> weights) {
        super(weights);
        root = buildTree();
        codeMap = buildCodeMap();
    }

    public String getCode(char c) {
        return codeMap.get(c);
    }

    public char getSymbol(String code) throws NoSuchCodeException {
        Node current = root;
        for (char c : code.toCharArray()) {
            if (current.isLeaf()) {
                throw new NoSuchCodeException();
            } else if (c == '0') {
                current = current.getLeft();
            } else if (c == '1') {
                current = current.getRight();
            }
        }
        if (!current.isLeaf()) {
            throw new NoSuchCodeException();
        } else {
            return ((LeafNode) current).symbol;
        }
    }

    public HashMap<Character, String> getCodeMap() {
        return codeMap;
    }

    private HashMap<Character, String> buildCodeMap() {
        HashMap<Character, String> codeMap = new HashMap<Character, String>();
        buildCodeMap(root, "", codeMap);
        return codeMap;
    }

    private void buildCodeMap(Node node, String code, HashMap<Character, String> codeMap) {
        if (node.isLeaf()) {
            codeMap.put(((LeafNode) node).symbol, code);
            return;
        }
        buildCodeMap(node.getLeft(), code + "0", codeMap);
        buildCodeMap(node.getRight(), code + "1", codeMap);
    }

    private Node buildTree() {
        PriorityQueue<Node> pQueue = new PriorityQueue<Node>(super.weights.size());
        for (Character c : super.weights.keySet()) {
            pQueue.add(new LeafNode(c, super.weights.get(c)));
        }
        while (pQueue.size() > 1) {
            Node node1 = pQueue.poll();
            Node node2 = pQueue.poll();
            pQueue.add(new BranchNode(node1.weight + node2.weight, node1, node2));
        }
        return pQueue.poll();
    }

    public static HashMap<Character, Integer> getWeights(File file) throws IOException {
        HashMap<Character, Integer> weights = new HashMap<Character, Integer>();
        int size = 0;
        try (BufferedReader input = new BufferedReader(new FileReader(file))) {
            while (input.ready()) {
                char c = (char) input.read();
                size += 1;
                if (weights.containsKey(c)) {
                    weights.put(c, weights.get(c) + 1);
                } else {
                    weights.put(c, 1);
                }
            }
            // TWEAKED:
            for (Entry<Character, Integer> entry : weights.entrySet()) {
                entry.setValue((int) Math.round((Math.pow(2, Byte.SIZE) - 1) * entry.getValue() / size));
            }
            //
            return weights;
        }
    }

    public String toString() {
        String output = "";
        for (Character c : codeMap.keySet()) {
            String repr;
            if (Character.getType(c) == Character.CONTROL ||
                Character.isWhitespace(c)) {
                repr = Character.getName(c);
            } else {
                repr = String.valueOf(c);
            }
            output += "{ " + repr + ": " + codeMap.get(c) + " }\n";
        }
        return output;
    }

    public static void main(String[] args) {
        java.util.Scanner sc = new java.util.Scanner(System.in);
        System.out.println("Input file path:");
        System.out.print(">>> ");
        String path = sc.nextLine();

        File file = new File(path);
        try {
            HashMap<Character, Integer> weights = HuffmanTree.getWeights(file);
            HuffmanTree htree = new HuffmanTree(weights);
            System.out.println(htree);
            System.out.println("Get symbol (enter code):");
            System.out.print(">>> ");
            System.out.println("Symbol Found: " + htree.getSymbol(sc.next()));
            System.out.println("Get code (enter symbol):");
            System.out.print(">>> ");
            System.out.println("Code Found: " + htree.getCode(sc.next().charAt(0)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}