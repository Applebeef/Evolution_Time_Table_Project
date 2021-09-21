package descriptor;

import time_table.TimeTable;
import evolution.engine.EvolutionEngine;
import Generated.ETTDescriptor;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Descriptor {
    protected TimeTable timeTable;
    Map<String, EvolutionEngine> engineMap;


    public Descriptor(ETTDescriptor gen) {
        timeTable = new TimeTable(gen.getETTTimeTable());
    }

    public TimeTable getTimeTable() {
        return timeTable;
    }

    public void setTimeTable(TimeTable value) {
        this.timeTable = value;
    }

    public EvolutionEngine getEngine(String userName) {
        return engineMap.get(userName);
    }

    public void addEvolutionEngine(String userName, EvolutionEngine engine) {
        this.engineMap.put(userName, engine);
    }

    @Override
    public String toString() {
        String lineSeparator = System.getProperty("line.separator");
        return "Time Table:" + lineSeparator + timeTable + lineSeparator + lineSeparator;
    }

    public Set<String> checkValidity() {
        Set<String> errorSet = new HashSet<>();
        errorSet.add(timeTable.getSchoolClasses().checkValidity());
        errorSet.add(timeTable.getSchoolClasses().checkSubjectValidity(timeTable.getSubjects()));
        errorSet.add(timeTable.getSchoolClasses().checkHourValidity(timeTable.getHours(), timeTable.getDays()));
        errorSet.add(timeTable.getTeachers().checkIDValidity());
        errorSet.add(timeTable.getTeachers().checkSubjectValidity(timeTable.getSubjects()));
        errorSet.add(timeTable.getSubjects().checkValidity());
        errorSet.add(timeTable.getRules().checkValidity());
//        timeTable.getSelectionsList().forEach(selections -> { TODO check if needs fix or delete
//            errorSet.add(selections.checkElitismValidity(evolutionEngine.getInitialSolutionPopulation().getSize()));
//        });
        return errorSet;
    }
}
