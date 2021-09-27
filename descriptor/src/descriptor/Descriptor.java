package descriptor;

import time_table.TimeTable;
import evolution.engine.EvolutionEngine;
import Generated.ETTDescriptor;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Descriptor {
    protected TimeTable timeTable;
    protected Map<String, EvolutionEngine> engineMap;


    public Descriptor(ETTDescriptor gen) {
        timeTable = new TimeTable(gen.getETTTimeTable());
        engineMap = new HashMap<>();
    }

    public TimeTable getTimeTable() {
        return timeTable;
    }

    public void setTimeTable(TimeTable value) {
        this.timeTable = value;
    }

    public synchronized EvolutionEngine getEngine(String userName) {
        return engineMap.get(userName);
    }

    public void addEvolutionEngine(String userName, EvolutionEngine engine) {
        this.engineMap.put(userName, engine);
    }

    public Map<String, EvolutionEngine> getEngineMap() {
        return engineMap;
    }

    public Set<String> checkValidity() {
        Set<String> errorSet = new HashSet<>();
        errorSet.add(timeTable.getSchoolClasses().checkValidity());
        errorSet.add(timeTable.getSchoolClasses().checkSubjectValidity(timeTable.getSubjects()));
        errorSet.add(timeTable.getSchoolClasses().checkHourValidity(timeTable.getHours(), timeTable.getDays()));
        errorSet.add(timeTable.getTeachers().checkIDValidity());
        errorSet.add(timeTable.getTeachers().checkSubjectValidity(timeTable.getSubjects()));
        errorSet.add(timeTable.getTeachers().checkWorkingHoursValidity(timeTable.getDays(), timeTable.getHours()));
        errorSet.add(timeTable.getSubjects().checkValidity());
        errorSet.add(timeTable.getRules().checkValidity());
        return errorSet;
    }

    @Override
    public String toString() {
        String lineSeparator = System.getProperty("line.separator");
        return "Time Table:" + lineSeparator + timeTable + lineSeparator + lineSeparator;
    }

    public synchronized boolean engineExists(String username) {
        return engineMap.containsKey(username);
    }
}
