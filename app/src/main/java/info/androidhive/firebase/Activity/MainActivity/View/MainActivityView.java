package info.androidhive.firebase.Activity.MainActivity.View;

import android.content.Context;

public interface MainActivityView {
    Context getContext();
    void setConnectionState(String msg, int color);
}
