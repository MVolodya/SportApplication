package info.androidhive.firebase.classes.retrofit.rateMatch;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by andrii on 15.08.16.
 */
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

    /**
     *
     * @return
     * The self
     */
    public Self getSelf() {
        return self;
    }

    /**
     *
     * @param self
     * The self
     */
    public void setSelf(Self self) {
        this.self = self;
    }

    /**
     *
     * @return
     * The competition
     */
    public Competition getCompetition() {
        return competition;
    }

    /**
     *
     * @param competition
     * The competition
     */
    public void setCompetition(Competition competition) {
        this.competition = competition;
    }

    /**
     *
     * @return
     * The homeTeam
     */
    public HomeTeam getHomeTeam() {
        return homeTeam;
    }

    /**
     *
     * @param homeTeam
     * The homeTeam
     */
    public void setHomeTeam(HomeTeam homeTeam) {
        this.homeTeam = homeTeam;
    }

    /**
     *
     * @return
     * The awayTeam
     */
    public AwayTeam getAwayTeam() {
        return awayTeam;
    }

    /**
     *
     * @param awayTeam
     * The awayTeam
     */
    public void setAwayTeam(AwayTeam awayTeam) {
        this.awayTeam = awayTeam;
    }
}
