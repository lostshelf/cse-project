package org.hadifaraz.server;

import java.util.*;

public class Misc {
    // Prompts the user if they want to see the error
    public static void promptErr(Exception e) {
        Scanner scan = new Scanner(System.in);

        System.out.print("Would you like to see the error? (y, n): ");

        // Will fail if nothing is input
        char input = scan.nextLine().trim().toLowerCase().charAt(0);

        if (input == 'n')
            return;

        System.out.println(e);
        e.printStackTrace();
    }
}
