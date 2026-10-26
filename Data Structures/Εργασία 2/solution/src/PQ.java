public class PQ {
    private City[] heap;
    private int[] indexMap;
    private int size;

    // Constructor
    public PQ(int capacity) {
        heap = new City[capacity + 1];

        // Keep an array to store in to store which IDs is in the queue
        // and in which position
        indexMap = new int[1000]; // Assuming city IDs range from 1 to 999
        for (int i = 0; i < indexMap.length; i++) {
            indexMap[i] = -1;
        }
        size = 0;
    }

    public PQ() {
        this(10); // Set an initial capacity (e.g., 10)
        size = 0;

    }

    // Check if the priority queue is empty
    public boolean isEmpty() {
        return size == 0;
    }

    // Get the size of the priority queue
    public int size() {
        return size;
    }

    // Insert an element into the priority queue
    public void insert(City x) {
        // Check available space
        if (size >= 0.75 * heap.length)
            resize();

        // Place x at the next available position
        heap[++size] = x;
        indexMap[x.getID()] = size;

        // Let the newly added item swim
        swim(size);
    }

    // Get the minimum element without removing it
    public City min() {
        if (isEmpty()) {
            return null;
        }
        return heap[1];
    }

    // Get the maximum element without removing it
    public City max() {
        if (isEmpty()) {
            return null;
        }
        int maxIndex = 1;
        for (int i = 2; i <= size; i++) {
            if (heap[i].compareTo(heap[maxIndex]) > 0) {
                maxIndex = i;
            }
        }
        return heap[maxIndex];
    }

    // Remove and return the element with the smallest value
    public City getmin() {
        if (isEmpty())
            return null;
        City min = heap[1];
        indexMap[min.getID()] = -1; // Mark the ID as not in the heap

        // Replace root with the one at the rightmost leaf
        heap[1] = heap[size];
        size--; // Dispose the rightmost leaf
        sink(1); // Sink the root element
        return min;
    }

    // Remove and return the element with the given ID
    public City remove(int id) {
        if (isEmpty() || indexMap[id] == -1) {
            return null;
        }

        int position = indexMap[id];
        City removedCity = heap[position];
        indexMap[id] = -1; // Mark the ID as not in the heap
        swap(position, size);
        size--;
        sink(position);

        return removedCity;
    }

    // Return the k node
    public City getNode(int k) {
        return heap[k];
    }

    // Ensure the heap property is maintained by moving the element up
    private void swim(int i) {
        // If i is root, return
        if (i == 1)
            return;

        // Find parent
        int parent = i / 2;

        // Compare parent with child i
        while (i != 1 && heap[i].compareTo(heap[parent]) < 0) {
            swap(i, parent);
            i = parent;
            parent = i / 2;
        }
    }

    // Ensure the heap property is maintained by moving the element down
    private void sink(int i) {
        // Determine left, right child
        int left = 2 * i;
        int right = 2 * i + 1;

        // if 2*i > size, node i is a leaf, return
        if (left > size)
            return;

        // While haven't reached the leafs
        while (left <= size) {
            // Determine the largest smaller child of node id
            int min = left;
            if (right <= size) {
                if (heap[right].compareTo(heap[left]) < 0)
                    min = right;
            }

            if (heap[i].compareTo(heap[min]) <= 0)
                return;
            else {
                swap(i, min);
                i = min;
                left = i * 2;
                right = left + 1;
            }
        }
    }

    // Swap two elements in the heap
    private void swap(int i, int j) {
        City temp = heap[i];
        heap[i] = heap[j];
        heap[j] = temp;

        // Update the indexMap accordingly
        indexMap[heap[i].getID()] = i;
        indexMap[heap[j].getID()] = j;
    }

    // Resize the heap array
    private void resize() {
        City[] newHeap = new City[heap.length * 2];
        for (int i = 0; i <= size; i++) {
            newHeap[i] = heap[i];
        }
        heap = newHeap;
    }
}
