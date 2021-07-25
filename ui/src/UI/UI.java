package UI;

import Menu.MenuOptions;

import java.util.Scanner;

public class UI {

    public static boolean exit = false;

    public static void main(String[] args) {
        int choice;
        while(!exit) {
            for (MenuOptions option : MenuOptions.values()) {
                System.out.println(option.toString());
            }
            Scanner scanner = new Scanner(System.in);
            choice = scanner.nextInt();
            MenuOptions.values()[choice - 1].start();
        }
    }
}
