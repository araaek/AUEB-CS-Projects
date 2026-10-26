/**
 * Node represents a queue node
 * Each node contains a generic type T as data and a reference to the next node
 * and a reference to the previus node in the queue.
 */

public class Node<T> {
    protected T data;
    protected Node<T> next = null;
    protected Node<T> prev = null;

    /**
     * Constructor. Sets data and next and prev referencies
     */
    Node(T data) {
        this.data = data;
    }

    /**
     * Returns this node's data
     *
     * @return the reference to node's data
     */
    public T getData() {
        // return data stored in this node
        return data;
    }

    /**
     * Get reference to next node
     *
     * @return the next node
     */
    public Node<T> getNext() {
        // get next node
        return next;
    }

    /**
     * Set reference to next node
     *
     * @param next reference
     */
    public void setNext(Node<T> next) {
        this.next = next;
    }

    /**
     * Get reference to previus node
     *
     * @return the previus node
     */
    public Node<T> getPrevius() {
        // get previus node
        return prev;
    }

    /**
     * Set reference to previus node
     *
     * @param previus reference
     */
    public void setPrevius(Node<T> prev) {
        this.prev = prev;
    }

}