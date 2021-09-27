package evolution.engine;

import evolution.configuration.*;
import evolution.engine.problem_solution.Problem;
import evolution.engine.problem_solution.Solution;
import evolution.util.Pair;
import javafx.beans.property.*;


import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

public class EvolutionEngine extends Thread {
    private InitialPopulation initialSolutionPopulation;

    private List<MutationIFC> mutations;
    private List<SelectionIFC> selectionIFCList;
    private List<CrossoverIFC> crossoverIFCList;

    private List<Solution> solutionList;
    private List<Solution> offspringSolutionsList;
    private Map<Integer, Solution> bestSolutionsPerFrequency;
    private Pair<Integer, Solution> bestSolution;

    private DoubleProperty bestSolutionFitness;
    private IntegerProperty currentGenerationProperty;

    private BooleanProperty engineStarted;
    private BooleanProperty enginePaused;
    private BooleanProperty solutionsReady;
    private BooleanProperty newBestSolution;

    private int frequency;
    private EndingConditions endingConditions;
    private Instant startTime;
    private LongProperty currentTime;

    Instant pauseStart;


    public EvolutionEngine(List<SelectionIFC> selectionIFCList, List<CrossoverIFC> crossoverIFCList, List<MutationIFC> mutationIFCList, Integer initialPopulation) {
        this.selectionIFCList = selectionIFCList;
        this.crossoverIFCList = crossoverIFCList;
        this.mutations = mutationIFCList;
        this.initialSolutionPopulation = new InitialPopulation(initialPopulation);

        solutionList = new ArrayList<>(initialSolutionPopulation.getSize());
        bestSolutionsPerFrequency = new HashMap<>();
        engineStarted = new SimpleBooleanProperty(false);
        enginePaused = new SimpleBooleanProperty(true);
        solutionsReady = new SimpleBooleanProperty(false);
        newBestSolution = new SimpleBooleanProperty(false);
        bestSolutionFitness = new SimpleDoubleProperty(0);
        currentGenerationProperty = new SimpleIntegerProperty(0);
        currentTime = new SimpleLongProperty(0);
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

    public synchronized boolean isEnginePaused() {
        return enginePaused.get();
    }

    public BooleanProperty enginePausedProperty() {
        return enginePaused;
    }

    public synchronized void setEnginePaused(boolean enginePaused) {
        this.enginePaused.set(enginePaused);
    }

    public InitialPopulation getInitialSolutionPopulation() {
        return initialSolutionPopulation;
    }

    public List<MutationIFC> getMutationIFCList() {
        return mutations;
    }

    public List<SelectionIFC> getSelectionIFCList() {
        return selectionIFCList;
    }

    public List<CrossoverIFC> getCrossoverIFCList() {
        return crossoverIFCList;
    }

    public synchronized List<Solution> getSolutionList() {
        return solutionList;
    }

    public boolean isEngineStarted() {
        return engineStarted.get();
    }


    public void initSolutionPopulation(Problem problem) {
        Solution solution;
        solutionList = new ArrayList<>();

        for (int i = 0; i < initialSolutionPopulation.getSize(); i++) {
            // Create solution:
            solution = problem.solve();
            // Add to solutionList:
            solutionList.add(solution);
        }
        // Sort list:
        currentGenerationProperty().set(0);
        solutionList.sort(Collections.reverseOrder());
        bestSolutionsPerFrequency.put(0, solutionList.get(0));
        bestSolution = new Pair<>(0, solutionList.get(0));
        newBestSolution.set(true);
        newBestSolution.set(false);
        engineStarted.set(true);
        solutionsReady.set(true);
        enginePaused.set(false);
    }

    public void runEvolution() {
        // Main loop: #iterations = number of generations
        //Stop the loop if we reach the desired amount of generations or reach max fitness.
        startTime = Instant.now();
        int lastGeneration = 0;
        long timeOffset = 0;
        currentGenerationProperty.set(lastGeneration);
        for (int i = 1; !endingConditions.test(i, getBestSolutionFitness(), ChronoUnit.SECONDS.between(startTime, Instant.now())) && !isInterrupted(); i++) {
            updateCurrentTime();
            // Spawn new generation:
            spawnGeneration();
            // Mutate each solution (includes calculate fitness):
            for (Solution solution : getSolutionList()) {
                mutations.forEach(mutationIFC -> mutationIFC.mutate(solution));
            }
            // Sort by fitness (highest to lowest):
            solutionList.sort(Collections.reverseOrder());
            // Handle generation by frequency:
            currentGenerationProperty.set(i);
            if (i % frequency == 0) {
                synchronized (bestSolutionsPerFrequency) {
                    Solution solution = solutionList.get(0);
                    bestSolutionsPerFrequency.put(i, solution);
                }
            }
            synchronized (bestSolution) {
                if (solutionList.get(0).getFitness() > bestSolution.getV2().getFitness()) {
                    bestSolution.setV1(i);
                    bestSolution.setV2(solutionList.get(0));
                    bestSolutionFitness.set(solutionList.get(0).getFitness());
                    newBestSolution.set(true);
                    newBestSolution.set(false);
                }
            }
            lastGeneration = i;
            if (isEnginePaused()) {
                try {
                    synchronized (this) {
                        this.pauseStart = Instant.now();
                        wait();
                        timeOffset = ChronoUnit.SECONDS.between(pauseStart, Instant.now());
                    }
                    addToMaxTime(timeOffset);
                } catch (InterruptedException e) {
                    break;
                }
            }
        }
        currentGenerationProperty.set(lastGeneration);
        engineStarted.set(false);
        setEnginePaused(true);
    }

    private void updateCurrentTime() {
        //if (currentTime.get() + 5 <= getCurrentTimeLive()) {
        currentTime.set(getCurrentTimeLive());
        //}
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
        SelectionIFC activeSelection = null;
        CrossoverIFC activeCrossover = null;
        for (SelectionIFC selection : getSelectionIFCList()) {
            if (selection.isActive()) {
                activeSelection = selection;
                break;
            }
        }
        for (CrossoverIFC crossover : getCrossoverIFCList()) {
            if (crossover.isActive()) {
                activeCrossover = crossover;
                break;
            }
        }
        if (activeSelection != null && activeCrossover != null) {
            List<Solution> selectedSolutions;
            // Elitism:
            List<Solution> eliteList = solutionList.subList(0, activeSelection.getElitism());
            List<Solution> copiedEliteList = new ArrayList<>(eliteList.size());
            eliteList.forEach(solution -> copiedEliteList.add(solution.copy()));
            offspringSolutionsList.addAll(copiedEliteList);

            // Using crossover (class EvolutionEngine), create an offspring Solution List:
            while (offspringSolutionsList.size() < initialSolutionPopulation.getSize()) {
                // Receive the solutions to be crossed over:
                selectedSolutions = activeSelection.select(solutionList);
                // Crossover the two solutions:
                offspringSolutionsList.addAll(activeCrossover.cross(selectedSolutions.get(0), selectedSolutions.get(1)));
                /**/
            }
            // Shrink to initial population size:
            if (offspringSolutionsList.size() != initialSolutionPopulation.getSize()) {
                this.solutionList = offspringSolutionsList.subList(0, initialSolutionPopulation.getSize());
            } else {
                setSolutionList(offspringSolutionsList);
                //this.solutionList = offspringSolutionsList;
            }
        }

    }

    public synchronized void setSolutionList(List<Solution> solutionList) {
        this.solutionList.clear();
        this.solutionList = solutionList;
    }

    @Override
    public String toString() {
        String lineSeparator = System.getProperty("line.separator");
        return "Initial population - " + initialSolutionPopulation + lineSeparator + lineSeparator;
    }

    public Map<Integer, Solution> getBestSolutionsPerFrequency() {
        return bestSolutionsPerFrequency;
    }

    @Override
    public void run() {
        runEvolution();
    }

    public void initThreadParameters(int frequency, int number_of_generations, double max_fitness, long max_time) {
        this.frequency = frequency;
        this.endingConditions = new EndingConditions(number_of_generations, max_fitness, max_time);
    }

    public void setEngineStarted(boolean engineStarted) {
        this.engineStarted.set(engineStarted);
    }

    public void reset() {
        if (isSolutionsReady()) {
            solutionList.clear();
            bestSolutionsPerFrequency.clear();
            offspringSolutionsList.clear();
        }
        engineStarted.set(false);
        solutionsReady.set(false);
    }

    public double getBestSolutionFitness() {
        return bestSolutionFitness.get();
    }

    public DoubleProperty bestSolutionFitnessProperty() {
        return bestSolutionFitness;
    }

    public int getCurrentGenerationProperty() {
        return currentGenerationProperty.get();
    }

    public IntegerProperty currentGenerationProperty() {
        return currentGenerationProperty;
    }

    public EndingConditions getEndingConditions() {
        return endingConditions;
    }

    public void addToMaxTime(long timeOffset) {
        if (endingConditions.getEndingConditionWrapper(EndingCondition.TIME).getMax().longValue() != 0) {
            endingConditions
                    .getEndingConditionWrapper(EndingCondition.TIME)
                    .setMax(endingConditions
                            .getEndingConditionWrapper(EndingCondition.TIME)
                            .getMax().longValue() + timeOffset);
        }
    }

    public long getCurrentTimeLive() {
        return ChronoUnit.SECONDS.between(startTime, Instant.now());
    }

    public Integer getnumberOfGenerations() {
        return (Integer) this.endingConditions.getEndingConditionWrapper(EndingCondition.GENERATIONS).getMax();
    }

    public LongProperty currentTimeProperty() {
        return currentTime;
    }

    public void setCurrentTime(long currentTime) {
        this.currentTime.set(currentTime);
    }

    public long getCurrentTime() {
        return currentTime.get();
    }

    public long getMaxTime() {
        return this.endingConditions.getEndingConditionWrapper(EndingCondition.TIME).getMax().longValue();
    }

    public double getMaxFitness() {
        return endingConditions.getEndingConditionWrapper(EndingCondition.FITNESS).getMax().doubleValue();
    }

    public synchronized Pair<Integer, Solution> getBestSolution() {
        return bestSolution;
    }

    public boolean isNewBestSolution() {
        return newBestSolution.get();
    }

    public BooleanProperty newBestSolutionProperty() {
        return newBestSolution;
    }

    public void setNewBestSolution(boolean newBestSolution) {
        this.newBestSolution.set(newBestSolution);
    }

    public synchronized void pauseOrResumeEngine() {
        if (isEnginePaused()) {
            notify();
        }
        setEnginePaused(!this.enginePaused.get());
    }

    public int getFrequency() {
        return frequency;
    }
}
