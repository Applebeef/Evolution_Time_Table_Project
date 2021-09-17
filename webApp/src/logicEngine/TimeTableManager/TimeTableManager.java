package logicEngine.TimeTableManager;

import time_table.TimeTable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TimeTableManager {
    private final List<TimeTable> timeTableList;

    public TimeTableManager() {
        timeTableList = new ArrayList<>();
    }

    public synchronized void addTimetable(TimeTable timeTable) {
        timeTableList.add(timeTable);
    }

    public synchronized void removeTimeTable(TimeTable timeTable) {
        timeTableList.remove(timeTable);
    }

    public synchronized List<TimeTable> getTimeTables() {
        return Collections.unmodifiableList(timeTableList);
    }

}
