package info.androidhive.firebase.Classes.Retrofit.Match;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class MatchResult {

    @SerializedName("goalsHomeTeam")
    @Expose
    private Integer goalsHomeTeam;
    @SerializedName("goalsAwayTeam")
    @Expose
    private Integer goalsAwayTeam;

    public Integer getGoalsAwayTeam() {
        return goalsAwayTeam;
    }

    public Integer getGoalsHomeTeam() {
        return goalsHomeTeam;
    }
}
