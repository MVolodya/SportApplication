package info.androidhive.firebase.activity.mainActivity.view;

import android.content.Context;

public interface MainActivityView {
    Context getContext();
    void setConnectionState(String msg, int color);
}
