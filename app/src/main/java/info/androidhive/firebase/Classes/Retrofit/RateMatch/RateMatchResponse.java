package info.androidhive.firebase.Classes.Retrofit.RateMatch;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class RateMatchResponse {
    @SerializedName("fixture")
    @Expose
    private Fixture fixture;

    /**
     * @return The fixture
     */
    public Fixture getFixture() {
        return fixture;
    }
}
