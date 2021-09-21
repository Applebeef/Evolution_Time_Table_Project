
package evolutionaryApp.utils.engineDataUtils;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class EndingConditions {

    @SerializedName("generations")
    @Expose
    private Integer generations;
    @SerializedName("fitness")
    @Expose
    private Integer fitness;
    @SerializedName("time")
    @Expose
    private Integer time;

    public Integer getGenerations() {
        return generations;
    }

    public void setGenerations(Integer generations) {
        this.generations = generations;
    }

    public Integer getFitness() {
        return fitness;
    }

    public void setFitness(Integer fitness) {
        this.fitness = fitness;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

}
