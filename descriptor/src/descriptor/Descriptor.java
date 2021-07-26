package descriptor;

import Converted.TimeTable;
import Converted.EvolutionEngine;
import Generated.ETTDescriptor;

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

}
