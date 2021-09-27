package evolution.engine.problem_solution;

import evolution.configuration.CrossoverIFC;
import evolution.configuration.MutationsIFC;
import evolution.configuration.SelectionIFC;

import java.util.List;

public interface Problem {
    Solution solve();
}
