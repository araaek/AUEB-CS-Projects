import java.io.PrintStream;
import java.util.NoSuchElementException;

/**
 * Implements the methods for a Double-ended Queue that handles items of type T
 */

public class StringDoubleEndedQueueImpl<T> implements StringDoubleEndedQueue<T> {

    private Node<T> head = null; /* The first Node of the queue */
    private Node<T> tail = null; /* The last Node of the queue */
    private int size = 0; /* The number of elements (Nodes) in the queue */

    /**
     * Default constructor
     */
    public StringDoubleEndedQueueImpl() {

    }

    /**
     * Determine whether queue is empty
     */
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Inserts an item of type T at the head of the queue
     */
    @Override
    public void addFirst(T item) {
        Node<T> newNode = new Node<>(item); /* create a new Node */
        if (isEmpty()) {
            head = newNode;
            tail = newNode;
        } else {
            newNode.setNext(head);
            head.prev = newNode;
            head = newNode;
        }
        size++;
    }

    /**
     * Inserts an item of type T at the end of the queue
     */
    @Override
    public void addLast(T item) {
        Node<T> newNode = new Node<>(item);
        if (isEmpty()) {
            head = newNode;
            tail = newNode;
        } else {
            newNode.setPrevius(tail);
            tail.next = newNode;
            tail = newNode;
        }
        size++;
    }

    /**
     * remove and return the item at the head of the queue
     *
     * @return item of type T from the head of the queue
     * @throws NoSuchElementException if the queue is empty
     */
    @Override
    public T removeFirst() throws NoSuchElementException {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        T data = head.getData();
        head = head.getNext();
        if (head != null) {
            head.setPrevius(null);
        } else {
            tail = null;
        }
        size--;
        return data;
    }

    /**
     * remove and return the item at the end of the queue
     *
     * @return item of type T from the end of the queue
     * @throws NoSuchElementException if the queue is empty
     */
    @Override
    public T removeLast() throws NoSuchElementException {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        T data = tail.getData();
        tail = tail.getPrevius();
        if (tail != null) {
            tail.setNext(null);
        } else {
            head = null;
        }
        size--;
        return data;
    }

    /**
     * return without removing the item at the head of the queue
     *
     * @return item of type T from the head of the queue
     * @throws NoSuchElementException if the queue is empty
     */
    @Override
    public T getFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return head.getData();
    }

    /**
     * return without removing the item at the end of the queue
     *
     * @return item of type T from the end of the queue
     * @throws NoSuchElementException if the queue is empty
     */
    @Override
    public T getLast() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return tail.getData();
    }

    /**
     * print the items of the queue, starting from the head,
     * to the print stream given as an argument. For example, to
     * print the elements to the
     * standard output, pass System.out as a parameter. E.g.,
     * printQueue(System.out);
     */
    @Override
    public void printQueue(PrintStream stream) {
        Node<T> current = head;
        while (current != null) {
            stream.print(current.data + " ");
            current = current.next;
        }
        stream.println();
    }

    /**
     * return the size of the queue, 0 if empty
     *
     * @return number of elements in the queue
     */
    @Override
    public int size() {
        return size;
    }
}
