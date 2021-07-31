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
import java.util.Random;
import java.util.stream.Collectors;

public class EvolutionEngine {
    private InitialPopulation initialSolutionPopulation;
    private Mutations mutations;
    private Selection selection;
    private Crossover crossover;

    private List<Solution> solutionList;
    private List<Solution> tempSolutionList;
    private List<Solution> bestSolutions;

    private boolean engineStarted = false;
    private Integer number_of_generations;

    public EvolutionEngine(ETTEvolutionEngine gen) {
        initialSolutionPopulation = new InitialPopulation(gen.getETTInitialPopulation());
        mutations = new Mutations(gen.getETTMutations());
        selection = new Selection(gen.getETTSelection());
        crossover = new Crossover(gen.getETTCrossover());

        solutionList = new ArrayList<>(initialSolutionPopulation.getSize());
        bestSolutions = new ArrayList<>();
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
        Solution solution;
        this.number_of_generations = number_of_generations;

        for (int i = 0; i < initialSolutionPopulation.getSize(); i++) {
            solution = problem.solve();
            solution.calculateFitness();
            solutionList.add(solution);
        }
        solutionList.sort(Collections.reverseOrder());
        engineStarted = true;
    }

    public void runEvolution(int frequency) {
        int topAmount;
        for (int i = 0; i < number_of_generations; i++) {
            tempSolutionList = new ArrayList<>();
            topAmount = (int) Math.floor(solutionList.size() * ((double) selection.getTopPercent() / 100));
            List<Solution> selectionList = solutionList.stream().limit(topAmount).collect(Collectors.toList());

            while (tempSolutionList.size() < initialSolutionPopulation.getSize()) {
                tempSolutionList.addAll(selectionList.get(getRandomNumber(0, topAmount)).crossover(selectionList.get(getRandomNumber(0, topAmount)), crossover));
            }
            if (tempSolutionList.size() != initialSolutionPopulation.getSize()) {
                solutionList = tempSolutionList.subList(0, initialSolutionPopulation.getSize());
            } else {
                solutionList = tempSolutionList;
            }

            solutionList.sort(Collections.reverseOrder());
            if (i % frequency == 0) {
                System.out.println(solutionList.get(0));
                bestSolutions.add(solutionList.get(0));
            }

            solutionList.forEach(Solution::mutate);
        }
    }

    private int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
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
