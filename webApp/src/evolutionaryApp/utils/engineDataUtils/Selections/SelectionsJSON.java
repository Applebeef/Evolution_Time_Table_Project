
package evolutionaryApp.utils.engineDataUtils.Selections;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class SelectionsJSON {

    @SerializedName("truncation")
    @Expose
    private Truncation truncation;
    @SerializedName("rouletteWheel")
    @Expose
    private RouletteWheel rouletteWheel;
    @SerializedName("tournament")
    @Expose
    private Tournament tournament;

    public Truncation getTruncation() {
        return truncation;
    }

    public void setTruncation(Truncation truncation) {
        this.truncation = truncation;
    }

    public RouletteWheel getRouletteWheel() {
        return rouletteWheel;
    }

    public void setRouletteWheel(RouletteWheel rouletteWheel) {
        this.rouletteWheel = rouletteWheel;
    }

    public Tournament getTournament() {
        return tournament;
    }

    public void setTournament(Tournament tournament) {
        this.tournament = tournament;
    }

}
