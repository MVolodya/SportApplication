package info.androidhive.firebase.activity.mainActivity.presenter;

import android.content.res.Configuration;

import java.util.Locale;

import info.androidhive.firebase.activity.mainActivity.view.MainActivityView;
import info.androidhive.firebase.classes.utils.ConnectivityReceiver;
import info.androidhive.firebase.R;

public class MainActivityPresenter {

    public MainActivityView mainActivityView;

    public void setView(MainActivityView view){
        mainActivityView = view;
    }

    public void checkConnection() {
        String message;
        int color;
        if (!ConnectivityReceiver.isOnline(mainActivityView.getContext())) {
            color = mainActivityView.getContext().getResources().getColor(R.color.snackbar);
            message = mainActivityView.getContext().getString(R.string.no_connection);
            mainActivityView.setConnectionState(message, color);
        }
    }
}
