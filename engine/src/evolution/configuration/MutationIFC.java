package evolution.configuration;

import evolution.engine.problem_solution.Solution;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;

public interface MutationIFC {
    String getName();

    double getProbability();

    int getTupples();

    //public void initFromXML(ETTMutation gen); TODO fix

    public <T extends Solution> void mutate(T solution);

    default IntegerProperty tupplesProperty() {
        return null;
    }

    default StringProperty componentProperty() {
        return null;
    }
    default DoubleProperty probabilityProperty(){
        return null;
    }
}
