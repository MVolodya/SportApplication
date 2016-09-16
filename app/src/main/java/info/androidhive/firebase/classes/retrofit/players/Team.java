package info.androidhive.firebase.classes.retrofit.players;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Team {
    @SerializedName("href")
    @Expose
    private String href;

    public String getHref() {
        return href;
    }
}
