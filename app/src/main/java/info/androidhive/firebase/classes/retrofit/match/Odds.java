package info.androidhive.firebase.classes.retrofit.match;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Odds {

    @SerializedName("homeWin")
    @Expose
    private Double homeWin;

    @SerializedName("draw")
    @Expose
    private Double draw;

    @SerializedName("awayWin")
    @Expose
    private Double awayWin;

    public Double getHomeWin() {
        return homeWin;
    }

    public Double getDraw() {
        return draw;
    }

    public Double getAwayWin() {
        return awayWin;
    }
}