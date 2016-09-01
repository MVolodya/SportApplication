package info.androidhive.firebase.Classes.Retrofit.RateMatch;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HomeTeam {

    @SerializedName("href")
    @Expose
    private String href;

    /**
     * @return The href
     */
    public String getHref() {
        return href;
    }
}
