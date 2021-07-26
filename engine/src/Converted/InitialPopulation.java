package Converted;

import Generated.ETTInitialPopulation;

public class InitialPopulation {
    private int size;

    public InitialPopulation(ETTInitialPopulation gen){
        size = gen.getSize();
    }

    public int getSize() {
        return size;
    }
}
