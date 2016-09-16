package info.androidhive.firebase.classes.retrofit.players;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class PlayersResponse {

    @SerializedName("_links")
    @Expose
    private Links links;

    @SerializedName("count")
    @Expose
    private Integer count;

    @SerializedName("players")
    @Expose
    private List<Player> players = new ArrayList<>();

    public Links getLinks() {
        return links;
    }

    public Integer getCount() {
        return count;
    }

    public List<Player> getPlayers() {
        return players;
    }
}
