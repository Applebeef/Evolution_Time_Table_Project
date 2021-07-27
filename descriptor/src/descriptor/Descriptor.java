package descriptor;

import Converted.TimeTable;
import evolution.engine.EvolutionEngineData;
import Generated.ETTDescriptor;

public class Descriptor {
    protected TimeTable timeTable;
    protected EvolutionEngineData evolutionEngine;

    public Descriptor(ETTDescriptor gen) {
        timeTable = new TimeTable(gen.getETTTimeTable());
        evolutionEngine = new EvolutionEngineData(gen.getETTEvolutionEngine());
    }

    public TimeTable getTimeTable() {
        return timeTable;
    }

    public void setTimeTable(TimeTable value) {
        this.timeTable = value;
    }

    public EvolutionEngineData getEngine() {
        return evolutionEngine;
    }

    public void setEvolutionEngine(EvolutionEngineData value) {
        this.evolutionEngine = value;
    }

    @Override
    public String toString() {
        String lineSeparator = System.getProperty("line.separator");
        return "Time Table:" + lineSeparator + timeTable + lineSeparator + lineSeparator +
                "Evolution Engine:" + lineSeparator + evolutionEngine;
    }
}
