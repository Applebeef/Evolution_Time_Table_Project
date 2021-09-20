
package evolutionaryApp.utils.engineDataUtils.Selections;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class RouletteWheel {

    @SerializedName("elitism")
    @Expose
    private Integer elitism;
    @SerializedName("isActive")
    @Expose
    private Boolean isActive;

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
