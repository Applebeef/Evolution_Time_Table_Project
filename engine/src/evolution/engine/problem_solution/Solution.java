package evolution.engine.problem_solution;


import evolution.configuration.CrossoverIFC;
import evolution.configuration.MutationsIFC;

import java.util.List;

public interface Solution extends Comparable<Solution> {
    // Calculate and return fitness for solution:
    double calculateFitness();

    // Mutate solution:
    void mutate(MutationsIFC mutations);

    // Crossover with other solution:
    List<Solution> crossover(Solution solution, CrossoverIFC crossover);

    // Set presentation option:
    void setPresentationOption(int requested_presentation_option);

    Double getFitness();

    Solution copy();
}
