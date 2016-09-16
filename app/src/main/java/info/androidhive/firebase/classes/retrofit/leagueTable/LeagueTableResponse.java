package info.androidhive.firebase.classes.retrofit.leagueTable;

import java.util.ArrayList;
import java.util.List;


public class LeagueTableResponse {

    private String leagueCaption;

    private Integer matchday;

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