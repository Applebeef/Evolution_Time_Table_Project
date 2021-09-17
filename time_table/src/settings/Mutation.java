package settings;

import evolution.configuration.MutationIFC;
import evolution.engine.problem_solution.Solution;
import evolution.util.Randomizer;
import javafx.beans.property.*;
import solution.Fifth;
import solution.TimeTableSolution;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum Mutation implements MutationIFC {
    Flipping("Flipping") {
        @Override
        public <T extends Solution> void mutate(T solution) {
            if (solution instanceof TimeTableSolution) {
                TimeTableSolution timeTableSolution = (TimeTableSolution) solution;
                if (checkProbability()) {
                    List<Fifth> toBeMutated = new ArrayList<>();
                    int mutatedNumber = Randomizer.getRandomNumber(1, getTupples());
                    for (int i = 0; i < mutatedNumber; i++) {
                        toBeMutated.add(timeTableSolution.getFifthsList().get(Randomizer.getRandomNumber(0, timeTableSolution.getFifthsList().size() - 1)));
                    }
                    // Mutate the toBeMutated's according to the component in the mutation:
                    switch (getComponent()) {
                        case "D":
                            toBeMutated.forEach(fifth -> fifth.setDay(Randomizer.getRandomNumber(1, timeTableSolution.getTimeTable().getDays())));
                            break;
                        case "H":
                            toBeMutated.forEach(fifth -> fifth.setHour(Randomizer.getRandomNumber(1, timeTableSolution.getTimeTable().getHours())));
                            break;
                        case "C":
                            toBeMutated.forEach(fifth -> fifth.setSchoolClass(Randomizer.getRandomNumber(1, timeTableSolution.getTimeTable().getAmountofSchoolClasses())));
                            break;
                        case "T":
                            toBeMutated.forEach(fifth -> fifth.setTeacher(Randomizer.getRandomNumber(1, timeTableSolution.getTimeTable().getAmountofTeachers())));
                            break;
                        case "S":
                            toBeMutated.forEach(fifth -> fifth.setSubject(Randomizer.getRandomNumber(1, timeTableSolution.getTimeTable().getAmountofSubjects())));
                            break;
                        default:
                            break;
                    }
                    timeTableSolution.calculateFitness();
                }
            }
        }

        @Override
        protected void parseString(String config) {
            Pattern pattern = Pattern.compile("^MaxTupples=(\\d+),Component=([DHCTS])$");
            Matcher m = pattern.matcher(config);
            if (m.find()) {
                this.tupples.set(Integer.parseInt(m.group(1)));
                this.component = new SimpleStringProperty(m.group(2));
            }
        }

        @Override
        public String toString() {
            return super.toString() + "MaxTupples - " + tupples + " Component - " + component;
        }
    },

    Sizer("Sizer") {
        @Override
        public <T extends Solution> void mutate(T solution) {
            if (solution instanceof TimeTableSolution) {
                if (checkProbability()) {
                    TimeTableSolution timeTableSolution = (TimeTableSolution) solution;
                    int mutatedNumber = Randomizer.getRandomNumber(1, getTupples());
                    if (getTupples() < 0) {
                        sizerReduce(mutatedNumber, timeTableSolution);
                    } else {
                        sizerIncrease(mutatedNumber, timeTableSolution);
                    }
                    timeTableSolution.calculateFitness();
                }

            }
        }

        private void sizerIncrease(int mutatedNumber, TimeTableSolution solution) {
            for (int i = 0; i < mutatedNumber && (solution.getFifthsList().size() <= solution.getTimeTable().getDays() * solution.getTimeTable().getHours()); i++) {
                solution.getFifthsList().add(solution.generateRandomFifth());
            }
        }

        private void sizerReduce(int mutatedNumber, TimeTableSolution solution) {
            for (int i = 0; i < mutatedNumber && solution.getFifthsList().size() > solution.getTimeTable().getDays(); i++) {
                int randomIndex = Randomizer.getRandomNumber(0, solution.getFifthsList().size());
                solution.getFifthsList().remove(randomIndex);
            }
        }

        @Override
        protected void parseString(String config) {
            Pattern pattern = Pattern.compile("^TotalTupples=(\\d+)$");
            Matcher m = pattern.matcher(config);
            if (m.find()) {
                this.tupples.set(Integer.parseInt(m.group(1)));
            }
        }

        @Override
        public String toString() {
            return super.toString() + "TotalTupples - " + tupples;
        }
    };

    DoubleProperty probability;
    String name;
    IntegerProperty tupples;
    StringProperty component;

    public int getTupples() {
        return tupples.get();
    }

    public IntegerProperty tupplesProperty() {
        return tupples;
    }

    public String getComponent() {
        return component.get();
    }

    boolean checkProbability() {
        double random = Randomizer.getRandomNumber(0, getProbability());
        return random < getProbability();
    }

    Mutation(String name) {
        this.name = name;
        probability = new SimpleDoubleProperty(0);
        tupples = new SimpleIntegerProperty(0);
        component = new SimpleStringProperty("D");
    }

    protected abstract void parseString(String config);

    public double getProbability() {
        return probability.get();
    }

    public void setProbability(double probability) {
        this.probability.set(probability);
    }

    public DoubleProperty probabilityProperty() {
        return probability;
    }

    public String getName() {
        return name;
    }

    public StringProperty componentProperty() {
        return component;
    }

//    @Override
//    public void initFromXML(ETTMutation gen) { TODO fix
//        setProbability(gen.getProbability());
//        parseString(gen.getConfiguration());
//    }

    @Override
    public String toString() {
        String lineSeparator = System.getProperty("line.separator");
        return "   " + "Name - " + name + lineSeparator +
                "   " + "Probability - " + probability + lineSeparator +
                "   " + "Configuration - ";
    }
}
