package info.androidhive.firebase.classes.retrofit.rateMatch;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RateMatchResponse {
    @SerializedName("fixture")
    @Expose
    private Fixture fixture;

    public Fixture getFixture() {
        return fixture;
    }
}
