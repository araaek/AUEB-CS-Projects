public class Lista {
    LargeDepositor item;
    Lista next;

    public Lista(LargeDepositor item) {
        this.item = item;
        this.next = null;
    }

    public void appendToLista(LargeDepositor newItem) {
        Lista current = this;
        while (current.next != null) {
            current = current.next;
        }
        current.next = new Lista(newItem);
    }

    public void printLista() {
        Lista current = this;
        while (current != null) {
            System.out.println(current.item);
            current = current.next;
        }
    }
}
