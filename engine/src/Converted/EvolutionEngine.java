package Converted;

import Generated.ETTEvolutionEngine;

public class EvolutionEngine {
    InitialPopulation initialPopulation;
    Mutations mutations;
    Selection selection;
    Crossover crossover;

    public EvolutionEngine(ETTEvolutionEngine gen){
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
}
