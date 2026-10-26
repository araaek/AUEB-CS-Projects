/*
	Name: 
	Student Number: 
*/

import java.util.Scanner;

public class CustomerList {
    private Customer[] myList;
    private int size;

    public CustomerList() {
        myList = new Customer[50];
        size = 0;
    }

    public void insertCustomer() {
        if (size == myList.length) {
            System.out.println("Customer list is full. Cannot add a new customer.");
            return;
        }
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter customer name: ");
        String name = scanner.nextLine();
        System.out.print("Enter customer code: ");
        String code = scanner.nextLine();
        System.out.print("Enter registration date (dd/mm/yyyy): ");
        String[] dateParts = scanner.nextLine().split("/");
        int day = Integer.parseInt(dateParts[0]);
        int month = Integer.parseInt(dateParts[1]);
        int year = Integer.parseInt(dateParts[2]);
        myDate registrationDate = new myDate(day, month, year);
        Customer customer = new Customer(name, code, day, month, year);
        myList[size] = customer;
        size++;
        System.out.println("Customer added successfully.");
    }

    public void lookupCustomer(String code) {
        for (int i = 0; i < size; i++) {
            if (myList[i].getCode().equals(code)) {
                System.out.println("Name: " + myList[i].getName() + ", Registration Date: "
                        + myList[i].getRegistrationDate().toString());
                return;
            }
        }
        System.out.println("Customer with code " + code + " not found.");
    }

    public void displayList() {
        for (int i = 0; i < size; i++) {
            System.out.println(myList[i].toString());
        }
    }
}
