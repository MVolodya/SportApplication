package info.androidhive.firebase.Classes.Retrofit.Match;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class FixtResponse {

    @SerializedName("count")
    private Integer count;

    @SerializedName("timeFrameStart")
    private String timeFrameStart;

    @SerializedName("timeFrameEnd")
    private String timeFrameEnd;

    @SerializedName("fixtures")
    private List<Fixtures> fixtures = new ArrayList();

    public Integer getCount() {
        return count;
    }

    public List<Fixtures> getFixtures() {
        return fixtures;
    }

    public String getTimeFrameStart() {
        return timeFrameStart;
    }

    public String getTimeFrameEnd() {
        return timeFrameEnd;
    }
}

