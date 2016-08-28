package info.androidhive.firebase.Classes.Managers;

import android.content.Context;

import info.androidhive.firebase.Classes.Managers.RemoteDatabaseManager;


public class RateManager {

    public static final String WIN_FIRST = "W1";
    public static final String DRAW= "D";
    public static final String WIN_SECOND = "W2";

    private Context context;
    private RemoteDatabaseManager remoteDatabaseManager;

    public RateManager (Context context){
        this.context = context;
    }

    public void setRate(String name, String matchId, String points, String typeOfRate){
        remoteDatabaseManager = new RemoteDatabaseManager(context);
        remoteDatabaseManager.setRateToDatabase(name, matchId, points, typeOfRate);
    }

}
