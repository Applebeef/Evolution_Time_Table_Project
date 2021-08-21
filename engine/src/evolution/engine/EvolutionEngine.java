package evolution.engine;

import evolution.configuration.*;
import Generated.ETTEvolutionEngine;
import evolution.engine.problem_solution.Problem;
import evolution.engine.problem_solution.Solution;
import evolution.util.Pair;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;


import java.util.*;
import java.util.function.Consumer;

public class EvolutionEngine implements Runnable {
    private InitialPopulation initialSolutionPopulation;
    private MutationsIFC mutations;
    private SelectionIFC selection;
    private CrossoverIFC crossover;

    private List<Solution> solutionList;
    private List<Solution> offspringSolutionsList;
    private List<Pair<Integer, Solution>> bestSolutionsPerFrequency;
    private Pair<Integer, Solution> bestSolution;

    private BooleanProperty engineStarted;
    private BooleanProperty enginePaused;
    private BooleanProperty solutionsReady;
    private Integer number_of_generations;

    private int frequency;
    private int max_fitness;
    private Consumer<String> consumer;

    public EvolutionEngine(ETTEvolutionEngine gen) {
        initialSolutionPopulation = new InitialPopulation(gen.getETTInitialPopulation());

        solutionList = new ArrayList<>(initialSolutionPopulation.getSize());
        bestSolutionsPerFrequency = new ArrayList<>();
        engineStarted = new SimpleBooleanProperty(false);
        enginePaused = new SimpleBooleanProperty(true);
        solutionsReady = new SimpleBooleanProperty(false);
    }

    public boolean isSolutionsReady() {
        return solutionsReady.get();
    }

    public BooleanProperty solutionsReadyProperty() {
        return solutionsReady;
    }

    public void setSolutionsReady(boolean solutionsReady) {
        this.solutionsReady.set(solutionsReady);
    }

    public BooleanProperty engineStartedProperty() {
        return engineStarted;
    }

    public boolean isEnginePaused() {
        return enginePaused.get();
    }

    public BooleanProperty enginePausedProperty() {
        return enginePaused;
    }

    public void setEnginePaused(boolean enginePaused) {
        this.enginePaused.set(enginePaused);
    }

    public InitialPopulation getInitialSolutionPopulation() {
        return initialSolutionPopulation;
    }

    public MutationsIFC getMutations() {
        return mutations;
    }

    public SelectionIFC getSelection() {
        return selection;
    }

    public CrossoverIFC getCrossover() {
        return crossover;
    }

    public List<Solution> getSolutionList() {
        return solutionList;
    }

    public boolean isEngineStarted() {
        return engineStarted.get();
    }

    public void initSolutionPopulation(Problem problem, Integer number_of_generations) {
        Solution solution;
        this.number_of_generations = number_of_generations;
        mutations = problem.getMutations();
        selection = problem.getSelection();
        crossover = problem.getCrossover();

        for (int i = 0; i < initialSolutionPopulation.getSize(); i++) {
            // Create solution:
            solution = problem.solve();
            // Add to solutionList:
            solutionList.add(solution);
        }
        // Sort list:
        solutionList.sort(Collections.reverseOrder());
        bestSolution = new Pair<>(0, solutionList.get(0));
        engineStarted.set(true);
        enginePaused.set(false);
        solutionsReady.set(true);
    }

    public void runEvolution() {
        // Main loop: #iterations = number of generations
        //Stop the loop if we reach the desired amount of generations or reach max fitness.
        for (int i = 1; i <= number_of_generations && solutionList.get(0).getFitness() < max_fitness && !Thread.currentThread().isInterrupted(); i++) {
            // Spawn new generation:
            spawnGeneration();
            // Mutate each solution (includes calculate fitness):
            for (Solution solution : solutionList) {
                mutations.getMutationList().forEach(mutationIFC -> mutationIFC.mutate(solution));
            }
            // Sort by fitness (highest to lowest):
            solutionList.sort(Collections.reverseOrder());
            // Handle generation by frequency:
            if (i % frequency == 0 || i == 1) {
                synchronized (bestSolutionsPerFrequency) {
                    Solution solution = solutionList.get(0);
                    bestSolutionsPerFrequency.add(new Pair<>(i, solution));
                }
            }
            if (solutionList.get(0).getFitness() > bestSolution.getV2().getFitness()) {
                synchronized (bestSolution) {
                    bestSolution.setV1(i);
                    bestSolution.setV2(solutionList.get(0));
                }
            }
            while (enginePaused.get()) {//TODO make sure this works.
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    break;
                }
            }
        }
        consumer.accept("Engine is finished.");
        //engineStarted.set(false);
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
        List<Solution> eliteList = solutionList.subList(0, this.selection.getElitism());
        List<Solution> copiedEliteList = new ArrayList<>(eliteList.size());
        eliteList.forEach(solution -> copiedEliteList.add(solution.copy()));
        offspringSolutionsList.addAll(copiedEliteList);

        // Using crossover (class EvolutionEngine), create an offspring Solution List:
        while (offspringSolutionsList.size() < initialSolutionPopulation.getSize()) {
            // Receive the solutions to be crossed over:
            selectedSolutions = this.selection.select(solutionList);
            // Crossover the two solutions:
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
        return "Initial population - " + initialSolutionPopulation + lineSeparator + lineSeparator;
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
        this.engineStarted.set(engineStarted);
    }

    public void reset() {
        solutionList.clear();
        bestSolutionsPerFrequency.clear();
        offspringSolutionsList.clear();
        engineStarted.set(false);
        solutionsReady.set(false);
    }
}
