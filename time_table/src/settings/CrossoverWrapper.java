package settings;

import evolution.configuration.CrossoverIFC;
import evolution.engine.problem_solution.Solution;
import javafx.beans.property.*;

import java.util.List;

public class CrossoverWrapper implements CrossoverIFC {
    private Crossovers crossover;
    protected StringProperty orientation;
    protected BooleanProperty active;
    protected IntegerProperty cuttingPoints;

    public CrossoverWrapper(Crossovers crossover, String orientation, Integer cuttingPoints) {
        this.crossover = crossover;
        this.orientation = orientation != null ? new SimpleStringProperty(orientation) : null;
        this.cuttingPoints = cuttingPoints != null ? new SimpleIntegerProperty(cuttingPoints) : null;
    }

    @Override
    public int getCuttingPoints() {
        return cuttingPoints.get();
    }

    public String getOrientation() {
        return orientation.get();
    }

    public StringProperty orientationProperty() {
        return orientation;
    }

    public void setOrientation(String orientation) {
        this.orientation.set(orientation);
    }

    public BooleanProperty activeProperty() {
        return active;
    }

    public void setActive(boolean active) {
        this.active.set(active);
    }

    public IntegerProperty cuttingPointsProperty() {
        return cuttingPoints;
    }

    public void setCuttingPoints(int cuttingPoints) {
        this.cuttingPoints.set(cuttingPoints);
    }

    @Override
    public List<? extends Solution> cross(Solution s_1, Solution s_2) {
        return crossover.cross(s_1, s_2, getCuttingPoints(), getOrientation());
    }

    @Override
    public boolean isActive() {
        return active.get();
    }
}
