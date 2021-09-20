
package evolutionaryApp.utils.engineDataUtils.Mutations;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class MutationsJSON {

    @SerializedName("flipping")
    @Expose
    private Flipping flipping;
    @SerializedName("sizer")
    @Expose
    private Sizer sizer;

    public Flipping getFlipping() {
        return flipping;
    }

    public void setFlipping(Flipping flipping) {
        this.flipping = flipping;
    }

    public Sizer getSizer() {
        return sizer;
    }

    public void setSizer(Sizer sizer) {
        this.sizer = sizer;
    }

}
