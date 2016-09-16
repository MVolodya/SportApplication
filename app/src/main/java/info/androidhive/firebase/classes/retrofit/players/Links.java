package info.androidhive.firebase.classes.retrofit.players;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Links {

    @SerializedName("self")
    @Expose
    private Self self;

    @SerializedName("team")
    @Expose
    private Team team;

    public Self getSelf() {
        return self;
    }

    public Team getTeam() {
        return team;
    }
}
