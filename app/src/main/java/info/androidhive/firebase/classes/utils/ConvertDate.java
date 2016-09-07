package info.androidhive.firebase.classes.utils;

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
