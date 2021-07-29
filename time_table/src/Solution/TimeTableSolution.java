package Solution;

import evolution.configuration.Crossover;
import evolution.engine.problem_solution.Solution;
import evolution.rules.Type;
import time_table.Rule;
import time_table.TimeTable;

import java.util.ArrayList;
import java.util.List;

public class TimeTableSolution implements Solution {
    private List<Fifth> fifthsList;
    private Double fitness;
    private TimeTable timeTable;

    public TimeTableSolution(TimeTable timeTable) {
        this.timeTable = timeTable;
        fifthsList = new ArrayList<>();
        for (int day = 1; day <= timeTable.getDays(); day++) {
            for (int hour = 1; hour <= timeTable.getHours(); hour++) {
                for (int schoolClass = 1; schoolClass <= timeTable.getAmountofSchoolClasses(); schoolClass++) {
                    int teacher = getRandomNumber(1, timeTable.getAmountofTeachers());
                    int subject = getRandomNumber(1, timeTable.getAmountofSubjects());
                    fifthsList.add(new Fifth(day, hour, schoolClass, teacher, subject));
                }
            }
        }

    }

    @Override
    public double calculateFitness() {
        double fitness;
        double hardRulesWeight = (double) timeTable.getRules().getHardRulesWeight() / 100;
        int hardTotal = 0, softTotal = 0;
        int hardCount = 0, softCount = 0;
        for (Rule rule : timeTable.getRules().getRuleList()) {
            if (rule.getType() == Type.HARD) {
                hardCount++;
                hardTotal += rule.test(this);
            } else {
                softCount++;
                softTotal += rule.test(this);
            }
        }
        this.fitness = ((double) hardTotal / (double) hardCount) * (hardRulesWeight) + ((double) softTotal / (double) softCount) * (1 - hardRulesWeight);
        return this.fitness;
    }

    @Override
    public Solution createNewSolution(Solution solution, Crossover crossover) {
        return null;
    }

    public List<Fifth> getFifthsList() {
        return fifthsList;
    }

    public Double getFitness() {
        return fitness;
    }

    public TimeTable getTimeTable() {
        return timeTable;
    }

    private int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    @Override
    public int compareTo(Solution o) {
        return this.getFitness().compareTo(((TimeTableSolution) o).getFitness());
    }
}
