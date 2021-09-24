package evolutionaryApp.utils.resultUtils;

public class PullData {
    Integer generation;
    Double fitness;
    Boolean isAlive;

    public PullData(Integer generation, Double fitness, Boolean isAlive) {
        this.generation = generation;
        this.fitness = fitness;
        this.isAlive = isAlive;
    }
}
