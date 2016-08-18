package info.androidhive.firebase.Classes;

/**
 * Created by andrii on 17.08.16.
 */
public class RatedMatches {

    public String matchId;
    public String points;
    public String typeOfRate;
    private static RatedMatches ratedMatches;

    public static RatedMatches getInstance() {

        if(ratedMatches == null) ratedMatches = new RatedMatches();
        return ratedMatches;
    }

    private RatedMatches(){}

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
}
