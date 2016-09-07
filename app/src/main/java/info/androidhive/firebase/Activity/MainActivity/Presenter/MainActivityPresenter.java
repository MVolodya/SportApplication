package info.androidhive.firebase.Activity.MainActivity.Presenter;

import android.graphics.Color;

import info.androidhive.firebase.Activity.MainActivity.View.MainActivityView;
import info.androidhive.firebase.Classes.Utils.ConnectivityReceiver;
import info.androidhive.firebase.R;

public class MainActivityPresenter {

    public MainActivityView mainActivityView;

    public void setView(MainActivityView view){
        mainActivityView = view;
    }

    public void checkConnection() {
        String message;
        int color;
        if (ConnectivityReceiver.isOnline(mainActivityView.getContext())) {
            message = "Connected to Internet";
            color = mainActivityView.getContext().getResources().getColor(R.color.snackbar_ok);
        } else {
            message = "Not connected to internet";
            color = mainActivityView.getContext().getResources().getColor(R.color.snackbar);
        }
        mainActivityView.setConnectionState(message, color);
    }
}
