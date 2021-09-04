package settings;

import Generated.ETTSelection;
import evolution.configuration.SelectionIFC;
import evolution.engine.problem_solution.Solution;
import evolution.util.Randomizer;
import javafx.beans.property.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public enum Selections implements SelectionIFC {
    TRUNCATION("Truncation", 1) {
        @Override
        public List<Solution> select(List<Solution> solutionList) {
            List<Solution> res = new ArrayList<>();
            int bestSolutionsAmount;

            // bestSolutionsAmount = the amount of the X% best solution (X is given)
            bestSolutionsAmount = (int) Math.floor(solutionList.size() * (((double) topPercentProperty.get() / 100)));
            if (bestSolutionsAmount > 0) {
                // Return one of the best solutions (random solution from 0 to bestSolutionsAmount):
                res.add(solutionList.get(Randomizer.getRandomNumber(0, bestSolutionsAmount - 1)));
                res.add(solutionList.get(Randomizer.getRandomNumber(0, bestSolutionsAmount - 1)));
                return res;
            } else {
                return null;
            }
        }

        @Override
        void parseString(String configuration) {
            int percent = 1;
            Pattern pattern = Pattern.compile("^TopPercent=(\\d+)$");
            Matcher m = pattern.matcher(configuration);
            if (m.find())
                percent = Integer.parseInt(m.group(1));
            topPercentProperty.set(percent);
        }
    },
    ROULETTE_WHEEL("RouletteWheel", 1) {
        @Override
        void parseString(String configuration) {

        }

        @Override
        public List<Solution> select(List<Solution> solutionList) {
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
    TOURNAMENT("Tournament", 1) {
        @Override
        void parseString(String configuration) {
            Pattern pattern = Pattern.compile("^pte=(0(\\.\\d+)?|1(\\.0+)?)$");
            double value = 0.5;
            Matcher m = pattern.matcher(configuration);
            if (m.find())
                value = Double.parseDouble(m.group(1));
            pte.set(value);
        }

        @Override
        public List<Solution> select(List<Solution> solutionList) {
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
                if (randomNumber >= pte.doubleValue()) {
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
    protected IntegerProperty topPercentProperty;
    protected BooleanProperty active;
    protected IntegerProperty elitism;
    protected DoubleProperty pte;

    Selections(String type, int topPercentValue) {
        this.type = type;
        elitism = new SimpleIntegerProperty(0);
        active = new SimpleBooleanProperty(false);
        this.topPercentProperty = new SimpleIntegerProperty(topPercentValue);
        this.pte = new SimpleDoubleProperty(0.5);


        active.addListener((observable, oldValue, newValue) -> {
            if (newValue.equals(true)) {
                for (Selections selections : Selections.values()) {
                    if (!selections.equals(this)) {
                        selections.setActive(false);
                    }
                }
            }
        });
    }

    public void initFromXml(ETTSelection gen) {
        if (gen.getETTElitism() == null) {
            setElitism(0);
        } else {
            setElitism(gen.getETTElitism());
        }
        setActive(true);
        parseString(gen.getConfiguration());
    }

    abstract void parseString(String configuration);


    public DoubleProperty pteProperty() {
        return pte;
    }

    @Override
    public int getElitism() {
        return elitism.get();
    }

    @Override
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public IntegerProperty topPercentProperty() {
        return topPercentProperty;
    }

    public IntegerProperty elitismProperty() {
        return elitism;
    }

    public void setElitism(int elitism) {
        this.elitism.set(elitism);
    }

    public boolean isActive() {
        return active.get();
    }

    public BooleanProperty activeProperty() {
        return active;
    }

    public void setActive(boolean active) {
        this.active.set(active);
    }


    @Override
    public String checkElitismValidity(int size) {
        if (elitism.get() >= size) {
            return "Elitism operator bigger than population size.";
        } else {
            return "";
        }
    }
}
