package evolution.configuration;

import evolution.engine.problem_solution.Solution;

import java.util.List;

public interface SelectionIFC {
    int getElitism();

    List<Solution> select(List<Solution> solutionList);

    String checkElitismValidity(int size);
}

