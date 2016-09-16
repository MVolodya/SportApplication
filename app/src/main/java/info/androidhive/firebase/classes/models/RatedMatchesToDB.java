package info.androidhive.firebase.classes.models;

public class RatedMatchesToDB {

    private String mMatchId;
    private String mPoints;
    private String mTypeOfRate;
    private String mStatus;
    private static RatedMatchesToDB sRatedMatches;

    public static RatedMatchesToDB getInstance() {

        if(sRatedMatches == null) sRatedMatches = new RatedMatchesToDB();
        return sRatedMatches;
    }

    private RatedMatchesToDB(){}

    public void setMatchId(String mMatchId) {
        this.mMatchId = mMatchId;
    }

    public void setPoints(String mPoints) {
        this.mPoints = mPoints;
    }

    public String getPoints() {
        return mPoints;
    }

    public String getMatchId() {
        return mMatchId;
    }

    public String getTypeOfRate() {
        return mTypeOfRate;
    }

    public void setTypeOfRate(String mTypeOfRate) {
        this.mTypeOfRate = mTypeOfRate;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String mStatus) {
        this.mStatus = mStatus;
    }
}
