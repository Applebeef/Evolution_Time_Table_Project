package settings;

import Generated.ETTCrossover;
import evolution.configuration.CrossoverIFC;
import evolution.engine.problem_solution.Solution;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Crossover implements CrossoverIFC {
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

    @Override
    public int getCuttingPoints() {
        return cuttingPoints;
    }

    public void setCuttingPoints(int cuttingPoints) {
        this.cuttingPoints = cuttingPoints;
    }

    @Override
    public void initFromXML(ETTCrossover gen) {

    }

    @Override
    public List<? extends Solution> cross(Solution s_1, Solution s_2) {
        return null;
    }

    @Override
    public boolean isActive() {
        return false;
    }

    @Override
    public String toString() {
        String lineSeparator = System.getProperty("line.separator");
        return "   " + "Name - " + this.name + lineSeparator +
                "   " + "Cutting Points - " + this.cuttingPoints + lineSeparator;
    }
}
