/*
	Name: 
	Student Number: 
*/

class App4 {

	// Fill your code here (Method)
	static int sumOfDigits(int n) {
		if (n == 0) {
			return 0;
		} else {
			return n % 10 + sumOfDigits(n / 10);
		}
	}

	public static void main(String args[]) {

		// Fill your code here
		System.out.print("The sum of digits of the 1234 is: " + sumOfDigits(1234));
	}
}