package UI;

import Converted.Teacher;
import Menu.*;
import descriptor.Descriptor;
import java.util.Scanner;

public class UI {

    public static boolean exit = false;
    public static Descriptor descriptor;

    public static boolean isExit() {
        return exit;
    }

    public static void setExit(boolean exit) {
        UI.exit = exit;
    }

    public static Descriptor getDescriptor() {
        return descriptor;
    }

    public static void setDescriptor(Descriptor descriptor) {
        UI.descriptor = descriptor;
    }

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
