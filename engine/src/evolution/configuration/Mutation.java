package evolution.configuration;

import java.util.List;
import java.util.regex.*;

public enum Mutation {
    Flipping("Flipping") {
        @Override
        protected void parseString(String config) {
            Pattern pattern = Pattern.compile("^MaxTupples=(\\d+),Component=([DHCTS])$");
            Matcher m = pattern.matcher(config);
            if (m.find()) {
                this.tupples = Integer.parseInt(m.group(1));
                this.component = m.group(2);
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
                this.tupples = Integer.parseInt(m.group(1));
                this.component = null;
            }
        }

        @Override
        public String toString() {
            return super.toString() + "TotalTupples - " + tupples;
        }
    };

    double probability;
    String name;
    int tupples;
    String component;

    public int getTupples() {
        return tupples;
    }

    public String getComponent() {
        return component;
    }


    Mutation(String name) {
        this.name = name;
    }

    protected abstract void parseString(String config);

    public double getProbability() {
        return probability;
    }

    public void setProbability(double probability) {
        this.probability = probability;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        String lineSeparator = System.getProperty("line.separator");
        return "   " + "Name - " + name + lineSeparator +
                "   " + "Probability - " + probability + lineSeparator +
                "   " + "Configuration - ";
    }
}
