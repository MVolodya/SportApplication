package info.androidhive.firebase.activity.mainActivity.presenter;

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
