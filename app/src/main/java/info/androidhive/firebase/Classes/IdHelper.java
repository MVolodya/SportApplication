package info.androidhive.firebase.Classes;

/**
 * Created by andri on 06.08.2016.
 */
public class IdHelper {


    private static IdHelper ourInstance;
    private int id;
    private String leagueName;

    public static IdHelper getInstance() {

        if(ourInstance== null) ourInstance = new IdHelper();
        return ourInstance;
    }

    private IdHelper() {
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
