package info.androidhive.firebase.Classes.Models;

import java.util.List;

public class RatedUser {

    public String name;
    public String currentPoints;
    public String photoUrl;
    public List<RatedMatchesToDB> ratedMatches;


    public void setName(String name) {
        this.name = name;
    }

    public void setCurrentPoints(String currentPoints) {
        this.currentPoints = currentPoints;
    }

    public void setRatedMatches(List<RatedMatchesToDB> ratedMatches) {
        this.ratedMatches = ratedMatches;
    }

    public String getName() {
        return name;
    }

    public List<RatedMatchesToDB> getRatedMatches() {
        return ratedMatches;
    }

    public String getCurrentPoints() {
        return currentPoints;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}
