package Mains;

import ui.UI;

import java.time.Instant;

public class ConsoleMain {
    public static void main(String[] args) {
        String num = "1,404";
        num.replace(",","");
        Integer i = Integer.getInteger(num);

        System.out.println(i);
        /*UI ui = UI.getInstance();
        ui.runMenu();*/
    }
}
