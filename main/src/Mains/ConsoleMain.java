package Mains;

import evolution.util.Randomizer;
import ui.UI;

import java.time.Instant;

public class ConsoleMain {
    public static void main(String[] args) {
        for(int i =0;i<100;i++){
            System.out.println(Randomizer.getRandomNumber(0.0,1.0));
        }
        /*UI ui = UI.getInstance();
        ui.runMenu();*/
    }
}
