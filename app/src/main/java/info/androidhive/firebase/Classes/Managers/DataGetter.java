package info.androidhive.firebase.Classes.Managers;

import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import info.androidhive.firebase.Classes.Retrofit.Match.Fixture;
import info.androidhive.firebase.Classes.Utils.ConvertDate;

public class DataGetter {

    public String getCurrentDate() {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String date = df.format(Calendar.getInstance().getTime());
        return date;
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
        Log.d("teamId", link);
        return Integer.parseInt(link.replaceAll("http://api.football-data.org/v1/teams/", ""));
    }
}
