package org.hadifaraz.server;

import java.util.*;

public class Misc {
    public static void promptErr(Exception e) {
        Scanner scan = new Scanner(System.in);

        System.out.print("Would you like to see the error? (y, n): ");

        char input = scan.nextLine().trim().toLowerCase().charAt(0);

        if (input == 'n')
            return;

        System.out.println(e);
        e.printStackTrace();
    }
}
