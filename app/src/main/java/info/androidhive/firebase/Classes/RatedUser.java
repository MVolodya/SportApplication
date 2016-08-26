package info.androidhive.firebase.Classes;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andrii on 17.08.16.
 */
public class RatedUser {

    public String name;
    public String currentPoints;
    public String photoUrl;
    public ArrayList<RatedMatches> ratedMatches;


    public void setName(String name) {
        this.name = name;
    }

    public void setCurrentPoints(String currentPoints) {
        this.currentPoints = currentPoints;
    }

    public void setRatedMatches(ArrayList<RatedMatches> ratedMatches) {
        this.ratedMatches = ratedMatches;
    }

    public String getName() {
        return name;
    }

    public ArrayList<RatedMatches> getRatedMatches() {
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
