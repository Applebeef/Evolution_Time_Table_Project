package evolution.engine.problem_solution;

import evolution.configuration.CrossoverIFC;
import evolution.configuration.MutationsIFC;
import evolution.configuration.SelectionIFC;

import java.util.List;

public interface Problem {
    Solution solve();
    <M extends MutationsIFC> M getMutations();
    <S extends SelectionIFC> List<S> getSelectionsList();
    <C extends CrossoverIFC> List<C> getCrossoverList();
}
