package settings;

import evolution.engine.problem_solution.Solution;
import evolution.util.Randomizer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public enum Selections {
    TRUNCATION("Truncation") {
        public List<Solution> select(List<Solution> solutionList, Integer topPercent, Double pte) {
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
    },
    ROULETTE_WHEEL("RouletteWheel") {
        public List<Solution> select(List<Solution> solutionList, Integer topPercent, Double pte) {
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
    },
    TOURNAMENT("Tournament") {
//        void parseString(String configuration) { OLD
//            Pattern pattern = Pattern.compile("^pte=([0-9]+\\.?[0-9]*)$");
//            double value = 0.5;
//            Matcher m = pattern.matcher(configuration);
//            if (m.find())
//                value = Double.parseDouble(m.group(1));
//            selectionValue.set(value);
//        }

        public List<Solution> select(List<Solution> solutionList, Integer topPercent, Double pte) {
            List<Solution> res = new ArrayList<>();
            List<Solution> twoRandomSolutions = new ArrayList<>(2);
            Solution s1, s2;
            double randomNumber;
            // Receive two random solutions from the solutionList:
            for (int i = 0; i < 2; i++) {
                twoRandomSolutions.add(solutionList.get(Randomizer.getRandomNumber(0, solutionList.size() - 1)));
                twoRandomSolutions.add(solutionList.get(Randomizer.getRandomNumber(0, solutionList.size() - 1)));
                twoRandomSolutions.sort((o1, o2) -> {
                    // Sort top to bottom
                    return (int) ((o2.getFitness() - o1.getFitness()) * 1000);
                });
                randomNumber = Randomizer.getRandomNumber(0.0, 1.0);
                if (randomNumber >= pte) {
                    res.add(twoRandomSolutions.get(0));
                } else {
                    res.add(twoRandomSolutions.get(1));
                }
                twoRandomSolutions.clear();
            }
            return res;
        }
    };

    protected String type;

    Selections(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    abstract public List<Solution> select(List<Solution> solutionList, Integer topPercent, Double pte);
}
