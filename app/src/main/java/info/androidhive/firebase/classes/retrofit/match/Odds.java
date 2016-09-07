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

    /**
     *
     * @return
     * The homeWin
     */
    public Double getHomeWin() {
        return homeWin;
    }

    /**
     *
     * @param homeWin
     * The homeWin
     */
    public void setHomeWin(Double homeWin) {
        this.homeWin = homeWin;
    }

    /**
     *
     * @return
     * The draw
     */
    public Double getDraw() {
        return draw;
    }

    /**
     *
     * @param draw
     * The draw
     */
    public void setDraw(Double draw) {
        this.draw = draw;
    }

    /**
     *
     * @return
     * The awayWin
     */
    public Double getAwayWin() {
        return awayWin;
    }

    /**
     *
     * @param awayWin
     * The awayWin
     */
    public void setAwayWin(Double awayWin) {
        this.awayWin = awayWin;
    }

}