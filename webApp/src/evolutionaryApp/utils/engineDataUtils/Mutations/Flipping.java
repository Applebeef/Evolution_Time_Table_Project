
package evolutionaryApp.utils.engineDataUtils.Mutations;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class Flipping {

    @SerializedName("probability")
    @Expose
    private Double probability;
    @SerializedName("tupples")
    @Expose
    private Integer tupples;
    @SerializedName("component")
    @Expose
    private String component;

    public Double getProbability() {
        return probability;
    }

    public void setProbability(Double probability) {
        this.probability = probability;
    }

    public Integer getTupples() {
        return tupples;
    }

    public void setTupples(Integer tupples) {
        this.tupples = tupples;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

}