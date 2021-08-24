package evolution.configuration;

import Generated.ETTCrossover;
import evolution.engine.problem_solution.Solution;

import java.util.List;


public interface CrossoverIFC {
    int getCuttingPoints();
    void initFromXML(ETTCrossover gen);
    List<? super Solution> cross(Solution s_1,Solution s_2);
}
