package info.androidhive.firebase.classes.models;

import java.util.List;

public class RatedUser {

    private String mName;
    private String mCurrentPoints;
    private String mPhotoUrl;
    private List<RatedMatchesToDB> mRatedMatches;

    public void setName(String mName) {
        this.mName = mName;
    }

    public void setCurrentPoints(String mCurrentPoints) {
        this.mCurrentPoints = mCurrentPoints;
    }

    public void setRatedMatches(List<RatedMatchesToDB> mRatedMatches) {
        this.mRatedMatches = mRatedMatches;
    }

    public String getName() {
        return mName;
    }

    public List<RatedMatchesToDB> getRatedMatches() {
        return mRatedMatches;
    }

    public String getCurrentPoints() {
        return mCurrentPoints;
    }

    public String getPhotoUrl() {
        return mPhotoUrl;
    }

    public void setPhotoUrl(String mPhotoUrl) {
        this.mPhotoUrl = mPhotoUrl;
    }
}
