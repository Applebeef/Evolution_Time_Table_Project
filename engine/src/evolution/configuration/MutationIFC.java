package evolution.configuration;

import Generated.ETTMutation;
import evolution.engine.problem_solution.Solution;

public interface MutationIFC {
    String getName();
    double getProbability();
    int getTupples();
    void initFromXML(ETTMutation gen);
    public <T extends Solution> void mutate(T solution);

}
