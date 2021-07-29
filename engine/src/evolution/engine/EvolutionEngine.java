package evolution.engine;

import evolution.configuration.Crossover;
import evolution.configuration.InitialPopulation;
import evolution.configuration.Mutations;
import evolution.configuration.Selection;
import Generated.ETTEvolutionEngine;
import evolution.engine.problem_solution.Problem;
import evolution.engine.problem_solution.Solution;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EvolutionEngine {
    private InitialPopulation initialSolutionPopulation;
    private Mutations mutations;
    private Selection selection;
    private Crossover crossover;
    private List<Solution> solutionList;
    private boolean engineStarted = false;
    private Integer number_of_generations;

    public EvolutionEngine(ETTEvolutionEngine gen) {
        initialSolutionPopulation = new InitialPopulation(gen.getETTInitialPopulation());
        mutations = new Mutations(gen.getETTMutations());
        selection = new Selection(gen.getETTSelection());
        crossover = new Crossover(gen.getETTCrossover());

        solutionList = new ArrayList<>(initialSolutionPopulation.getSize());
    }

    public InitialPopulation getInitialSolutionPopulation() {
        return initialSolutionPopulation;
    }

    public Mutations getMutations() {
        return mutations;
    }

    public Selection getSelection() {
        return selection;
    }

    public Crossover getCrossover() {
        return crossover;
    }

    public List<Solution> getSolutionList() {
        return solutionList;
    }

    public boolean isEngineStarted() {
        return engineStarted;
    }

    public void initSolutionPopulation(Problem problem, Integer number_of_generations) {
        double fitness;
        Solution solution;
        this.number_of_generations = number_of_generations;

        for (int i = 0; i < initialSolutionPopulation.getSize(); i++) {
            solution = problem.solve();
            solution.calculateFitness();
            solutionList.add(solution);
        }
        Collections.sort(solutionList);
        engineStarted = true;
    }

    public void runEvolution() {

    }

    @Override
    public String toString() {
        String lineSeparator = System.getProperty("line.separator");
        return "Initial population - " + initialSolutionPopulation + lineSeparator + lineSeparator +
                "Mutations - " + lineSeparator + mutations + lineSeparator +
                "Selection - " + selection + lineSeparator + lineSeparator +
                "Crossover - " + lineSeparator + crossover;
    }
}
