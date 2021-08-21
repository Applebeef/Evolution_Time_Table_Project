package evolution.engine.problem_solution;

import evolution.configuration.CrossoverIFC;
import evolution.configuration.MutationsIFC;
import evolution.configuration.SelectionIFC;

public interface Problem {
    Solution solve();
    <M extends MutationsIFC> M getMutations();
    <S extends SelectionIFC> S getSelection();
    <C extends CrossoverIFC> C getCrossover();
}
