package evolution.configuration;

import Generated.ETTInitialPopulation;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class InitialPopulation {
    private IntegerProperty size;

    public InitialPopulation(ETTInitialPopulation gen){
        size = new SimpleIntegerProperty(gen.getSize());
    }

    public IntegerProperty sizeProperty() {
        return size;
    }

    public int getSize() {
        return size.get();
    }

    @Override
    public String toString() {return "size - " + this.size;}
}
