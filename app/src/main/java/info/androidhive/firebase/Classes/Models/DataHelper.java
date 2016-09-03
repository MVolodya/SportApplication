package info.androidhive.firebase.Classes.Models;

public class DataHelper {


    private static DataHelper ourInstance;
    private int id;
    private int matchId;
    private int homeTeamId;
    private int awayTeamId;
    private int deletedPosition;

    private RatedMatchesToDB ratedMatchesToDB;
    private Rate rate;


    private String leagueName;


    public static DataHelper getInstance() {

        if(ourInstance== null) ourInstance = new DataHelper();
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

    public RatedMatchesToDB getRatedMatchesToDB() {
        return ratedMatchesToDB;
    }

    public void setRatedMatchesToDB(RatedMatchesToDB ratedMatchesToDB) {
        this.ratedMatchesToDB = ratedMatchesToDB;
    }

    public Rate getRate() {
        return rate;
    }

    public void setRate(Rate rate) {
        this.rate = rate;
    }

    public int getDeletedPosition() {
        return deletedPosition;
    }

    public void setDeletedPosition(int deletedPosition) {
        this.deletedPosition = deletedPosition;
    }
}
