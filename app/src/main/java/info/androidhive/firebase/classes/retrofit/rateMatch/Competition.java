package info.androidhive.firebase.classes.retrofit.rateMatch;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Competition {
    @SerializedName("href")
    @Expose
    private String href;

    public String getHref() {
        return href;
    }

}
