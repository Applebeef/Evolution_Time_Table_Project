package evolution.engine.problem_solution;

import evolution.configuration.Crossover;

public interface Solution extends Comparable<Solution> {
    double calculateFitness();
    Solution createNewSolution(Solution solution, Crossover crossover);
}
