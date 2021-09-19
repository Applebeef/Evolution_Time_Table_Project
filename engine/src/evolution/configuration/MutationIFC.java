package evolution.configuration;

import evolution.engine.problem_solution.Solution;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;

public interface MutationIFC {
    String getName();

    default Double getProbability() {
        return null;
    }

    default Integer getTupples() {
        return null;
    }


    public <T extends Solution> void mutate(T solution);

    default IntegerProperty tupplesProperty() {
        return null;
    }

    default StringProperty componentProperty() {
        return null;
    }

    default DoubleProperty probabilityProperty() {
        return null;
    }
}
