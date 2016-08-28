package info.androidhive.firebase.Classes.Utils;

/**
 * Created by andri on 04.08.2016.
 */
public class ConvertDate {

    public static String getDate(String date) {

        StringBuilder builder = new StringBuilder();
        char dateArray[] = date.toCharArray();

        for(char c:dateArray){
            if(c != 'T') builder.append(c);
            else break;
        }
        return builder.toString();
    }
}
