package days_hours;

import time_table.TimeTable;

import java.util.ArrayList;
import java.util.List;

public class Day {
    private List<Hour> hourList;

    public Day(TimeTable timeTable) {
        hourList = new ArrayList<>(timeTable.getHours());
        for (int i = 0; i < timeTable.getHours(); i++) {
            // NOTE: Calling the random initializing Hour Ctor:
            hourList.add(new Hour(timeTable));
        }
    }



}
