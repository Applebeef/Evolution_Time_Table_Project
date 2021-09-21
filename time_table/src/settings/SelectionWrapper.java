package settings;

import evolution.configuration.SelectionIFC;
import evolution.engine.problem_solution.Solution;
import javafx.beans.property.*;

import java.util.List;

public class SelectionWrapper implements SelectionIFC {
    private Selections selection;
    protected IntegerProperty topPercentProperty;
    protected BooleanProperty active;
    protected IntegerProperty elitism;
    protected DoubleProperty pte;

    public SelectionWrapper(Selections selection, Integer topPercent, Integer elitism, Double pte, Boolean active) {
        this.selection = selection;
        this.topPercentProperty = topPercent != null ? new SimpleIntegerProperty(topPercent) : null;
        this.elitism = new SimpleIntegerProperty(elitism);
        this.pte = pte != null ? new SimpleDoubleProperty(pte) : null;
        this.active = new SimpleBooleanProperty(active);
    }

    @Override
    public int getElitism() {
        return elitism.get();
    }

    @Override
    public String getType() {
        return selection.getType();
    }

    @Override
    public List<Solution> select(List<Solution> solutionList) {
        return selection.select(solutionList, getTopPercentProperty(), getPte());
    }

    @Override
    public boolean isActive() {
        return active.get();
    }

    public int getTopPercentProperty() {
        return topPercentProperty.get();
    }

    public IntegerProperty topPercentPropertyProperty() {
        return topPercentProperty;
    }

    public void setTopPercentProperty(int topPercentProperty) {
        this.topPercentProperty.set(topPercentProperty);
    }

    public BooleanProperty activeProperty() {
        return active;
    }

    public void setActive(boolean active) {
        this.active.set(active);
    }

    public IntegerProperty elitismProperty() {
        return elitism;
    }

    public void setElitism(int elitism) {
        this.elitism.set(elitism);
    }

    public double getPte() {
        return pte.get();
    }

    public DoubleProperty pteProperty() {
        return pte;
    }

    public void setPte(double pte) {
        this.pte.set(pte);
    }

    @Override
    public String checkElitismValidity(int size) {
        if (getElitism() >= size) {
            return "Elitism operator bigger than population size.";
        } else {
            return "";
        }
    }
}
