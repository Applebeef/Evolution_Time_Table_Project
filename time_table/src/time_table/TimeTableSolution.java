package time_table;

import days_hours.Day;
import evolution.engine.problem_solution.Solution;

import java.util.ArrayList;
import java.util.List;

public class TimeTableSolution implements Solution {
    private List<Day> dayList;
    private Integer fitness = -1;
    private TimeTable timeTable;

    public TimeTableSolution(TimeTable timeTable) {
        this.timeTable = timeTable;
        dayList = new ArrayList<>(timeTable.getDays());
        for (int i = 0; i < timeTable.getDays(); i++) {
            dayList.add(new Day(timeTable));
        }

    }

    @Override
    public Integer calculateFitness() {
        if (fitness < 0) {

        }
        return fitness;
    }


}
