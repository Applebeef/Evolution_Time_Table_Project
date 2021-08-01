package evolution.engine.problem_solution;

import evolution.configuration.Crossover;
import evolution.configuration.Mutations;

import java.util.List;

public interface Solution extends Comparable<Solution> {
    double calculateFitness();
    void mutate(Mutations mutations);
    List<Solution> crossover(Solution solution, Crossover crossover);
}
