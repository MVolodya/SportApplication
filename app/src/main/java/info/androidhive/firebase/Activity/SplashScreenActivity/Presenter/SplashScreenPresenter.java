package info.androidhive.firebase.Activity.SplashScreenActivity.Presenter;

import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import info.androidhive.firebase.Activity.SplashScreenActivity.Callback.CheckRateCallback;
import info.androidhive.firebase.Activity.SplashScreenActivity.View.SplashScreenView;
import info.androidhive.firebase.Classes.Managers.RateManager;
import info.androidhive.firebase.Classes.Models.RatedMatchesToDB;

public class SplashScreenPresenter implements CheckRateCallback {

    private SplashScreenView splashScreenView;
    private RateManager rateManager;

    public void setSplashScreenView(SplashScreenView splashScreenView) {
        this.splashScreenView = splashScreenView;
        rateManager = new RateManager(splashScreenView.getContext());
    }

    public void getUserRates(String name){
        rateManager.getUsersRates(FirebaseAuth.getInstance().getCurrentUser().getDisplayName(),
                splashScreenView);
    }

    public void checkRate(List<RatedMatchesToDB> userRateList){
        rateManager.checkRate(userRateList, FirebaseAuth.getInstance().getCurrentUser().getDisplayName(),this);
    }

    @Override
    public void onSuccess() {
        splashScreenView.onSuccess();
    }

    @Override
    public void onFail() {}
}
