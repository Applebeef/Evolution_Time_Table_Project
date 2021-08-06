package evolution.engine;

import evolution.configuration.Crossover;
import evolution.configuration.InitialPopulation;
import evolution.configuration.Mutations;
import evolution.configuration.Selection;
import Generated.ETTEvolutionEngine;
import evolution.engine.problem_solution.Problem;
import evolution.engine.problem_solution.Solution;
import evolution.util.Pair;
import evolution.util.Randomizer;

import java.util.*;
import java.util.function.Consumer;

public class EvolutionEngine implements Runnable {
    private InitialPopulation initialSolutionPopulation;
    private Mutations mutations;
    private Selection selection;
    private Crossover crossover;

    private List<Solution> solutionList;
    private List<Solution> offspringSolutionsList;
    private List<Pair<Integer, Solution>> bestSolutions;

    private boolean engineStarted = false;
    private Integer number_of_generations;

    private int frequency;
    private int max_fitness;
    private Consumer<String> consumer;
    static int debug = 1;

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
            // Create solution:
            solution = problem.solve();
            // Calculate solution fitness:
            solution.calculateFitness();
            // Add to solutionList:
            solutionList.add(solution);
        }
        // Sort list:
        solutionList.sort(Collections.reverseOrder());
        engineStarted = true;
    }

    public void runEvolution() {
        // Main loop: #iterations = number of generations
        //Stop the loop if we reach the desired amount of generations or reach max fitness.
        for (int i = 1; i <= number_of_generations && solutionList.get(0).getFitness() < max_fitness && engineStarted; i++) {
            // Spawn new generation:
            spawnGeneration();
            // Mutate each solution (includes calculate fitness):
            solutionList.forEach(solution -> solution.mutate(mutations));
            // Sort by fitness (highest to lowest):
            solutionList.sort(Collections.reverseOrder());
            // Handle generation by frequency:
            synchronized (bestSolutions) {
                if (i % frequency == 0 || i == 1) {
                    Solution solution = solutionList.get(0);
                    //consumer.accept("Generation " + i + " " + String.format("%.1f", solution.getFitness()));
                    bestSolutions.add(new Pair<>(i, solution));
                }
            }
            if (Thread.currentThread().isInterrupted()) {
                return;
            }
        }
    }

    public String getBestSolutionDisplay(int choice) {
        // Sort bestSolutions by fitness:
        Pair<Integer, Solution> var;
        synchronized (bestSolutions) {
            var = bestSolutions.stream().max(Comparator.comparing(o -> o.getV2().getFitness())).get();
        }
        // Set presentation option to choice. Note: might throw outOfBound exception:
        var.getV2().setPresentationOption(choice - 1);
        return "Generation: " + var.getV1() +
                ", fitness: " + String.format("%.1f", var.getV2().getFitness()) +
                System.getProperty("line.separator") +
                var.getV2().toString();
    }

    private void spawnGeneration() {
        int bestSolutionsAmount;
        List<Solution> bestSolutionsList = new ArrayList<>();
        this.offspringSolutionsList = new ArrayList<>();
        // bestSolutionsAmount = the amount of the X% best solution (X is given)
        bestSolutionsAmount = (int) Math.floor(solutionList.size() * ((double) selection.getTopPercent() / 100));
        if (bestSolutionsAmount > 0) {
            // Get best solutions into "bestSolutionsList":
            bestSolutionsList = solutionList.subList(0, bestSolutionsAmount);
        }
        // Using crossover (class EvolutionEngine), create an offspring Solution List:
        while (offspringSolutionsList.size() < initialSolutionPopulation.getSize()) {
            Solution s1 = bestSolutionsList.get(Randomizer.getRandomNumber(0, bestSolutionsAmount - 1));
            Solution s2 = bestSolutionsList.get(Randomizer.getRandomNumber(0, bestSolutionsAmount - 1));
            offspringSolutionsList.addAll(s1.crossover(s2, this.crossover));
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

    public List<Pair<Integer, Solution>> getBestSolutions() {
        return bestSolutions;
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
        bestSolutions.clear();
        offspringSolutionsList.clear();
        engineStarted = false;
    }
}
