package info.androidhive.firebase.activity.splashScreenActivity.presenter;

import android.content.res.Configuration;

import com.google.firebase.auth.FirebaseAuth;

import java.util.List;
import java.util.Locale;

import info.androidhive.firebase.activity.splashScreenActivity.callback.CheckRateCallback;
import info.androidhive.firebase.activity.splashScreenActivity.view.SplashScreenView;
import info.androidhive.firebase.classes.managers.RateManager;
import info.androidhive.firebase.classes.models.RatedMatchesToDB;

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
        rateManager.addPoints(FirebaseAuth.getInstance().getCurrentUser().getDisplayName()  );
    }


    public void setLanguage(String language){
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        splashScreenView.getContext().getResources().updateConfiguration(config,
                splashScreenView.getContext().getResources().getDisplayMetrics());
    }

    @Override
    public void onSuccess() {
        splashScreenView.onSuccess();
    }

    @Override
    public void onFail() {splashScreenView.onFail();}
}
