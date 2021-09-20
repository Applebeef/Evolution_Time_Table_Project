
package evolutionaryApp.utils.engineDataUtils.Crossovers;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class CrossoversJSON {

    @SerializedName("dayTimeOriented")
    @Expose
    private DayTimeOriented dayTimeOriented;
    @SerializedName("aspectOriented")
    @Expose
    private AspectOriented aspectOriented;

    public DayTimeOriented getDayTimeOriented() {
        return dayTimeOriented;
    }

    public void setDayTimeOriented(DayTimeOriented dayTimeOriented) {
        this.dayTimeOriented = dayTimeOriented;
    }

    public AspectOriented getAspectOriented() {
        return aspectOriented;
    }

    public void setAspectOriented(AspectOriented aspectOriented) {
        this.aspectOriented = aspectOriented;
    }

}
