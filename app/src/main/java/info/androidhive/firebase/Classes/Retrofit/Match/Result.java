package info.androidhive.firebase.Classes.Retrofit.Match;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Result {

    @SerializedName("goalsHomeTeam")
    @Expose
    private Object goalsHomeTeam;
    @SerializedName("goalsAwayTeam")
    @Expose
    private Object goalsAwayTeam;

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
    public void setGoalsHomeTeam(Object goalsHomeTeam) {
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
    public void setGoalsAwayTeam(Object goalsAwayTeam) {
        this.goalsAwayTeam = goalsAwayTeam;
    }

}