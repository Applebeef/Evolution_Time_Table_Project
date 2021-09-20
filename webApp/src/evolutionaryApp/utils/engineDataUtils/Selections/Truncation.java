
package evolutionaryApp.utils.engineDataUtils.Selections;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class Truncation {

    @SerializedName("topPercent")
    @Expose
    private Integer topPercent;
    @SerializedName("elitism")
    @Expose
    private Integer elitism;
    @SerializedName("isActive")
    @Expose
    private Boolean isActive;

    public Integer getTopPercent() {
        return topPercent;
    }

    public void setTopPercent(Integer topPercent) {
        this.topPercent = topPercent;
    }

    public Integer getElitism() {
        return elitism;
    }

    public void setElitism(Integer elitism) {
        this.elitism = elitism;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

}
