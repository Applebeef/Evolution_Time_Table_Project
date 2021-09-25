package evolutionaryApp.utils.resultUtils;

public class PullData {
    Integer generation;
    Integer bestGeneration;
    Double fitness;
    Boolean isAlive;

    public PullData(Integer generation, Double fitness, Integer bestGeneration, Boolean isAlive) {
        this.generation = generation;
        this.fitness = fitness;
        this.isAlive = isAlive;
        this.bestGeneration = bestGeneration;
    }
}
