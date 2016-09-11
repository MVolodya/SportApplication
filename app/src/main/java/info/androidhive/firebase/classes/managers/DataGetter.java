package info.androidhive.firebase.classes.managers;

import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import info.androidhive.firebase.classes.retrofit.match.Fixture;
import info.androidhive.firebase.classes.utils.ConvertDate;

public class DataGetter {

    public String getCurrentDate() {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.format(Calendar.getInstance().getTime());
    }

    public List<Fixture> getCorrectMatches(List<Fixture> list, String currentDate) {

        ArrayList<Fixture> listCorrect = new ArrayList<>();

        for (Fixture f : list) {
            if (currentDate.equals(ConvertDate.getDate(f.getDate()))) {
                listCorrect.add(f);
            }
        }
        return listCorrect;
    }

    public int getTeamId(String link) {
        return Integer.parseInt(link.replaceAll("http://api.football-data.org/v1/teams/", ""));
    }

    public static int getMatchId(String link) {
        return Integer.parseInt(link.substring(41));
    }

   public boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email)
                .matches();
    }
}
