package info.androidhive.firebase.Classes.Retrofit.Team;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by andrii on 15.08.16.
 */
public class Fixtures {
    @SerializedName("href")
    @Expose
    private String href;

    /**
     *
     * @return
     * The href
     */
    public String getHref() {
        return href;
    }

    /**
     *
     * @param href
     * The href
     */
    public void setHref(String href) {
        this.href = href;
    }
}