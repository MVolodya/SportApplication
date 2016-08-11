package info.androidhive.firebase.Classes;

/**
 * Created by andri on 06.08.2016.
 */
public class DataHelper {


    private static DataHelper ourInstance;
    private int id;
    private String leagueName;



    public static DataHelper getInstance() {

        if(ourInstance== null) ourInstance = new DataHelper();
        return ourInstance;
    }

    private DataHelper() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setLeagueName(String leagueName) {
        this.leagueName = leagueName;
    }

    public String getLeagueName() {

        return leagueName;
    }
}
