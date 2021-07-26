package evolution.configuration;

import Generated.ETTInitialPopulation;

public class InitialPopulation {
    private int size;

    public InitialPopulation(ETTInitialPopulation gen){
        size = gen.getSize();
    }

    public int getSize() {
        return size;
    }

    @Override
    public String toString() {return "size - " + this.size;}
}
