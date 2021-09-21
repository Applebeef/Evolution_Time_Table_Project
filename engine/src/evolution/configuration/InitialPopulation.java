package evolution.configuration;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class InitialPopulation {
    private IntegerProperty size;

    public InitialPopulation(Integer size){
        this.size = new SimpleIntegerProperty(size);
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
