/*
	Name: 
	Student Number: 
*/

import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class ClockApp {
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter current hour (0-23): ");
        int hour = scanner.nextInt();

        System.out.print("Enter current minute (0-59): ");
        int min = scanner.nextInt();

        System.out.print("Enter current second (0-59): ");
        int sec = scanner.nextInt();

        Clock clock = new Clock(hour, min, sec);

        for (int i = 0; i < 180; i++) {
            System.out.println(clock.toString());
            clock.tick();
            TimeUnit.SECONDS.sleep(1);
        }

        scanner.close();
    }
}

