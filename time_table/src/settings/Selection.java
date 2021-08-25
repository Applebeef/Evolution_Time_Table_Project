package settings;

import Generated.ETTSelection;
import evolution.configuration.SelectionIFC;
import evolution.engine.problem_solution.Solution;
import evolution.util.Randomizer;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Selection implements SelectionIFC {
    protected String type;
    protected IntegerProperty topPercent;
    protected IntegerProperty elitism;

    public Selection(ETTSelection gen) {
        if (gen.getETTElitism() != null) {
            this.elitism = new SimpleIntegerProperty(gen.getETTElitism());
        } else {
            this.elitism = new SimpleIntegerProperty(0);
        }
        this.type = gen.getType();
        if (type.equals("Truncation")) {
            Pattern pattern = Pattern.compile("^TopPercent=(\\d+)$");
            Matcher m = pattern.matcher(gen.getConfiguration());
            if (m.find())
                topPercent = new SimpleIntegerProperty(Integer.parseInt(m.group(1)));
        } else {
            topPercent = null;
        }
    }

    public List<Solution> select(List<Solution> solutionList) {
        // Select according to selection type:
        /* Note: this method returns a List of only TWO solutions to be crossed over.
         *        The two solutions might be the same solution!
         * */
        switch (this.type) {
            case "Truncation":
                return truncationSelect(solutionList);
            case "RouletteWheel":
                return rouletteWheelSelect(solutionList);
            default:
                return null;
        }
    }

    @Override
    public boolean isActive() {
        return false;
    }

    private List<Solution> truncationSelect(List<Solution> solutionList) {
        List<Solution> res = new ArrayList<>();
        int bestSolutionsAmount;

        // bestSolutionsAmount = the amount of the X% best solution (X is given)
        bestSolutionsAmount = (int) Math.floor(solutionList.size() * ((double) topPercent.get() / 100));
        if (bestSolutionsAmount > 0) {
            // Return one of the best solutions (random solution from 0 to bestSolutionsAmount):
            res.add(solutionList.get(Randomizer.getRandomNumber(0, bestSolutionsAmount - 1)));
            res.add(solutionList.get(Randomizer.getRandomNumber(0, bestSolutionsAmount - 1)));
            return res;
        } else {
            return null;
        }
    }

    private List<Solution> rouletteWheelSelect(List<Solution> solutionList) {
        double totalFitnessSum = solutionList.stream().mapToDouble(Solution::getFitness).sum();
        double sum = 0;
        List<Double> randomList = new ArrayList<>();
        List<Solution> res = new ArrayList<>();
        // Add two random doubles in range 0, totalFitnessSum:
        randomList.add(Randomizer.getRandomNumber(0.0, totalFitnessSum));
        randomList.add(Randomizer.getRandomNumber(0.0, totalFitnessSum));
        // Sort the two random doubles:
        Collections.sort(randomList);
        // Index for the random doubles:
        int randomListIndex = 0;

        for (int i = 0; i < solutionList.size() && randomListIndex < 2; i++) {
            // Sum the fitness of the current solution:
            sum += solutionList.get(i).getFitness();
            // Add the solution if the random number(s) is in range:
            while (randomList.get(randomListIndex) < sum) {
                res.add(solutionList.get(i));
                randomListIndex++;
                if (randomListIndex >= 2) {
                    break;
                }
            }
        }
        return res;
    }

    public String getType() {
        return type;
    }

    public int getTopPercent() {
        return topPercent.get();
    }

    public IntegerProperty topPercentProperty() {
        return topPercent;
    }

    public void setTopPercent(int topPercent) {
        this.topPercent.set(topPercent);
    }

    public int getElitism() {
        return elitism.get();
    }

    public IntegerProperty elitismProperty() {
        return elitism;
    }

    public void setElitism(int elitism) {
        this.elitism.set(elitism);
    }

    @Override
    public String toString() {
        return "Selection of type: " + type + ", while choosing top " + topPercent + "%.";
    }

    public String checkElitismValidity(int populationSize) {
        if (elitism.get() >= populationSize) {
            return "Elitism operator bigger than population size.";
        } else {
            return "";
        }
    }
}
