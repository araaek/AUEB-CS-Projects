/*
	Name: 
	Student Number: 
*/

import java.util.Scanner;

class Calc {

	// Fill your code here
	private int int1;
	private int int2;

	Calc(int int1, int int2) {
		this.int1 = int1;
		this.int2 = int2;
	}

	void display() {
		System.out.println("1st = " + int1);
		System.out.println("2nd = " + int2);
	}

	void setValues(int int1, int int2) {
		this.int1 = int1;
		this.int2 = int2;
	}

	public int multiply() {
		return this.int1 * this.int2;
	}

	public int add() {
		return this.int1 + this.int2;
	}
}

class App5 {

	public static void main(String args[]) {

		// Fill your code here
		Scanner in = new Scanner(System.in);
		
		System.out.print("Enter 1st integer: ");
		int x1 = in.nextInt();
		System.out.print("Enter 2nd integer: ");
		int x2 = in.nextInt();

		Calc calculator = new Calc(x1, x2);
		
		System.out.println("\n");
		System.out.println("Input:");
		calculator.display();
		System.out.println("\n");
		System.out.println("Output:");
		calculator.display();
		System.out.println("sum = " + calculator.add());
		System.out.println("product = " + calculator.multiply());
		
		System.out.println("\n");
		System.out.print("Enter an integer: ");
		int x3 = in.nextInt();
		System.out.print("Enter one more integer: ");
		int x4 = in.nextInt();
		calculator.setValues(x3, x4);
		System.out.println("\n");
		System.out.println("Input:");
		calculator.display();
		System.out.println("\n");
		System.out.println("Output:");
		calculator.display();
		System.out.println("sum = " + calculator.add());
		System.out.println("product = " + calculator.multiply());
	}

}
