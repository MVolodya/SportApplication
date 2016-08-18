package info.androidhive.firebase.Classes;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andrii on 17.08.16.
 */
public class RatedUser {

    public String name;
    public String currentPoints;
    public List<RatedMatches> ratedMatches;

    public RatedUser(){}

    public RatedUser(String name, String currentPoints, ArrayList<RatedMatches> ratedMatches){
        this.name = name;
        this.currentPoints = currentPoints;
        this.ratedMatches = ratedMatches;
    }

    public String getName() {
        return name;
    }

    public List<RatedMatches> getRatedMatches() {
        return ratedMatches;
    }

    public String getCurrentPoints() {
        return currentPoints;
    }
}
