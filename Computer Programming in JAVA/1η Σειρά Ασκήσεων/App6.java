/*
	Name: 
	Student Number: 
*/

import java.util.Scanner;

class App6 {

		public static void main (String args []) {
		// Fill your code here
		int[] coinValues = {1000, 500, 200, 100, 50, 20, 10, 5, 2, 1};
		String[] coinNames = {"10 euro", "5 euro", "2 euro", "1 euro", "50 cents", "20 cents", "10 cents", "5 cents", "2 cents", "1 cent"};
		Scanner in = new Scanner (System.in);
		
		System.out.println("-- Parking Ticket Payment --\n");
        System.out.println("Fee: 5 euros and 24 cents");
        System.out.println("Payment...");
		System.out.print("euros = ");
        int euros = in.nextInt();
		while (euros < 0 || euros > 20) {
            System.out.print("euros = ");
            euros = in.nextInt();
        }
		System.out.print("cents = ");
        int cents = in.nextInt();
		while (cents < 0 || cents > 99) {
            System.out.print("cents = ");
            cents = in.nextInt();
        }
		int total = euros * 100 + cents;
        int change = total - (5 * 100 + 24);
		if (change < 0) {
            System.out.println("Not enough money!");
        } else {
			System.out.println("\nChange:");
			for (int i = 0; i < coinValues.length; i++) {
				int coins = change / coinValues[i];
				if (coins > 0) {
					System.out.println(coins + "  (" + coinNames[i] + ")");
					change -= coins * coinValues[i];
				}
			}
		}
	}	
}
