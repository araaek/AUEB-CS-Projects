import java.util.Scanner;

/**
 * Checks if sequence of nucleotides is Watson-Crick complement palindrome using
 * the Double-ended Queue that handles items of type String
 */
public class DNAPalindrome {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the DNA sequence: ");
        String dnaSequence = scanner.nextLine();

        if (isValidSequence(dnaSequence)) {
            boolean isPalindrome = isWCPalindrome(dnaSequence);
            if (isPalindrome) {
                System.out.println("The DNA sequence is Watson-Crick complemented palindrome");
            } else {
                System.out.println("The DNA sequence is NOT Watson-Crick complemented palindrome");
            }
        } else {
            System.out.println("Invalid DNA sequence.");
        }
        scanner.close();
    }

    /** Check if the DNA sequence is valid */
    private static boolean isValidSequence(String dnaSequence) {
        /** The empty string is valid */
        if (dnaSequence.length() == 0) {
            return true;
        }

        /** Check if the string contains only 'A', 'T', 'C', 'G' */
        for (char c : dnaSequence.toCharArray()) {
            if (c != 'A' && c != 'T' && c != 'C' && c != 'G') {
                return false;
            }
        }

        /** If the loop completes, the string is valid */
        return true;
    }

    /** Check if the sequence is Watson-Crick complemented palindrome */
    private static boolean isWCPalindrome(String dnaSequence) {
        StringDoubleEndedQueue<Character> queue = new StringDoubleEndedQueueImpl<>();

        /**
         * If the DNA sequence has odd characters then is not Watson-Crick complemented
         * palindrome
         */
        if (dnaSequence.length() % 2 == 1) {
            return false;
        }
        /** Copy the characters from the DNA sequence to the queue */
        for (char c : dnaSequence.toCharArray()) {
            queue.addLast(c);
        }

        while (!queue.isEmpty()) {
            char n1 = queue.removeFirst();
            char n2 = queue.removeLast();

            if (!areComplements(n1, n2)) {
                return false;
            }
        }

        return true;
    }

    /** Check if 2 nucleotids are complements */
    private static boolean areComplements(char nucl1, char nucl2) {
        if ((nucl1 == 'A' && nucl2 == 'T') || (nucl1 == 'T' && nucl2 == 'A') || (nucl1 == 'C' && nucl2 == 'G')
                || (nucl1 == 'G' && nucl2 == 'C')) {
            return true;
        } else {
            return false;
        }
    }
}
