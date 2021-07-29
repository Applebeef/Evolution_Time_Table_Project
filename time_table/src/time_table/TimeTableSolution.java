package time_table;

import days_hours.Day;
import evolution.configuration.Crossover;
import evolution.engine.problem_solution.Solution;
import evolution.rules.Type;

import java.util.ArrayList;
import java.util.List;

public class TimeTableSolution implements Solution {
    private List<Day> dayList;
    private Double fitness;
    private TimeTable timeTable;

    public TimeTableSolution(TimeTable timeTable) {
        this.timeTable = timeTable;
        dayList = new ArrayList<>(timeTable.getDays());
        for (int i = 0; i < timeTable.getDays(); i++) {
            dayList.add(new Day(timeTable));
        }

    }

    @Override
    public void calculateFitness() {
        double fitness;
        double hardRulesWeight = (double) timeTable.rules.getHardRulesWeight() / 100;
        int hardTotal = 0, softTotal = 0;
        int hardCount = 0, softCount = 0;
        for (Rule rule : timeTable.getRules().getRuleList()) {
            if (rule.type == Type.HARD) {
                hardCount++;
                hardTotal += rule.test(this);
            } else {
                softCount++;
                softTotal += rule.test(this);
            }
        }
        this.fitness = ((double) hardTotal / (double) hardCount) * (hardRulesWeight) + ((double) softTotal / (double) softCount) * (1 - hardRulesWeight);
    }

    @Override
    public Solution createNewSolution(Solution solution, Crossover crossover) {
        return null;
    }

    public List<Day> getDayList() {
        return dayList;
    }

    public Double getFitness() {
        return fitness;
    }

    public TimeTable getTimeTable() {
        return timeTable;
    }

    @Override
    public int compareTo(Solution o) {
       return this.getFitness().compareTo(((TimeTableSolution) o).getFitness());
    }
}
