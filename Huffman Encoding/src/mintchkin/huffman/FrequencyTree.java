package mintchkin.huffman;

import java.util.ArrayList;

class FrequencyTree {
    FrequencyLeaf root;

    public FrequencyTree(String str) {
        String[] words = str.replaceAll("\\p{P}", "").split("\\s");
        root = new FrequencyLeaf(words[0]);
        for (int i = 1; i < words.length; i++) {
            add(words[i]);
        }
    }

    public void add(String word) {
        word = word.toLowerCase();
        if (root == null) {
            root = new FrequencyLeaf(word);
        } else {
            add(word, root);
        }
    }

    private void add(String word, FrequencyLeaf leaf) {
        if (leaf.word.equals(word)) {
            leaf.freq++;
        } else if (leaf.word.compareTo(word) > 0) {
            if (leaf.lChild == null) {
                leaf.lChild = new FrequencyLeaf(word, leaf);
            } else {
                add(word, leaf.lChild);
            }
        } else {
            if (leaf.rChild == null) {
                leaf.rChild = new FrequencyLeaf(word, leaf);
            } else {
                add(word, leaf.rChild);
            }
        }
    }

    public boolean remove(String word) {
        word = word.toLowerCase();
        if (root == null) {
            return false;
        } else {
            return remove(word, root);
        }
    }

    private boolean remove(String word, FrequencyLeaf leaf) {
        if (leaf.lChild != null && leaf.word.compareTo(word) > 0) {
            return remove(word, leaf.lChild);
        } else if (leaf.rChild != null && leaf.word.compareTo(word) < 0) {
            return remove(word, leaf.rChild);
        } else if (leaf.word.equals(word)) {
            if (leaf.lChild != null && leaf.rChild != null) {
                FrequencyLeaf succ = successor(leaf);
                leaf.word = succ.word;
                leaf.freq = succ.freq;
                remove(succ.word, succ);
            } else if (leaf.lChild != null) {
                replaceTree(leaf, leaf.lChild);
            } else if (leaf.rChild != null) {
                replaceTree(leaf, leaf.rChild);
            } else {
                replaceTree(leaf, null);
            }
            return true;
        } else {
            return false;
        }
    }

    private void replaceTree(FrequencyLeaf oldTree, FrequencyLeaf newTree) {
        if (oldTree.parent != null) {
            if (oldTree == oldTree.parent.lChild) {
                oldTree.parent.lChild = newTree;
            } else {
                oldTree.parent.rChild = newTree;
            }
        }
        if (newTree != null) {
            newTree.parent = oldTree.parent;
        }
        if (oldTree == root) {
            root = newTree;
        }
    }

    private FrequencyLeaf successor(FrequencyLeaf leaf) {
        FrequencyLeaf succ = leaf.rChild;
        while (succ.lChild != null) {
            succ = succ.lChild;
        }
        return succ;
    }

    public WordHeap toWordHeap() {
        return new WordHeap(this);
    }

    public HuffmanTree toHuffmanTree() {
        return new HuffmanTree(this);
    }

    public ArrayList<FrequencyLeaf> toArrayList() {
        return toArrayList(new ArrayList<FrequencyLeaf>(), root);
    }

    private ArrayList<FrequencyLeaf> toArrayList(ArrayList<FrequencyLeaf> list, FrequencyLeaf leaf) {
        if (leaf.lChild != null) {
            toArrayList(list, leaf.lChild);
        }
        list.add(leaf);
        if (leaf.rChild != null) {
            toArrayList(list, leaf.rChild);
        }
        return list;
    }

    public FrequencyLeaf find(String word) {
        for (FrequencyLeaf leaf : toArrayList()) {
            if (leaf.word.equals(word)) {
                return leaf;
            }
        }
        return null;
    }

    public void printInOrder() {
        for (FrequencyLeaf tree : toArrayList()) {
            System.out.println(tree);
        }
    }

    public static void main(String[] args) {
        String sentence = "We are not enemies but friends we must not be enemies";
        FrequencyTree ft = new FrequencyTree(sentence);
        ft.printInOrder();
        System.out.println();
        System.out.println(ft.remove("be"));
        System.out.println(ft.remove("BuT"));
        System.out.println(ft.remove("we"));
        System.out.println(ft.remove("banana"));
        System.out.println();
        ft.printInOrder();
        System.out.println();
        for (int i = 0; i < sentence.split("\\s").length; i++) {
            ft.remove(sentence.split("\\s")[i]);
        }
        if (ft.root == null) {
            System.out.println("Tree is Empty!");
        }
    }
}