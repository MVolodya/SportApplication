package info.androidhive.firebase.classes.retrofit.leagueTable;

import com.google.gson.annotations.SerializedName;

public class Standing {

    private Integer rank;

    private String team;

    private Integer teamId;

    private Integer playedGames;

    private String crestURI;

    private Integer points;

    private Integer goals;

    private Integer goalsAgainst;

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
