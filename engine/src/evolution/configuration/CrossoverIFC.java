package evolution.configuration;

import Generated.ETTCrossover;


public interface CrossoverIFC {
    int getCuttingPoints();

    void initFromXML(ETTCrossover gen);
}
