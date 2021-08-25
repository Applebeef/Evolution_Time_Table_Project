package evolution.configuration;

import evolution.engine.problem_solution.Solution;
import evolution.rules.Type;

import java.util.List;

public interface SelectionIFC {
    int getElitism();

    String getType();

    List<Solution> select(List<Solution> solutionList);

    boolean isActive();

    String checkElitismValidity(int size);
}

