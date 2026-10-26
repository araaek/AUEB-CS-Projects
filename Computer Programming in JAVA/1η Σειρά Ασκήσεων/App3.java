/*
	Name: 
	Student Number: 
*/

import java.io.*;

class App3 {

	public static void main(String args[]) throws IOException {
		// Fil your code here

		int count = 0;
		char c1, c2;
		System.out.println("Write some text, finish with an * in a new line:");
		for (;;) {
			c1 = (char) System.in.read();
			if (c1 == (char) 10) { // the ascii code for new line is 10
				c2 = (char) System.in.read();
				if (c2 == '*')
					break;
			}
			if (!((c1 >= 'a' && c1 <= 'z') || (c1 >= 'A' && c1 <= 'Z')))
				count++;
		}
		System.out.println("Words " + count);
	}
}
