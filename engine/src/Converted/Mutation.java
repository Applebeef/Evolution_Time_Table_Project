package Converted;

import java.util.regex.*;
public enum Mutation {
    Flipping("Flipping"){

    };

    double probability;
    String name;
    public class Configuration{

        int maxTupples;
        String component;

        Configuration(String configString){
            Pattern pattern = Pattern.compile("^MaxTupples=(\\d+),Component=([DHCTS])$");//TODO ask Aviad about configuration being a string
            Matcher m = pattern.matcher(configString);
            maxTupples = Integer.parseInt(m.group(1));
            component = m.group(2);
        }

        public int getMaxTupples() {
            return maxTupples;
        }

        public String getComponent() {
            return component;
        }
    }

    Configuration config;

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
}
