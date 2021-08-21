package settings;

import evolution.configuration.CrossoverIFC;
import javafx.beans.property.*;

public enum Crossovers implements CrossoverIFC {
    DAY_TIME_ORIENTED("DayTimeOriented"){
        @Override
        void cross() {

        }
    },
    ASPECT_ORIENTED("AspectOriented"){
        @Override
        void cross() {

        }
    };

    protected StringProperty orientation;
    protected String name;
    protected BooleanProperty active;
    protected IntegerProperty cuttingPoints;

    Crossovers(String name) {
        this.name = name;
        cuttingPoints = new SimpleIntegerProperty(0);
        active = new SimpleBooleanProperty(false);
        orientation = new SimpleStringProperty("");
    }

    @Override
    public int getCuttingPoints() {
        return 0;
    }

    abstract void cross();
}
