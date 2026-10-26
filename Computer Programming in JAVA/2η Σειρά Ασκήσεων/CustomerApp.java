/*
	Name: 
	Student Number: 
*/

import java.util.Scanner;

public class CustomerApp {
    public static void main(String[] args) {
        CustomerList customerList = new CustomerList();
        Scanner scanner = new Scanner(System.in);
        int choice = -1;
        while (choice != 0) {
            System.out.println("1. Insert a customer");
            System.out.println("2. Search for a customer");
            System.out.println("3. Display all customers");
            System.out.println("0. Exit");
            System.out.print("Your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine(); // read the newline character
            switch (choice) {
                case 1:
                    customerList.insertCustomer();
                    break;
                case 2:
                    System.out.print("Give the customer's code: ");
                    String code = scanner.nextLine();
                    customerList.lookupCustomer(code);
                    break;
                case 3:
                    customerList.displayList();
                    break;
                case 0:
                    System.out.println("End of program.");
                    break;
                default:
                    System.out.println("Invalid choice. Try again.");
                    break;
            }
        }
    }
}
