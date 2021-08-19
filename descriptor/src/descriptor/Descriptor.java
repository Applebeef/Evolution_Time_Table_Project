package descriptor;

import solution.Crossover;
import time_table.TimeTable;
import solution.*;
import evolution.engine.EvolutionEngine;
import Generated.ETTDescriptor;

import java.util.HashSet;
import java.util.Set;

public class Descriptor {
    protected TimeTable timeTable;
    protected EvolutionEngine evolutionEngine;
    Mutations mutations;
    Selection selection;
    Crossover crossover;


    public Descriptor(ETTDescriptor gen) {
        timeTable = new TimeTable(gen.getETTTimeTable());
        mutations = new Mutations(gen.getETTEvolutionEngine().getETTMutations());
        selection = new Selection(gen.getETTEvolutionEngine().getETTSelection());
        crossover = new Crossover(gen.getETTEvolutionEngine().getETTCrossover());
        evolutionEngine = new EvolutionEngine(gen.getETTEvolutionEngine(), crossover, mutations, selection);
    }

    public Mutations getMutations() {
        return mutations;
    }

    public Selection getSelection() {
        return selection;
    }

    public Crossover getCrossover() {
        return crossover;
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
        errorSet.add(timeTable.getSchoolClasses().checkHourValidity(timeTable.getHours(), timeTable.getDays()));
        errorSet.add(timeTable.getTeachers().checkIDValidity());
        errorSet.add(timeTable.getTeachers().checkSubjectValidity(timeTable.getSubjects()));
        errorSet.add(timeTable.getSubjects().checkValidity());
        errorSet.add(timeTable.getRules().checkValidity());
        errorSet.add(evolutionEngine.getSelection().checkElitismValidity(evolutionEngine.getInitialSolutionPopulation().getSize()));
        return errorSet;
    }
}
