package evolution.configuration;

import Generated.ETTCrossover;
import evolution.engine.problem_solution.Solution;
import evolution.util.Randomizer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Crossover {
    protected String configuration;
    protected String name;
    protected int cuttingPoints;

    public Crossover(ETTCrossover ettCrossover) {
        this.name = ettCrossover.getName();
        this.cuttingPoints = ettCrossover.getCuttingPoints();
        if (name.equals("AspectOriented")) {
            Pattern pattern = Pattern.compile("^Orientation=(CLASS|TEACHER)$");
            Matcher m = pattern.matcher(ettCrossover.getConfiguration());
            if (m.find())
                this.configuration = m.group(1);
        } else {
            this.configuration = null;
        }
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
