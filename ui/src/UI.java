import Menu.MenuOptions;

import java.util.Scanner;

public class UI {
    public static void main(String[] args) {

        for(MenuOptions option : MenuOptions.values()){
            int choice;
            System.out.println(option.toString());
        }
    }
}
