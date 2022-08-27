package com.company.client.utils;

import java.util.Scanner;

public class ConsoleUtils {

    public static String getInput(String caption, boolean isSameLineInput) {
        System.out.print(caption);
        if (!isSameLineInput) {
            System.out.print("\n");
        }
        return new Scanner(System.in).nextLine();
    }

    public static String getInput(String caption) {
        return getInput(caption, false);
    }

}
