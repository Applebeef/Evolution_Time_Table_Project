package evolution.engine.problem_solution;

import evolution.configuration.Crossover;
import evolution.configuration.Mutations;

import java.util.List;

public interface Solution extends Comparable<Solution> {
    // Calculate and return fitness for solution:
    double calculateFitness();
    // Mutate solution:
    void mutate(Mutations mutations);
    // Crossover with other solution:
    List<Solution> crossover(Solution solution, Crossover crossover);
    // Set presentation option:
    void setPresentationOption(int presentationOption);
}
