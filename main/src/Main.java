import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
     /*   List<List<Integer>> fifthsArray = new ArrayList<>(5);
        for (int i = 0; i < 5; i++) {
            fifthsArray.add(i, new ArrayList<>(5));
            for (int j = 0; j < 5; j++) {
                fifthsArray.get(i).add(j, j);
            }
        }
        fifthsArray.forEach(lst -> {
            lst.forEach(System.out::print);
            System.out.println();
        });*/
        UI ui = UI.getInstance();
        ui.runMenu();
    }
}
