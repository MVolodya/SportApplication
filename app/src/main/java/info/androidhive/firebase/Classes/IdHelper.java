package info.androidhive.firebase.Classes;

/**
 * Created by andri on 06.08.2016.
 */
public class IdHelper {


    private static IdHelper ourInstance;
    private int id;

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
}
