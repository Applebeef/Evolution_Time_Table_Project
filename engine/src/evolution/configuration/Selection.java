package evolution.configuration;

import Generated.ETTSelection;
import evolution.engine.problem_solution.Solution;
import evolution.util.Randomizer;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.*;

public class Selection {
    protected String type;
    protected int topPercent;
    protected Integer elitism;

    public Selection(ETTSelection gen) {
        this.elitism = gen.getETTElitism();
        this.type = gen.getType();
        Pattern pattern = Pattern.compile("^TopPercent=(\\d+)$");
        Matcher m = pattern.matcher(gen.getConfiguration());
        if (m.find())
            topPercent = Integer.parseInt(m.group(1));
    }

    public Integer getElitism() {
        return elitism;
    }

    public Solution select(List<Solution> solutionList) {
        switch (this.type) {
            case "Truncation":
                return truncationSelect(solutionList);
            case "RouletteWheel":
                return rouletteWheelSelect(solutionList);
            default:
                return null;
        }
    }

    private Solution truncationSelect(List<Solution> solutionList) {
        int bestSolutionsAmount;

        // bestSolutionsAmount = the amount of the X% best solution (X is given)
        bestSolutionsAmount = (int) Math.floor(solutionList.size() * ((double) topPercent / 100));
        if (bestSolutionsAmount > 0) {
            // Return one of the best solutions (random solution from 0 to bestSolutionsAmount):
            return solutionList.get(Randomizer.getRandomNumber(0, bestSolutionsAmount - 1));
        } else {
            return null;
        }
    }

    private Solution rouletteWheelSelect(List<Solution> solutionList) {
        double sum = solutionList.stream().mapToDouble(Solution::getFitness).sum();
        return null;
    }

    public String getType() {
        return type;
    }

    public int getTopPercent() {
        return topPercent;
    }

    @Override
    public String toString() {
        return "Selection of type: " + type + ", while choosing top " + topPercent + "%.";
    }
}
