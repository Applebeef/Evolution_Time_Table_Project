package evolution.engine.problem_solution;

import evolution.configuration.Crossover;

import java.util.List;

public interface Solution extends Comparable<Solution> {
    double calculateFitness();
    void mutate();
    List<Solution> crossover(Solution solution, Crossover crossover);
}
