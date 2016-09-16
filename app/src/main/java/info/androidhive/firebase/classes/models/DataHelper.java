package info.androidhive.firebase.classes.models;

public class DataHelper {

    private static DataHelper mOurInstance;
    private int mId;
    private int mMatchId;
    private int mHomeTeamId;
    private int mAwayTeamId;
    private int mDeletedPosition;
    private String mLanguage;
    private RatedMatchesToDB ratedMatchesToDB;
    private Rate rate;
    private String leagueName;

    public static DataHelper getInstance() {
        if (mOurInstance == null) mOurInstance = new DataHelper();
        return mOurInstance;
    }

    private DataHelper() {}

    public int getId() {
        return mId;
    }

    public void setId(int mId) {
        this.mId = mId;
    }

    public void setLeagueName(String leagueName) {
        this.leagueName = leagueName;
    }

    public String getLeagueName() {
        return leagueName;
    }

    public int getMatchId() {
        return mMatchId;
    }

    public void setMatchId(int mMatchId) {
        this.mMatchId = mMatchId;
    }

    public int getHomeTeamId() {
        return mHomeTeamId;
    }

    public void setHomeTeamId(int mHomeTeamId) {
        this.mHomeTeamId = mHomeTeamId;
    }

    public int getAwayTeamId() {
        return mAwayTeamId;
    }

    public void setAwayTeamId(int mAwayTeamId) {
        this.mAwayTeamId = mAwayTeamId;
    }

    public String getLanguage() {
        return mLanguage;
    }

    public void setLanguage(String mLanguage) {
        this.mLanguage = mLanguage;
    }
}
