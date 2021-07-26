package descriptor;

import Converted.TimeTable;
import EvolutionEngine.Engine;

public class Descriptor {
        protected TimeTable timeTable;
        protected Engine evolutionEngine;

        public TimeTable getTimeTable() {
            return timeTable;
        }

        public void setTimeTable(TimeTable value) {
            this.timeTable = value;
        }

        public Engine getEngine() {
            return evolutionEngine;
        }

        public void setEvolutionEngine(Engine value) {
            this.evolutionEngine = value;
        }

}
