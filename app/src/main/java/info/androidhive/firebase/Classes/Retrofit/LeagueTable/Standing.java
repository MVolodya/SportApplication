package info.androidhive.firebase.Classes.Retrofit.LeagueTable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by andri on 05.08.2016.
 */
public class Standing {

    @SerializedName("position")
    private Integer rank;

    @SerializedName("teamName")
    private String team;

    @SerializedName("teamId")
    private Integer teamId;

    @SerializedName("playedGames")
    private Integer playedGames;

    @SerializedName("crestURI")
    private String crestURI;

    @SerializedName("points")
    private Integer points;

    @SerializedName("goals")
    private Integer goals;

    @SerializedName("goalsAgainst")
    private Integer goalsAgainst;

    @SerializedName("goalDifference")
    private Integer goalDifference;

    @SerializedName("wins")
    private Integer wins;

    @SerializedName("draws")
    private Integer draws;

    @SerializedName("losses")
    private Integer losses;

    public Integer getWins() {
        return wins;
    }

    public Integer getDraws() {
        return draws;
    }

    public Integer getLosses() {
        return losses;
    }

    public Integer getRank() {
        return rank;
    }

    public String getTeam() {
        return team;
    }

    public Integer getTeamId() {
        return teamId;
    }

    public Integer getPlayedGames() {
        return playedGames;
    }

    public String getCrestURI() {
        return crestURI;
    }

    public Integer getPoints() {
        return points;
    }

    public Integer getGoals() {
        return goals;
    }

    public Integer getGoalsAgainst() {
        return goalsAgainst;
    }

    public Integer getGoalDifference() {
        return goalDifference;
    }
}
