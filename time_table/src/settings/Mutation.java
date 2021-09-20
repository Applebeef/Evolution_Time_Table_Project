package settings;

import evolution.configuration.MutationIFC;
import evolution.engine.problem_solution.Solution;
import evolution.util.Randomizer;
import solution.Fifth;
import solution.TimeTableSolution;

import java.util.ArrayList;
import java.util.List;

public enum Mutation {
    Flipping("Flipping") {
        public <T extends Solution> void mutate(T solution, String component, Double probability, Integer tupples) {
            if (solution instanceof TimeTableSolution) {
                TimeTableSolution timeTableSolution = (TimeTableSolution) solution;
                if (checkProbability(probability)) {
                    List<Fifth> toBeMutated = new ArrayList<>();
                    int mutatedNumber = Randomizer.getRandomNumber(1, tupples);
                    for (int i = 0; i < mutatedNumber; i++) {
                        toBeMutated.add(timeTableSolution.getFifthsList().get(Randomizer.getRandomNumber(0, timeTableSolution.getFifthsList().size() - 1)));
                    }
                    // Mutate the toBeMutated's according to the component in the mutation:
                    switch (component) {
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
    },
    Sizer("Sizer") {
        @Override
        public <T extends Solution> void mutate(T solution, String component, Double probability, Integer tupples) {
            if (solution instanceof TimeTableSolution) {
                if (checkProbability(probability)) {
                    TimeTableSolution timeTableSolution = (TimeTableSolution) solution;
                    int mutatedNumber = Randomizer.getRandomNumber(1, tupples);
                    if (tupples < 0) {
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
    };

    String name;

    boolean checkProbability(double probability) {
        double random = Randomizer.getRandomNumber(0.0, 1.0);
        return random < probability;
    }

    Mutation(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    abstract public <T extends Solution> void mutate(T solution, String component, Double probability, Integer tupples);
}
