package info.androidhive.firebase.activity.splashScreenActivity.view;

import android.content.Context;

import java.util.List;

import info.androidhive.firebase.classes.models.RatedMatchesToDB;

public interface SplashScreenView {
    void onSuccess();
    void onFail();
    void setUserRateList(List<RatedMatchesToDB> userRateList);
    Context getContext();
}
