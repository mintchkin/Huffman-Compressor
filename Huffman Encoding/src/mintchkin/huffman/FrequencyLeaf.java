package mintchkin.huffman;

class FrequencyLeaf implements Comparable<FrequencyLeaf> {
    String word;
    int freq;
    FrequencyLeaf parent;
    FrequencyLeaf lChild;
    FrequencyLeaf rChild;

    public FrequencyLeaf(String word) {
        this(word, null);
    }

    public FrequencyLeaf(String word, FrequencyLeaf parent) {
        this(word, 1, parent, null, null);
    }

    public FrequencyLeaf(String word, int freq, FrequencyLeaf parent,
            FrequencyLeaf lChild, FrequencyLeaf rChild) {
        this.word = word.toLowerCase();
        this.freq = freq;
        this.parent = parent;
        this.lChild = lChild;
        this.rChild = rChild;
    }

    @Override
    public int compareTo(FrequencyLeaf other) {
        return this.freq - other.freq;
    }

    @Override
    public String toString() {
        String output = "[" + freq + "]\t" + word;
        if (parent != null) {
            return output + "\t[" + parent.word + "]";
        } else {
            return output + "\t[null]";
        }
    }
}