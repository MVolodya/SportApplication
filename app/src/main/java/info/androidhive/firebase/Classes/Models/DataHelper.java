package info.androidhive.firebase.Classes.Models;

public class DataHelper {


    private static DataHelper ourInstance;
    private int id;
    private int matchId;
    private int homeTeamId;
    private int awayTeamId;


    private String leagueName;


    public static DataHelper getInstance() {

        if (ourInstance == null) ourInstance = new DataHelper();
        return ourInstance;
    }

    private DataHelper() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setLeagueName(String leagueName) {
        this.leagueName = leagueName;
    }

    public String getLeagueName() {

        return leagueName;
    }

    public int getMatchId() {
        return matchId;
    }

    public void setMatchId(int matchId) {
        this.matchId = matchId;
    }

    public int getHomeTeamId() {
        return homeTeamId;
    }

    public void setHomeTeamId(int homeTeamId) {
        this.homeTeamId = homeTeamId;
    }

    public int getAwayTeamId() {
        return awayTeamId;
    }

    public void setAwayTeamId(int awayTeamId) {
        this.awayTeamId = awayTeamId;
    }
}
