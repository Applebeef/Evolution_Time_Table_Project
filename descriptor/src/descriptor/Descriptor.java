package descriptor;

import time_table.TimeTable;
import evolution.engine.EvolutionEngine;
import Generated.ETTDescriptor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Descriptor {
    protected TimeTable timeTable;
    protected EvolutionEngine evolutionEngine;

    public Descriptor(ETTDescriptor gen) {
        timeTable = new TimeTable(gen.getETTTimeTable());
        evolutionEngine = new EvolutionEngine(gen.getETTEvolutionEngine());
    }

    public TimeTable getTimeTable() {
        return timeTable;
    }

    public void setTimeTable(TimeTable value) {
        this.timeTable = value;
    }

    public EvolutionEngine getEngine() {
        return evolutionEngine;
    }

    public void setEvolutionEngine(EvolutionEngine value) {
        this.evolutionEngine = value;
    }

    @Override
    public String toString() {
        String lineSeparator = System.getProperty("line.separator");
        return "Time Table:" + lineSeparator + timeTable + lineSeparator + lineSeparator +
                "Evolution Engine:" + lineSeparator + evolutionEngine;
    }

    public Set<String> checkValidity() {
        Set<String> errorSet = new HashSet<>();
        errorSet.add(timeTable.getSchoolClasses().checkValidity());
        errorSet.add(timeTable.getSchoolClasses().checkSubjectValidity(timeTable.getSubjects()));
        errorSet.add(timeTable.getSchoolClasses().checkHourValidity(timeTable.getHours(),timeTable.getDays()));
        errorSet.add(timeTable.getTeachers().checkIDValidity());
        errorSet.add(timeTable.getTeachers().checkSubjectValidity(timeTable.getSubjects()));
        errorSet.add(timeTable.getSubjects().checkValidity());
        errorSet.add(timeTable.getRules().checkValidity());
        return errorSet;
    }
}
