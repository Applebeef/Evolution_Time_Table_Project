package evolution.configuration;

import evolution.engine.problem_solution.Solution;

import java.util.List;


public interface CrossoverIFC {
    int getCuttingPoints();
    List<? extends Solution> cross(Solution s_1,Solution s_2);
    boolean isActive();
}
