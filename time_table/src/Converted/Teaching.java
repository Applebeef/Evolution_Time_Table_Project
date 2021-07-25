package Converted;

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
}
