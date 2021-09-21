
package evolutionaryApp.utils.engineDataUtils;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class EndingConditionsJSON {

    @SerializedName("generations")
    @Expose
    private Integer generations;
    @SerializedName("fitness")
    @Expose
    private Integer fitness;
    @SerializedName("time")
    @Expose
    private Long time;

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

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

}
