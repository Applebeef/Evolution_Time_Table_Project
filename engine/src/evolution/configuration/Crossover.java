package evolution.configuration;

import Generated.ETTCrossover;

import javax.xml.bind.annotation.XmlAttribute;

public class Crossover {
    //protected String configuration;
    protected String name;
    protected int cuttingPoints;

    public Crossover(ETTCrossover ettCrossover) {
        //this.configuration = ettCrossover.getConfiguration();
        this.name = ettCrossover.getName();
        this.cuttingPoints = ettCrossover.getCuttingPoints();
    }

   /* public String getConfiguration() {
        return configuration;
    }

    public void setConfiguration(String configuration) {
        this.configuration = configuration;
    }*/

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCuttingPoints() {
        return cuttingPoints;
    }

    public void setCuttingPoints(int cuttingPoints) {
        this.cuttingPoints = cuttingPoints;
    }

    @Override
    public String toString() {
        String lineSeparator = System.getProperty("line.separator");
        return "   " + "Name - " + this.name + lineSeparator +
                "   " + "Cutting Points - " + this.cuttingPoints + lineSeparator;
    }

}
