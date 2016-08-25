package info.androidhive.firebase.Classes.Retrofit.Players;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by andrii on 25.08.16.
 */
public class Self {
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
