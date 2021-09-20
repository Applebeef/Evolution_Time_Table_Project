
package evolutionaryApp.utils.engineDataUtils.Crossovers;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class AspectOriented {

    @SerializedName("isActive")
    @Expose
    private Boolean isActive;
    @SerializedName("cuttingPoints")
    @Expose
    private Integer cuttingPoints;
    @SerializedName("aspect")
    @Expose
    private String aspect;

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Integer getCuttingPoints() {
        return cuttingPoints;
    }

    public void setCuttingPoints(Integer cuttingPoints) {
        this.cuttingPoints = cuttingPoints;
    }

    public String getAspect() {
        return aspect;
    }

    public void setAspect(String aspect) {
        this.aspect = aspect;
    }

}
