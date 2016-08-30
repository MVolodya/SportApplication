package info.androidhive.firebase.Classes.Retrofit.Match;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Result {

    @SerializedName("goalsHomeTeam")
    @Expose
    private Integer goalsHomeTeam;
    @SerializedName("goalsAwayTeam")
    @Expose
    private Integer goalsAwayTeam;

    /**
     *
     * @return
     * The goalsHomeTeam
     */
    public Object getGoalsHomeTeam() {
        return goalsHomeTeam;
    }

    /**
     *
     * @param goalsHomeTeam
     * The goalsHomeTeam
     */
    public void setGoalsHomeTeam(Integer goalsHomeTeam) {
        this.goalsHomeTeam = goalsHomeTeam;
    }

    /**
     *
     * @return
     * The goalsAwayTeam
     */
    public Object getGoalsAwayTeam() {
        return goalsAwayTeam;
    }

    /**
     *
     * @param goalsAwayTeam
     * The goalsAwayTeam
     */
    public void setGoalsAwayTeam(Integer goalsAwayTeam) {
        this.goalsAwayTeam = goalsAwayTeam;
    }

}