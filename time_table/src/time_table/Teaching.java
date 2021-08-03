package time_table;

import Generated.ETTTeaching;

import java.util.List;
import java.util.stream.Collectors;

public class Teaching {
    List<Teaches> teachesList;

    Teaching(ETTTeaching gen){
        teachesList = gen.getETTTeaches().stream().map(Teaches::new).collect(Collectors.toList());
    }

    public List<Teaches> getTeachesList() {
        return teachesList;
    }

    @Override
    public String toString() {

        String lineSeparator = System.getProperty("line.separator");
        StringBuilder result = new StringBuilder();
        for(Teaches teaches : teachesList){
            result.append(teaches.toString()).append(", ");
        }
        return result.toString();
    }
}
