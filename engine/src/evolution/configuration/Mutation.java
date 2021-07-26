package evolution.configuration;

import java.util.regex.*;
public enum Mutation {
    Flipping("Flipping"){

    };

    double probability;
    String name;
    Configuration config;

    public class Configuration{
        int maxTupples;
        String component;

        Configuration(String configString){
            Pattern pattern = Pattern.compile("^MaxTupples=(\\d+),Component=([DHCTS])$");//TODO ask Aviad about configuration being a string
            Matcher m = pattern.matcher(configString);
            if(m.find()) {
                maxTupples = Integer.parseInt(m.group(1));
                component = m.group(2);
            }
        }

        public int getMaxTupples() {
            return maxTupples;
        }

        public String getComponent() {
            return component;
        }

        @Override
        public String toString() {
            String lineSeparator = System.getProperty("line.separator");
            return "Max Tupples: " + maxTupples + ", Component: " + component;
        }
    }



    Mutation(String name){
        this.name = name;
    }

    public double getProbability() {
        return probability;
    }

    public void setProbability(double probability) {
        this.probability = probability;
    }

    public String getName() {
        return name;
    }

    public Configuration getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = new Configuration(config);
    }

    @Override
    public String toString() {
        String lineSeparator = System.getProperty("line.separator");
        return  "   " + "Name - " + name + lineSeparator +
                "   " + "Probability - " + probability + lineSeparator +
                "   " + "Configuration - " + config;
    }
}
