package evolution.configuration;

import Generated.ETTSelection;
import evolution.engine.problem_solution.Solution;
import evolution.util.Randomizer;

import java.util.*;
import java.util.regex.*;

public class Selection {
    protected String type;
    protected Integer topPercent;
    protected Integer elitism;

    public Selection(ETTSelection gen) {
        this.elitism = gen.getETTElitism();
        this.type = gen.getType();
        if (type.equals("Truncation")) {
            Pattern pattern = Pattern.compile("^TopPercent=(\\d+)$");
            Matcher m = pattern.matcher(gen.getConfiguration());
            if (m.find())
                topPercent = Integer.parseInt(m.group(1));
        } else {
            topPercent = null;
        }
    }

    public Integer getElitism() {
        return elitism;
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

    private List<Solution> truncationSelect(List<Solution> solutionList) {
        List<Solution> res = new ArrayList<>();
        int bestSolutionsAmount;

        // bestSolutionsAmount = the amount of the X% best solution (X is given)
        bestSolutionsAmount = (int) Math.floor(solutionList.size() * ((double) topPercent / 100));
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
        return topPercent;
    }

    @Override
    public String toString() {
        return "Selection of type: " + type + ", while choosing top " + topPercent + "%.";
    }
}
