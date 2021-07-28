package evolution.engine;

import evolution.configuration.Crossover;
import evolution.configuration.InitialPopulation;
import evolution.configuration.Mutations;
import evolution.configuration.Selection;
import Generated.ETTEvolutionEngine;
import evolution.engine.problem_solution.Problem;
import evolution.engine.problem_solution.Solution;

import java.util.ArrayList;
import java.util.List;

public class EvolutionEngine {
    private InitialPopulation initialPopulation;
    private Mutations mutations;
    private Selection selection;
    private Crossover crossover;
    private List<Solution> solutionList;
    private boolean engineStarted = false;

    public EvolutionEngine(ETTEvolutionEngine gen) {
        initialPopulation = new InitialPopulation(gen.getETTInitialPopulation());
        mutations = new Mutations(gen.getETTMutations());
        selection = new Selection(gen.getETTSelection());
        crossover = new Crossover(gen.getETTCrossover());
    }

    public InitialPopulation getInitialPopulation() {
        return initialPopulation;
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

    @Override
    public String toString() {
        String lineSeparator = System.getProperty("line.separator");
        return  "Initial population - " + initialPopulation + lineSeparator + lineSeparator +
                "Mutations - " + lineSeparator + mutations + lineSeparator +
                "Selection - " + selection + lineSeparator + lineSeparator +
                "Crossover - " + lineSeparator + crossover;
    }

    public void initializePopulation(Problem problem){
        solutionList = new ArrayList<>(initialPopulation.getSize());
        for(int i=0;i<initialPopulation.getSize();i++){
            solutionList.add(problem.solve());
        }
        engineStarted = true;
    }

    public void runEvolution(){

    }

    public List<Solution> getSolutionList() {
        return solutionList;
    }

    public boolean isEngineStarted() {
        return engineStarted;
    }
}
