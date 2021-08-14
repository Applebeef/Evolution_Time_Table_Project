package evolution.engine;

import evolution.configuration.Crossover;
import evolution.configuration.InitialPopulation;
import evolution.configuration.Mutations;
import evolution.configuration.Selection;
import Generated.ETTEvolutionEngine;
import evolution.engine.problem_solution.Problem;
import evolution.engine.problem_solution.Solution;
import evolution.util.Pair;


import java.util.*;
import java.util.function.Consumer;

public class EvolutionEngine implements Runnable {
    private InitialPopulation initialSolutionPopulation;
    private Mutations mutations;
    private Selection selection;
    private Crossover crossover;

    private List<Solution> solutionList;
    private List<Solution> offspringSolutionsList;
    private List<Pair<Integer, Solution>> bestSolutionsPerFrequency;
    private Pair<Integer, Solution> bestSolution;

    private boolean engineStarted = false;
    private Integer number_of_generations;

    private int frequency;
    private int max_fitness;
    private Consumer<String> consumer;

    public EvolutionEngine(ETTEvolutionEngine gen) {
        initialSolutionPopulation = new InitialPopulation(gen.getETTInitialPopulation());
        mutations = new Mutations(gen.getETTMutations());
        selection = new Selection(gen.getETTSelection());
        crossover = new Crossover(gen.getETTCrossover());

        solutionList = new ArrayList<>(initialSolutionPopulation.getSize());
        bestSolutionsPerFrequency = new ArrayList<>();
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
            // Create solution:
            solution = problem.solve();
            // Calculate solution fitness:
            solution.calculateFitness();
            // Add to solutionList:
            solutionList.add(solution);
        }
        // Sort list:
        solutionList.sort(Collections.reverseOrder());
        bestSolution = new Pair<>(0, solutionList.get(0));
        engineStarted = true;
    }

    public void runEvolution() {
        // Main loop: #iterations = number of generations
        //Stop the loop if we reach the desired amount of generations or reach max fitness.
        for (int i = 1; i <= number_of_generations && solutionList.get(0).getFitness() < max_fitness && !Thread.currentThread().isInterrupted(); i++) {
            // Spawn new generation:
            spawnGeneration();
            // Mutate each solution (includes calculate fitness):
            solutionList.forEach(solution -> solution.mutate(mutations));
            // Sort by fitness (highest to lowest):
            solutionList.sort(Collections.reverseOrder());
            // Handle generation by frequency:
            synchronized (bestSolutionsPerFrequency) {
                if (i % frequency == 0 || i == 1) {
                    Solution solution = solutionList.get(0);
                    //consumer.accept("Generation " + i + " " + String.format("%.1f", solution.getFitness()));
                    bestSolutionsPerFrequency.add(new Pair<>(i, solution));
                }
            }
            if (solutionList.get(0).getFitness() > bestSolution.getV2().getFitness()) {
                synchronized (bestSolution) {
                    bestSolution = new Pair<>(i, solutionList.get(0));
                }
            }
        }
        consumer.accept("Engine is finished.");
    }

    public String getBestSolutionDisplay(int choice) {
        String lineSeparator = System.getProperty("line.separator");

        // Sort bestSolutions by fitness:
        synchronized (bestSolution) {
            bestSolution.getV2().setPresentationOption(choice - 1);
            return "Displaying best solution - * represents duplicates for the timeframe." + lineSeparator +
                    "Generation: " + bestSolution.getV1() +
                    ", fitness: " + String.format("%.1f", bestSolution.getV2().getFitness()) +
                    System.getProperty("line.separator") +
                    bestSolution.getV2().toString();
        }
    }

    private void spawnGeneration() {
        this.offspringSolutionsList = new ArrayList<>();
        List<Solution> selectedSolutions;
        // Elitism:
        offspringSolutionsList.addAll(solutionList.subList(0, this.selection.getElitism()));

        // Using crossover (class EvolutionEngine), create an offspring Solution List:
        while (offspringSolutionsList.size() < initialSolutionPopulation.getSize()) {
            // Receive the solutions to be crossed over:
            selectedSolutions = this.selection.select(solutionList);
            // Crossover the two solutions:
            // TODO: fix fifths cutting points - add arithmetics to recognize position between cutting points.
            // TODO: Think of crossover method implementation (inside Crossover or inside Solution instantiation).

            offspringSolutionsList.addAll(
                    selectedSolutions.get(0)
                    .crossover(selectedSolutions.get(1), this.crossover)
            );
        }
        // Shrink to initial population size:
        if (offspringSolutionsList.size() != initialSolutionPopulation.getSize()) {
            this.solutionList = offspringSolutionsList.subList(0, initialSolutionPopulation.getSize());
        } else {
            this.solutionList = offspringSolutionsList;
        }
    }


    @Override
    public String toString() {
        String lineSeparator = System.getProperty("line.separator");
        return "Initial population - " + initialSolutionPopulation + lineSeparator + lineSeparator +
                "Mutations - " + lineSeparator + mutations + lineSeparator +
                "Selection - " + selection + lineSeparator + lineSeparator +
                "Crossover - " + lineSeparator + crossover;
    }

    public List<Pair<Integer, Solution>> getBestSolutionsPerFrequency() {
        return bestSolutionsPerFrequency;
    }

    @Override
    public void run() {
        runEvolution();
    }

    public void initThreadParameters(int frequency, int max_fitness, Consumer<String> consumer) {
        this.frequency = frequency;
        this.max_fitness = max_fitness;
        this.consumer = consumer;
    }

    public void setEngineStarted(boolean engineStarted) {
        this.engineStarted = engineStarted;
    }

    public void reset() {
        solutionList.clear();
        bestSolutionsPerFrequency.clear();
        offspringSolutionsList.clear();
        engineStarted = false;
    }
}
