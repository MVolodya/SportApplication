package info.androidhive.firebase.classes.retrofit.team;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Links {

    @SerializedName("self")
    @Expose
    private Self self;

    @SerializedName("fixtures")
    @Expose
    private Fixtures fixtures;

    @SerializedName("players")
    @Expose
    private Players players;

    public Self getSelf() {
        return self;
    }

    public Fixtures getFixtures() {
        return fixtures;
    }

    public Players getPlayers() {
        return players;
    }
}
