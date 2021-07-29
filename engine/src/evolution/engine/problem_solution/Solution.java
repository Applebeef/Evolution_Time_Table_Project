package evolution.engine.problem_solution;

import evolution.configuration.Crossover;

public interface Solution extends Comparable<Solution> {
    void calculateFitness();
    Solution createNewSolution(Solution solution, Crossover crossover);
}
