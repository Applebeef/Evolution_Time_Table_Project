package settings;

import evolution.configuration.SelectionIFC;
import evolution.engine.problem_solution.Solution;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;

import java.util.List;

public class SelectionWrapper implements SelectionIFC {
    private Selections selection;
    protected IntegerProperty topPercentProperty;
    protected BooleanProperty active;
    protected IntegerProperty elitism;
    protected DoubleProperty pte;

    public SelectionWrapper(Selections selection, IntegerProperty topPercentProperty, IntegerProperty elitism, DoubleProperty pte) {
        this.selection = selection;
        this.topPercentProperty = topPercentProperty;
        this.elitism = elitism;
        this.pte = pte;
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