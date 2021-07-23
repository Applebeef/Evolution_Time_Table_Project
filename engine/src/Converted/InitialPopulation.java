package Converted;

import Generated.ETTInitialPopulation;

public class InitialPopulation {

    int size;

    InitialPopulation(ETTInitialPopulation gen){
        size = gen.getSize();
    }

    public int getSize() {
        return size;
    }
}
