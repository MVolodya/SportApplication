package info.androidhive.firebase.Classes.Retrofit.Team;

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

    /**
     * @return The self
     */
    public Self getSelf() {
        return self;
    }

    /**
     * @param self The self
     */
    public void setSelf(Self self) {
        this.self = self;
    }

    /**
     * @return The fixtures
     */
    public Fixtures getFixtures() {
        return fixtures;
    }

    /**
     * @param fixtures The fixtures
     */
    public void setFixtures(Fixtures fixtures) {
        this.fixtures = fixtures;
    }

    /**
     * @return The players
     */
    public Players getPlayers() {
        return players;
    }

    /**
     * @param players The players
     */
    public void setPlayers(Players players) {
        this.players = players;
    }
}
