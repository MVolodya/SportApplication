package info.androidhive.firebase.Classes.Retrofit.Players;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Links {
    @SerializedName("self")
    @Expose
    private Self self;
    @SerializedName("team")
    @Expose
    private Team team;

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
     * @return The team
     */
    public Team getTeam() {
        return team;
    }

    /**
     * @param team The team
     */
    public void setTeam(Team team) {
        this.team = team;
    }
}
