import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class RandomizedBST implements TaxEvasionInterface {

    private class TreeNode {
        LargeDepositor item;
        TreeNode left; // pointer to left subtree
        TreeNode right; // pointer to right subtree
        int N; // number of nodes in the subtree rooted at this TreeNode

        // Constructor for TreeNode
        public TreeNode(LargeDepositor item) {
            this.item = item;
            this.left = null;
            this.right = null;
            this.N = 1; // initially there is only one node in the subtree
        }
    }

    private TreeNode root; // root of the BST

    @Override
    public void insert(LargeDepositor item) {
        root = insertR(root, item);
    }

    @Override
    public void load(String filename) {
        try {
            File file = new File(filename);
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(" ");

                // Create a LargeDepositor object
                LargeDepositor depositor = new LargeDepositor();
                depositor.setAFM(Integer.parseInt(parts[0]));
                depositor.setFirstName(parts[1]);
                depositor.setLastName(parts[2]);
                depositor.setSavings(Double.parseDouble(parts[3]));
                depositor.setTaxedIncome(Double.parseDouble(parts[4]));

                // Insert into the tree
                insert(depositor);
            }

            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + filename);
        }
    }

    @Override
    public void updateSavings(int AFM, double savings) {
        TreeNode foundNode = searchNodeForUpdate(root, AFM, savings);
        if (foundNode == null) {
            System.out.println("No Depositor found with AFM: " + AFM);
        } else {
            System.out.println("Savings updated for Depositor with AFM: " + AFM);
        }
    }

    @Override
    public LargeDepositor searchByAFM(int AFM) {
        TreeNode result = searchNode(root, AFM);
        if (result != null) {
            System.out.println("Found: " + result.item);
            return result.item;
        } else {
            System.out.println("No Depositor found with AFM: " + AFM);
            return null;
        }
    }

    @Override
    public Lista searchByLastName(String last_name) {
        return searchByLastNameRecursive(root, last_name, null);
    }

    @Override
    public void remove(int AFM) {
        if (searchByAFM(AFM) != null) {
            root = removeR(root, AFM);
            System.out.println("Large Depositor removed.");
        }
    }

    @Override
    public double getMeanSavings() {
        if (root == null)
            return 0;
        double totalSavings = sumSavings(root);
        return totalSavings / root.N;
    }

    @Override
    public void printΤopLargeDepositors(int k) {
        PQ maxHeap = new PQ(k);
        fillPQ(root, maxHeap);
        maxHeap.printSuspects();
    }

    @Override
    public void printByAFM() {
        printInOrder(root);
    }

    public static void main(String[] args) {
        RandomizedBST bst = new RandomizedBST();
        Scanner scanner = new Scanner(System.in);
        String filename = "input.txt"; // Default file name

        // Check for command line argument
        if (args.length > 0) {
            filename = args[0];
        }

        // Load data from the file
        bst.load(filename);

        while (true) {
            System.out.println("\n____Menu____");
            System.out.println("1. Insert a Depositor");
            System.out.println("2. Remove a Depositor");
            System.out.println("3. Update Savings of a Depositor");
            System.out.println("4. Search a Depositor By AFM");
            System.out.println("5. Search Depositors By Last Name");
            System.out.println("6. Get Mean Savings of all Depositors");
            System.out.println("7. Print Top Depositors");
            System.out.println("8. Print all Depositors By AFM");
            System.out.println("0. Exit");
            System.out.print("Option: ");

            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    // Insert Large Depositor
                    System.out.println("Enter AFM, First Name, Last Name, Savings, Taxed Income:");
                    int afm = scanner.nextInt();
                    String firstName = scanner.next();
                    String lastName = scanner.next();
                    double savings = scanner.nextDouble();
                    double taxedIncome = scanner.nextDouble();

                    LargeDepositor depositor = new LargeDepositor();
                    depositor.setAFM(afm);
                    depositor.setFirstName(firstName);
                    depositor.setLastName(lastName);
                    depositor.setSavings(savings);
                    depositor.setTaxedIncome(taxedIncome);
                    bst.insert(depositor);
                    break;
                case 2:
                    // Remove Large Depositor
                    System.out.print("Enter AFM of Depositor to remove: ");
                    int removeAfm = scanner.nextInt();
                    bst.remove(removeAfm);
                    break;
                case 3:
                    // Update Savings
                    System.out.print("Enter AFM: ");
                    int updateAfm = scanner.nextInt();
                    System.out.print("Enter new Savings: ");
                    double newSavings = scanner.nextDouble();
                    bst.updateSavings(updateAfm, newSavings);
                    break;
                case 4:
                    // Search By AFM
                    System.out.print("Enter AFM to search: ");
                    int searchAFM = scanner.nextInt();
                    bst.searchByAFM(searchAFM);
                    break;
                case 5:
                    // Search By Last Name
                    System.out.print("Enter Last Name to search: ");
                    String lastNameSearch = scanner.next();
                    Lista depositors = bst.searchByLastName(lastNameSearch);
                    depositors.printLista();
                    break;
                case 6:
                    // Get Mean Savings
                    double meanSavings = bst.getMeanSavings();
                    System.out.println("Mean savings: " + meanSavings);
                    break;
                case 7:
                    // Print Top Large Depositors
                    System.out.print("Enter number of top large depositors to print: ");
                    int k = scanner.nextInt();
                    bst.printΤopLargeDepositors(k);
                    break;
                case 8:
                    // Print By AFM
                    bst.printByAFM();
                    break;
                case 0:
                    System.out.println("Exiting program.");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid selection, try again.");
            }
        }
    }

    // Additional methods
    private TreeNode insertR(TreeNode h, LargeDepositor item) {
        if (h == null)
            return new TreeNode(item);
        if (item.key() == h.item.key()) {
            System.out.println("Error: Large Depositor with the same AFM already exists.");
            return h;
        }
        if (Math.random() * (h.N + 1) < 1.0) {
            return insertAsRoot(item, h);
        }
        if (item.key() < h.item.key()) {
            h.left = insertR(h.left, item);
        } else {
            h.right = insertR(h.right, item);
        }
        h.N++;
        return h;
    }

    private TreeNode insertAsRoot(LargeDepositor item, TreeNode h) {
        if (h == null)
            return new TreeNode(item);
        if (item.key() < h.item.key()) {
            h.left = insertAsRoot(item, h.left);
            h = rotR(h);
        } else {
            h.right = insertAsRoot(item, h.right);
            h = rotL(h);
        }
        return h;
    }

    private TreeNode rotR(TreeNode h) {
        if (h == null || h.left == null)
            return h;
        TreeNode x = h.left;
        h.left = x.right;
        x.right = h;
        x.N = h.N;
        h.N = 1 + size(h.left) + size(h.right);
        return x;
    }

    private TreeNode rotL(TreeNode h) {
        if (h == null || h.right == null)
            return h;
        TreeNode x = h.right;
        h.right = x.left;
        x.left = h;
        x.N = h.N;
        h.N = 1 + size(h.left) + size(h.right);
        return x;
    }

    private int size(TreeNode node) {
        if (node == null)
            return 0;
        return node.N;
    }

    private void printInOrder(TreeNode node) {
        if (node == null)
            return;
        printInOrder(node.left);
        System.out.println(node.item);
        printInOrder(node.right);
    }

    private TreeNode searchNode(TreeNode node, int AFM) {
        if (node == null)
            return null;
        if (AFM == node.item.key()) {
            return node;
        } else if (AFM < node.item.key()) {
            return searchNode(node.left, AFM);
        } else {
            return searchNode(node.right, AFM);
        }
    }

    private TreeNode searchNodeForUpdate(TreeNode node, int AFM, double savings) {
        if (node == null)
            return null;
        if (AFM == node.item.key()) {
            node.item.setSavings(savings);
            return node;
        } else if (AFM < node.item.key()) {
            return searchNodeForUpdate(node.left, AFM, savings);
        } else {
            return searchNodeForUpdate(node.right, AFM, savings);
        }
    }

    private TreeNode removeR(TreeNode h, int AFM) {
        if (h == null)
            return null;
        if (AFM < h.item.key()) {
            h.left = removeR(h.left, AFM);
        } else if (AFM > h.item.key()) {
            h.right = removeR(h.right, AFM);
        } else {
            h = joinLR(h.left, h.right);
        }
        if (h != null)
            h.N = size(h.left) + size(h.right) + 1;
        return h;
    }

    private TreeNode joinLR(TreeNode a, TreeNode b) {
        if (a == null)
            return b;
        if (b == null)
            return a;
        b = partR(b, 0);
        b.left = a;
        if (b != null)
            b.N = size(b.left) + size(b.right) + 1;
        return b;
    }

    private TreeNode partR(TreeNode h, int k) {
        if (h == null)
            return null;
        int t = (h.left == null) ? 0 : h.left.N;
        if (t > k) {
            h.left = partR(h.left, k);
            h = rotR(h);
        } else if (t < k) {
            h.right = partR(h.right, k - t - 1);
            h = rotL(h);
        }
        if (h != null)
            h.N = size(h.left) + size(h.right) + 1;
        return h;
    }

    private double sumSavings(TreeNode node) {
        if (node == null)
            return 0;
        return sumSavings(node.left) + node.item.getSavings() + sumSavings(node.right);
    }


    private Lista searchByLastNameRecursive(TreeNode node, String lastName, Lista list) {
        if (node == null) return list;

        list = searchByLastNameRecursive(node.left, lastName, list);

        if (node.item.getLastName().equals(lastName)) {
            if (list == null) {
                list = new Lista(node.item);
            } else {
                list.appendToLista(node.item);
            }
        }
        list = searchByLastNameRecursive(node.right, lastName, list);
        return list;
    }

    private void fillPQ(TreeNode node, PQ maxHeap) {
        if (node == null) return;

        fillPQ(node.left, maxHeap);
        double score = calculateScore(node.item);
        maxHeap.insert(node.item, score);
        fillPQ(node.right, maxHeap);
    }

    private double calculateScore(LargeDepositor depositor) {
        if (depositor.getTaxedIncome() < 8000) {
            return Double.MAX_VALUE;
        } else {
            return depositor.getSavings() - depositor.getTaxedIncome();
        }
    }

}
