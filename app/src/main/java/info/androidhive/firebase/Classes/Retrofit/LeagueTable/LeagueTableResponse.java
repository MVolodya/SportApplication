package info.androidhive.firebase.Classes.Retrofit.LeagueTable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;


public class LeagueTableResponse {

    @SerializedName("leagueCaption")
    private String leagueCaption;

    @SerializedName("matchday")
    private Integer matchday;

    @SerializedName("standing")
    private final List<Standing> standing = new ArrayList<>();

    public String getLeagueCaption() {
        return leagueCaption;
    }

    public List<Standing> getStanding() {
        return standing;
    }

    public Integer getMatchday() {
        return matchday;
    }
}