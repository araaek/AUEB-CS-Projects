public class FactorialLimit {

    public static void main(String[] args) {
        int limit = 2147483647;

        int x = 0;
        long factorialResult = 1;

        while (factorialResult <= limit) {
                x++;
                factorialResult *= x;
            }

        System.out.println("Η μεγαλύτερη τιμή του x για την οποία x! <= 2147483647 είναι: " + (x - 1));
    }
}