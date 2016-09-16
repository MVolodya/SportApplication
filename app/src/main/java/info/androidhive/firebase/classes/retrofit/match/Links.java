package info.androidhive.firebase.classes.retrofit.match;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Links {

    @SerializedName("self")
    @Expose
    private Self self;

    @SerializedName("competition")
    @Expose
    private Competition competition;

    @SerializedName("homeTeam")
    @Expose
    private HomeTeam homeTeam;

    @SerializedName("awayTeam")
    @Expose
    private AwayTeam awayTeam;

    public Self getSelf() {
        return self;
    }

    public Competition getCompetition() {
        return competition;
    }

    public HomeTeam getHomeTeam() {
        return homeTeam;
    }

    public AwayTeam getAwayTeam() {
        return awayTeam;
    }
}
