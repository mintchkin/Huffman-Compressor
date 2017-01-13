package mintchkin.huffman;

import java.util.PriorityQueue;
import java.util.HashMap;
import java.util.ArrayList;

class HuffmanTree {
    FrequencyLeaf root;
    HashMap<String, String> codeMap;

    public HuffmanTree(FrequencyTree ftree) {
        codeMap = new HashMap<String, String>();
        buildTree(ftree.toArrayList());
        buildCodeMap(root, "");
    }

    private void buildTree(ArrayList<FrequencyLeaf> flist) {
        PriorityQueue<FrequencyLeaf> pqueue = new PriorityQueue<FrequencyLeaf>(flist);
        while (pqueue.size() > 1) {
            FrequencyLeaf leaf1 = pqueue.poll();
            FrequencyLeaf leaf2 = pqueue.poll();
            pqueue.offer(new FrequencyLeaf("", leaf1.freq + leaf2.freq, null, leaf1, leaf2));
        }
        root = pqueue.poll(); 
    }

    private void buildCodeMap(FrequencyLeaf leaf, String code) {
        if (leaf == null) {
            return;
        } else if (!leaf.word.equals("")) {
            codeMap.put(leaf.word, code);
        } else {
            buildCodeMap(leaf.lChild, code + "0");
            buildCodeMap(leaf.rChild, code + "1");
        }
    }

    public String getCode(String word) {
        return codeMap.get(word.toLowerCase());
    }

    public static void main(String[] args) {
        String test = ("I could not would not on a boat " +
                       "I will not will not with a goat " +
                       "I will not eat them in the rain " +
                       "I will not eat them on a train " +
                       "Not in the dark Not in a tree " +
                       "Not in a car You let me be " +
                       "I do not like them in a box " +
                       "I do not like them with a fox " +
                       "I will not eat them in a house " +
                       "I do not like them with a mouse " +
                       "I do not like them here or there " +
                       "I do not like them ANYWHERE");
        FrequencyTree ftree = new FrequencyTree(test);
        HuffmanTree htree = new HuffmanTree(ftree);
        for (FrequencyLeaf leaf : ftree.toArrayList()) {
            System.out.println("[" + leaf.freq + "]\t" + leaf.word + "\t[" + htree.getCode(leaf.word) + "]");
        }
    }
}