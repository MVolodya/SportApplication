package info.androidhive.firebase.classes.retrofit.match;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class MatchResponse {

    @SerializedName("timeFrameStart")
    @Expose
    private String timeFrameStart;

    @SerializedName("timeFrameEnd")
    @Expose
    private String timeFrameEnd;

    @SerializedName("count")
    @Expose
    private Integer count;

    @SerializedName("fixtures")
    @Expose
    private List<Fixture> fixtures = new ArrayList<>();

    public String getTimeFrameStart() {
        return timeFrameStart;
    }

    public String getTimeFrameEnd() {
        return timeFrameEnd;
    }

    public Integer getCount() {
        return count;
    }

    public List<Fixture> getFixtures() {
        return fixtures;
    }
}
