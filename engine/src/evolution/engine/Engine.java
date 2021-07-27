package evolution.engine;

import evolution.engine.problem_solution.Problem;
import evolution.engine.problem_solution.Solution;
import java.util.List;

public class Engine {
    List<Solution> solutionList;
    int initialPopulation;
    public Engine(Problem problem, int initialPopulation) {
        this.initialPopulation = initialPopulation;
        for (int i = 0; i < initialPopulation; i++) {
            solutionList.add(problem.Solve());
        }

    }
}
