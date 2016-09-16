package info.androidhive.firebase.classes.retrofit.match;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Result {

    @SerializedName("goalsHomeTeam")
    @Expose
    private Integer goalsHomeTeam;

    @SerializedName("goalsAwayTeam")
    @Expose
    private Integer goalsAwayTeam;

    public Object getGoalsHomeTeam() {
        return goalsHomeTeam;
    }

    public Object getGoalsAwayTeam() {
        return goalsAwayTeam;
    }
}