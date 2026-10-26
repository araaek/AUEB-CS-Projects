public class PQ {
    private HeapNode[] heap;
    private int size;
    double minmeg = Double.MAX_VALUE;
    int posMinmeg;

    public PQ(int capacity) {
        heap = new HeapNode[capacity + 1];
        size = 0;
    }

    private class HeapNode {
        LargeDepositor depositor;
        double score;

        public HeapNode(LargeDepositor depositor, double score) {
            this.depositor = depositor;
            this.score = score;
        }
    }

    public void insert(LargeDepositor depositor, double score) {
        if (size >= heap.length - 1) {
            // overflow, then if score is bigger than the minimum of biggests, remove the
            // depositor with minimum score and insert the new depositor
            if (score > minmeg) {
                for (int i = 1; i <= size; i++)
                    if (minmeg == heap[i].score) // find the position of minmeg
                        posMinmeg = i;

                swap(posMinmeg, size); // set the last depositor in place of minmeg
                size--;                // and perform sink 
                sink(posMinmeg);
                heap[++size] = new HeapNode(depositor, score);
                swim(size);
                minmeg = Double.MAX_VALUE;
                for (int i = 1; i <= size; i++)
                    if (heap[i].score < minmeg)
                        minmeg = heap[i].score;
            }
            return;
        }

        if (score < minmeg)
            minmeg = score;
        heap[++size] = new HeapNode(depositor, score);
        swim(size);
    }

    private void swim(int k) {
        while (k > 1 && heap[k / 2].score < heap[k].score) {
            swap(k, k / 2);
            k = k / 2;
        }
    }

    private void sink(int k) {
        while (2 * k <= size) {
            int j = 2 * k;
            if (j < size && heap[j].score < heap[j + 1].score)
                j++;
            if (heap[k].score >= heap[j].score)
                break;
            swap(k, j);
            k = j;
        }
    }

    private void swap(int i, int j) {
        HeapNode temp = heap[i];
        heap[i] = heap[j];
        heap[j] = temp;
    }

    public LargeDepositor removeMax() {
        if (size == 0)
            return null;
        LargeDepositor max = heap[1].depositor;
        swap(1, size--);
        sink(1);
        heap[size + 1] = null;
        return max;
    }

    // Print the elements of the PQ
    public void printSuspects() {
        while (size > 0) {
            LargeDepositor depositor = removeMax();
            System.out.println(depositor);
        }
    }

}
