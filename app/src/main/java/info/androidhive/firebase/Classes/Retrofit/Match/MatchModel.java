package info.androidhive.firebase.Classes.Retrofit.Match;

/**
 * Created by andri on 29.07.2016.
 */

public class MatchModel {

    public MatchModel() {
    }

    private String competitionId;

    private String date;

    private String homeTeamName;

    private String awayTeamName;

    private String goalsHomeTeam;

    private String goalsAwayTeam;

    public String getCompetitionId() {
        return competitionId;
    }

    public String getDate() {
        return date;
    }

    public String getHomeTeamName() {
        return homeTeamName;
    }

    public String getAwayTeamName() {
        return awayTeamName;
    }

    public String getGoalsHomeTeam() {
        return goalsHomeTeam;
    }

    public String getGoalsAwayTeam() {
        return goalsAwayTeam;
    }
}
