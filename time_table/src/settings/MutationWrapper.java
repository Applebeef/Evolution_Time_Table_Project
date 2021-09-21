package settings;

import evolution.configuration.MutationIFC;
import evolution.engine.problem_solution.Solution;
import javafx.beans.property.*;

public class MutationWrapper implements MutationIFC {
    private final Mutation mutation;
    private DoubleProperty probability;
    private IntegerProperty tupples;
    private StringProperty component;

    public MutationWrapper(Mutation mutation, Double probability, Integer tupples, String component) {
        this.mutation = mutation;
        this.probability = new SimpleDoubleProperty(probability);
        this.tupples = tupples != null ? new SimpleIntegerProperty(tupples) : null;
        this.component = component != null ? new SimpleStringProperty(component) : null;
    }

    @Override
    public String getName() {
        return mutation.getName();
    }

    public Double getProbability() {
        return this.probability != null ? probability.get() : null;
    }

    public DoubleProperty probabilityProperty() {
        return probability;
    }

    public void setProbability(double probability) {
        this.probability.set(probability);
    }

    public Integer getTupples() {
        return this.tupples != null ? tupples.get() : null;
    }

    @Override
    public <T extends Solution> void mutate(T solution) {
        mutation.mutate(solution,
                getComponent(),
                getProbability(),
                getTupples());
    }

    public IntegerProperty tupplesProperty() {
        return tupples;
    }

    public void setTupples(int tupples) {
        this.tupples.set(tupples);
    }

    public String getComponent() {
        return this.component != null ? component.get() : null;
    }

    public StringProperty componentProperty() {
        return component;
    }

    public void setComponent(String component) {
        this.component.set(component);
    }


}
