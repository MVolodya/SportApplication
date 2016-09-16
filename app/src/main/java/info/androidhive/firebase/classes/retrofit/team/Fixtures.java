package info.androidhive.firebase.classes.retrofit.team;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Fixtures {
    @SerializedName("href")
    @Expose
    private String href;

    public String getHref() {
        return href;
    }
}
