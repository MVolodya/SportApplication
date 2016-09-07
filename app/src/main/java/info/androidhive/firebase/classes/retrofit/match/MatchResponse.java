package info.androidhive.firebase.classes.retrofit.match;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andrii on 15.08.16.
 */
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

    /**
     *
     * @return
     * The timeFrameStart
     */
    public String getTimeFrameStart() {
        return timeFrameStart;
    }

    /**
     *
     * @param timeFrameStart
     * The timeFrameStart
     */
    public void setTimeFrameStart(String timeFrameStart) {
        this.timeFrameStart = timeFrameStart;
    }

    /**
     *
     * @return
     * The timeFrameEnd
     */
    public String getTimeFrameEnd() {
        return timeFrameEnd;
    }

    /**
     *
     * @param timeFrameEnd
     * The timeFrameEnd
     */
    public void setTimeFrameEnd(String timeFrameEnd) {
        this.timeFrameEnd = timeFrameEnd;
    }

    /**
     *
     * @return
     * The count
     */
    public Integer getCount() {
        return count;
    }

    /**
     *
     * @param count
     * The count
     */
    public void setCount(Integer count) {
        this.count = count;
    }

    /**
     *
     * @return
     * The fixtures
     */
    public List<Fixture> getFixtures() {
        return fixtures;
    }

    /**
     *
     * @param fixtures
     * The fixtures
     */
    public void setFixtures(List<Fixture> fixtures) {
        this.fixtures = fixtures;
    }
}
