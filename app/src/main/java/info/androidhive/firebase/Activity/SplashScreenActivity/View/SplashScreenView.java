package info.androidhive.firebase.Activity.SplashScreenActivity.View;

import android.content.Context;

import java.util.List;

import info.androidhive.firebase.Classes.Models.RatedMatchesToDB;

public interface SplashScreenView {
    void onSuccess();
    void setUserRateList(List<RatedMatchesToDB> userRateList);
    Context getContext();
}
