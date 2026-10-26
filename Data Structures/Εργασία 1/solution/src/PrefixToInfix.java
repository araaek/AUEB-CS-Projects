import java.util.Scanner;

/**
 * Converts a Prefix expresion to an Infix expresion using the Double-ended
 * Queue that handles items of type String
 */
public class PrefixToInfix {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the prefix expression: ");
        String prefixExpression = scanner.nextLine();

        try {
            String infixExpression = convertPrefixToInfix(prefixExpression);
            System.out.println("Infix expression: " + infixExpression);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
        scanner.close();
    }

    private static String convertPrefixToInfix(String prefixExpression) {
        StringDoubleEndedQueue<String> queue = new StringDoubleEndedQueueImpl<>();

        /**
         * if the first character of expresion is a digit then is an invalid prefix
         * expression
         */
        if (Character.isDigit(prefixExpression.charAt(0))) {
            throw new IllegalArgumentException("The prefix expression must begin with an operator");
        }
        for (int i = prefixExpression.length() - 1; i >= 0; i--) {
            char c = prefixExpression.charAt(i);

            if (Character.isDigit(c)) {
                queue.addFirst(Character.toString(c));
            } else if (isOperator(c)) {
                /**
                 * if the queue does not contain at least 2 nodes, then the operartors are too
                 * many
                 */
                if (queue.size() >= 2) {
                    String operand1 = queue.removeFirst();
                    String operand2 = queue.removeFirst();
                    String infixExp = "(" + operand1 + c + operand2 + ")";
                    queue.addFirst(infixExp);
                } else {
                    throw new IllegalArgumentException("The expression contains too many operators");
                }
            } else {
                /**
                 * if any character of the expresion is not a digit or an operator then is an
                 * invalid character
                 */
                throw new IllegalArgumentException("The expresion contains invalid character");
            }
        }

        /**
         * if at the end there are more than 1 nodes in queue, then the operants are too
         * many
         */
        if (queue.size() == 1) {
            return queue.getFirst();
        } else {
            throw new IllegalArgumentException("The expression contains too many operants");
        }
    }

    /**
     * Check if a character is operator
     */
    private static boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/';
    }
}
