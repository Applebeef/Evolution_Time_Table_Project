import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        /*List<Integer> arr = new ArrayList<>();

        for(int i=0;i<10;i++){
            arr.add(i);
        }

        arr.subList(0,0).forEach(System.out::print);*/
        UI ui = UI.getInstance();
        ui.runMenu();
    }
}
