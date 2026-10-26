import java.io.PrintStream;
import java.util.NoSuchElementException;

/** * Defines the methods for a Double-ended Queue that handles String items */public interface StringDoubleEndedQueue {	/**	 * @return true if the queue is empty	 */	public boolean isEmpty();	/**	 * insert a String item at the front of the queue	 */	public void addFirst(String item);	/**	 * remove and return the item at the front of the queue
	 * @return String from the front of the queue
	 * @throws NoSuchElementException if the queue is empty	 */	public String removeFirst() throws NoSuchElementException;	/**	 * insert a String item at the end of the queue	 */	public void addLast(String item);	/**	 * remove and return the item at the end of the queue
	 * @return String from the end of the queue
	 * @throws NoSuchElementException if the queue is empty	 */	public String removeLast() throws NoSuchElementException;		/**	 * return without removing the item at the front of the queue
	 * @return String from the front of the queue
	 * @throws NoSuchElementException if the queue is empty	 */	public String getFirst();	/**	 * return without removing the item at the end of the queue
	 * @return String from the end of the queue
	 * @throws NoSuchElementException if the queue is empty	 */	public String getLast();			/**	 * print the String items of the queue, starting from the front,      	 * to the print stream given as argument. For example, to          * print the elements to the	 * standard output, pass System.out as parameter. E.g.,	 * printQueue(System.out);	 */	public void printQueue(PrintStream stream);	/**	 * return the size of the queue, 0 if empty
	 * @return number of elements in the queue	 */	public int size();}