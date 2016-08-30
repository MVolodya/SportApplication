package info.androidhive.firebase.Classes.Models;

/**
 * Created by andrii on 17.08.16.
 */
public class RatedMatchesToDB {

    public String matchId;
    public String points;
    public String typeOfRate;
    public String status;

    private static RatedMatchesToDB ratedMatches;

    public static RatedMatchesToDB getInstance() {

        if(ratedMatches == null) ratedMatches = new RatedMatchesToDB();
        return ratedMatches;
    }

    private RatedMatchesToDB(){}

    public void setMatchId(String matchId) {
        this.matchId = matchId;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public String getPoints() {
        return points;
    }

    public String getMatchId() {
        return matchId;
    }

    public String getTypeOfRate() {
        return typeOfRate;
    }

    public void setTypeOfRate(String typeOfRate) {
        this.typeOfRate = typeOfRate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
