import java.util.Scanner;
public class Combinations {
	public static void main (String args[]) {
		Scanner in = new Scanner(System.in);
		
		System.out.print ("Enter number of objects in the set (n <= 12) : "); 
		int n = in.nextInt();
		
		System.out.print ("Enter number to be chosen (k <= 12): ");
		int k = in.nextInt();
		if(n<=12 && k<=12){
			if (n>=k && k>=0) {
				int Factorial_n = 1;
				for (int i = 1; i <= n; i++)
					Factorial_n *= i;
			
				int Factorial_k = 1;
				for (int i = 1; i <= k; i++) 
					Factorial_k *= i;
			
				int Factorial_n_k = 1;
				for (int i = 1; i <= n-k; i++)
					Factorial_n_k *= i;
			
				System.out.println("C(" + n + ", " + k + ") = " + Factorial_n/(Factorial_k*Factorial_n_k));
			}
			else
				System.out.println ("Please enter n >= k >= 0");
	}	
	else
	System.out.println("Please enter n<=12 and k<=12");
	}
}