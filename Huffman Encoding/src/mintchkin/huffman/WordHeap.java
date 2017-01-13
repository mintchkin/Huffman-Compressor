package mintchkin.huffman;

import java.util.ArrayList;

class WordHeap {
    ArrayList<FrequencyLeaf> heap;
    FrequencyTree ftree;

    public WordHeap(FrequencyTree ftree) {
        this.ftree = ftree;
        this.heap = ftree.toArrayList();
        for (int i = (heap.size() - 1) / 2; i >= 0; i--) {
            maxHeapify(i);
        }
    }

    private void maxHeapify(int i) {
        int left = 2 * i + 1;
        int right = 2 * i + 2;
        int largest = i;
        if (left < heap.size() && heap.get(left).compareTo(heap.get(largest)) > 0) {
            largest = left;
        }
        if (right < heap.size() && heap.get(right).compareTo(heap.get(largest)) > 0) {
            largest = right;
        }
        if (largest != i) {
            FrequencyLeaf temp = heap.get(i);
            heap.set(i, heap.get(largest));
            heap.set(largest, temp);
            maxHeapify(largest);
        }
    }

    public FrequencyLeaf remove() {
        FrequencyLeaf max = heap.get(0);
        FrequencyLeaf top = heap.remove(heap.size() - 1);
        if (heap.size() > 0) {
            heap.set(0, top);
            maxHeapify(0);
        }
        ftree.remove(max.word);
        return max;
    }

    public static void main(String[] args) {
        String test = "g g g g g g g g g g a a d d d d s s s s s s s j j j k b b b b b b b b b b b b";
        FrequencyTree ft = new FrequencyTree(test);
        WordHeap wh = new WordHeap(ft);
        while (!wh.heap.isEmpty()) {
            for (FrequencyLeaf t : wh.heap) {
                System.out.println("[" + t.freq + "]\t" + t.word);
            }
        }
    }
}