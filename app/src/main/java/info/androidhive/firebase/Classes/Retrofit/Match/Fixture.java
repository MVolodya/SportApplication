package info.androidhive.firebase.Classes.Retrofit.Match;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Fixture {

    @SerializedName("_links")
    @Expose
    private Links links;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("matchday")
    @Expose
    private Integer matchday;
    @SerializedName("homeTeamName")
    @Expose
    private String homeTeamName;
    @SerializedName("awayTeamName")
    @Expose
    private String awayTeamName;
    @SerializedName("result")
    @Expose
    private Result result;
    @SerializedName("odds")
    @Expose
    private Odds odds;

    /**
     * @return The links
     */
    public Links getLinks() {
        return links;
    }

    /**
     * @param links The _links
     */
    public void setLinks(Links links) {
        this.links = links;
    }

    /**
     * @return The date
     */
    public String getDate() {
        return date;
    }

    /**
     * @param date The date
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * @return The status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status The status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return The matchday
     */
    public Integer getMatchday() {
        return matchday;
    }

    /**
     * @param matchday The matchday
     */
    public void setMatchday(Integer matchday) {
        this.matchday = matchday;
    }

    /**
     * @return The homeTeamName
     */
    public String getHomeTeamName() {
        return homeTeamName;
    }

    /**
     * @param homeTeamName The homeTeamName
     */
    public void setHomeTeamName(String homeTeamName) {
        this.homeTeamName = homeTeamName;
    }

    /**
     * @return The awayTeamName
     */
    public String getAwayTeamName() {
        return awayTeamName;
    }

    /**
     * @param awayTeamName The awayTeamName
     */
    public void setAwayTeamName(String awayTeamName) {
        this.awayTeamName = awayTeamName;
    }

    /**
     * @return The result
     */
    public Result getResult() {
        return result;
    }

    /**
     * @param result The result
     */
    public void setResult(Result result) {
        this.result = result;
    }

    /**
     * @return The odds
     */
    public Odds getOdds() {
        return odds;
    }

    /**
     * @param odds The odds
     */
    public void setOdds(Odds odds) {
        this.odds = odds;
    }

}
