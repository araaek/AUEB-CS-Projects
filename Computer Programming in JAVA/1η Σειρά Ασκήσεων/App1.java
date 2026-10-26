/*
	Name: 
	Student Number: 
*/

import java.util.Scanner;

class App1 {
	// Fill your code here (Method)
	boolean isPrime(int num) {
    	if (num <= 1) {
			return false;
		}
        for (int i = 2; i <= Math.sqrt(num); i++) {
			if (num % i == 0) {
				return false;
			}
		}
		return true;
	}
		
  	public static void main(String args[]){
		// Fill your code here 
		Scanner in=new Scanner(System.in);
		App1 x=new App1();
		System.out.print("Give an integer: ");
		int number=in.nextInt();	
		boolean result=x.isPrime(number);
		if (result){
			System.out.println("The number " + number + " is prime");
		} else {
			System.out.println("The number " + number + " is not prime");
		}
   	}
}

