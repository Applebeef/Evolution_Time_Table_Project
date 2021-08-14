package evolution.configuration;

import Generated.ETTCrossover;
import evolution.engine.problem_solution.Solution;

import java.util.Comparator;


public class Crossover {
    protected String configuration;
    protected String name;
    protected int cuttingPoints;

    public Crossover(ETTCrossover ettCrossover) {
        this.configuration = ettCrossover.getConfiguration();
        this.name = ettCrossover.getName();
        this.cuttingPoints = ettCrossover.getCuttingPoints();
    }

    public String getConfiguration() {
        return configuration;
    }

    public void setConfiguration(String configuration) {
        this.configuration = configuration;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCuttingPoints() {
        return cuttingPoints;
    }

    public void setCuttingPoints(int cuttingPoints) {
        this.cuttingPoints = cuttingPoints;
    }

    public List<Solution> crossover(Solution s1, Solution s2) {
        //TODO: choose comparator
        switch (this.name) {
            case "DayTimeOriented":
                return dayTimeOrientedCrossover(s1, s2);
            case "AspectOriented":
                return aspectOrientedCrossover(s1,s2);
            default:
                return null;
        }
    }

    private List<Solution> dayTimeOrientedCrossover(Solution s1, Solution s2) {
        List<Solution> res = new ArrayList<>(2);
        return res;
    }

    private List<Solution> aspectOrientedCrossover(Solution s1, Solution s2) {
        List<Solution> res = new ArrayList<>(2);
        return res;
    }

    @Override
    public String toString() {
        String lineSeparator = System.getProperty("line.separator");
        return "   " + "Name - " + this.name + lineSeparator +
                "   " + "Cutting Points - " + this.cuttingPoints + lineSeparator;
    }
}
