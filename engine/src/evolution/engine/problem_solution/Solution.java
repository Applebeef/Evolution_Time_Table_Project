package evolution.engine.problem_solution;

import evolution.configuration.Crossover;

public interface Solution extends Comparable<Solution> {
    double calculateFitness();
    void mutate();
    Solution crossover(Solution solution, Crossover crossover);
}
