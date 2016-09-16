package info.androidhive.firebase.classes.models;

public class Rate {

    private String mHomeTeamName;
    private String mAwayTeamName;
    private String mType;
    private String mPoints;
    private String mStatus;
    private String mId;

    public String getHomeTeamName() {
        return mHomeTeamName;
    }

    public void setHomeTeamName(String mHomeTeamName) {
        this.mHomeTeamName = mHomeTeamName;
    }

    public String getPoints() {
        return mPoints;
    }

    public void setPoints(String mPoints) {
        this.mPoints = mPoints;
    }

    public String getType() {
        return mType;
    }

    public void setType(String mType) {
        this.mType = mType;
    }

    public String getAwayTeamName() {
        return mAwayTeamName;
    }

    public void setAwayTeamName(String mAwayTeamName) {
        this.mAwayTeamName = mAwayTeamName;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String mStatus) {
        this.mStatus = mStatus;
    }

    public String getId() {
        return mId;
    }

    public void setId(String mId) {
        this.mId = mId;
    }
}
