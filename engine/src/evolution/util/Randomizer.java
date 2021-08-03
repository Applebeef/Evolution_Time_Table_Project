package evolution.util;

public class Randomizer {
    public static int getRandomNumber(int min, int max) { // return a random int between min and max (both inclusive).
        return (int) ((Math.random() * (max + 1 - min)) + min);
    }
}
