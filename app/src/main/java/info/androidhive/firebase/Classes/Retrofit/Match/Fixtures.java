package info.androidhive.firebase.Classes.Retrofit.Match;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Fixtures {


        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("competitionId")
        @Expose
        private Integer competitionId;
        @SerializedName("date")
        @Expose
        private String date;
        @SerializedName("matchday")
        @Expose
        private Integer matchday;
        @SerializedName("homeTeamName")
        @Expose
        private String homeTeamName;
        @SerializedName("homeTeamId")
        @Expose
        private Integer homeTeamId;
        @SerializedName("awayTeamName")
        @Expose
        private String awayTeamName;
        @SerializedName("awayTeamId")
        @Expose
        private Integer awayTeamId;
        @SerializedName("result")
        @Expose
        private MatchResult result;

    public Integer getId() {
        return id;
    }

    public Integer getCompetitionId() {
        return competitionId;
    }

    public String getDate() {
        return date;
    }

    public Integer getMatchday() {
        return matchday;
    }

    public String getHomeTeamName() {
        return homeTeamName;
    }

    public Integer getHomeTeamId() {
        return homeTeamId;
    }

    public String getAwayTeamName() {
        return awayTeamName;
    }

    public Integer getAwayTeamId() {
        return awayTeamId;
    }

    public MatchResult getResult() {
        return result;
    }
}


