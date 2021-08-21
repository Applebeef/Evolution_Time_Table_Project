package settings;

import Generated.ETTMutation;
import evolution.configuration.MutationIFC;
import javafx.beans.property.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum Mutation implements MutationIFC {
    Flipping("Flipping") {
        @Override
        protected void parseString(String config) {
            Pattern pattern = Pattern.compile("^MaxTupples=(\\d+),Component=([DHCTS])$");
            Matcher m = pattern.matcher(config);
            if (m.find()) {
                this.tupples.set(Integer.parseInt(m.group(1)));
                this.component = new SimpleStringProperty(m.group(2));
            }
        }

        @Override
        public String toString() {
            return super.toString() + "MaxTupples - " + tupples + " Component - " + component;
        }
    },
    Sizer("Sizer") {
        @Override
        protected void parseString(String config) {
            Pattern pattern = Pattern.compile("^TotalTupples=(\\d+)$");
            Matcher m = pattern.matcher(config);
            if (m.find()) {
                this.tupples.set(Integer.parseInt(m.group(1)));
                this.component = null;
            }
        }

        @Override
        public String toString() {
            return super.toString() + "TotalTupples - " + tupples;
        }
    };

    DoubleProperty probability;
    String name;
    IntegerProperty tupples;
    StringProperty component;

    public int getTupples() {
        return tupples.get();
    }

    public IntegerProperty tupplesProperty() {
        return tupples;
    }

    public String getComponent() {
        return component.get();
    }


    Mutation(String name) {
        this.name = name;
        probability = new SimpleDoubleProperty(0);
        tupples = new SimpleIntegerProperty(0);
        component = new SimpleStringProperty("");
    }

    protected abstract void parseString(String config);

    public double getProbability() {
        return probability.get();
    }

    public void setProbability(double probability) {
        this.probability.set(probability);
    }

    public DoubleProperty probabilityProperty() {
        return probability;
    }

    public String getName() {
        return name;
    }

    public StringProperty componentProperty() {
        return component;
    }

    @Override
    public void initFromXML(ETTMutation gen) {
        setProbability(gen.getProbability());
        parseString(gen.getConfiguration());
    }

    @Override
    public String toString() {
        String lineSeparator = System.getProperty("line.separator");
        return "   " + "Name - " + name + lineSeparator +
                "   " + "Probability - " + probability + lineSeparator +
                "   " + "Configuration - ";
    }
}
