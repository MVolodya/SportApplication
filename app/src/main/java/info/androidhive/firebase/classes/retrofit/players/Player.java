package info.androidhive.firebase.classes.retrofit.players;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Player {

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("position")
    @Expose
    private String position;

    @SerializedName("jerseyNumber")
    @Expose
    private Integer jerseyNumber;

    @SerializedName("dateOfBirth")
    @Expose
    private String dateOfBirth;

    @SerializedName("nationality")
    @Expose
    private String nationality;

    @SerializedName("contractUntil")
    @Expose
    private String contractUntil;

    @SerializedName("marketValue")
    @Expose
    private String marketValue;

    public String getName() {
        return name;
    }

    public String getPosition() {
        return position;
    }

    public Integer getJerseyNumber() {
        return jerseyNumber;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public String getNationality() {
        return nationality;
    }

    public String getContractUntil() {
        return contractUntil;
    }

    public String getMarketValue() {
        return marketValue;
    }
}
