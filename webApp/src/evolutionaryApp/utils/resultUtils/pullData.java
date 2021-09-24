package evolutionaryApp.utils.resultUtils;

public class pullData {
    Integer generation;
    Double fitness;
    Boolean isAlive;

    public pullData(Integer generation, Double fitness, Boolean isAlive) {
        this.generation = generation;
        this.fitness = fitness;
        this.isAlive = isAlive;
    }
}
