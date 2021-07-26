package evolution.engine;

import evolution.configuration.Crossover;
import evolution.configuration.InitialPopulation;
import evolution.configuration.Mutations;
import evolution.configuration.Selection;
import Generated.ETTEvolutionEngine;

public class EvolutionEngine {
    private InitialPopulation initialPopulation;
    private Mutations mutations;
    private Selection selection;
    private Crossover crossover;

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
        return  "initial population - " + initialPopulation + lineSeparator +
                "mutations - " + mutations + lineSeparator +
                "selection - " + selection + lineSeparator +
                "crossover - " + crossover + lineSeparator;
    }
}
