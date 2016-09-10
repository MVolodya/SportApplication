package info.androidhive.firebase.activity.splashScreenActivity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import info.androidhive.firebase.activity.loginActivity.LoginActivity;
import info.androidhive.firebase.activity.splashScreenActivity.presenter.SplashScreenPresenter;
import info.androidhive.firebase.activity.splashScreenActivity.view.SplashScreenView;
import info.androidhive.firebase.classes.managers.RateManager;
import info.androidhive.firebase.classes.models.RatedMatchesToDB;
import info.androidhive.firebase.R;

public class SplashActivity extends Activity implements SplashScreenView {

    private final RateManager rateManager = new RateManager(this);
    private SplashScreenPresenter splashScreenPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        splashScreenPresenter = new SplashScreenPresenter();
        splashScreenPresenter.setSplashScreenView(this);

        SharedPreferences sharedPref = getSharedPreferences("settings",Context.MODE_PRIVATE);
        if(sharedPref.getString("language","en").equalsIgnoreCase("en")) splashScreenPresenter.
                setLanguage("en");
        else if(sharedPref.getString("language","en").equalsIgnoreCase("uk")) splashScreenPresenter.
                setLanguage("uk");


        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            splashScreenPresenter.getUserRates(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
        }else{
            LoginActivity.start(this);
            finish();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    @Override
    public void onSuccess() {
        LoginActivity.start(this);
        finish();
    }

    @Override
    public void setUserRateList(List<RatedMatchesToDB> userRateList) {
        splashScreenPresenter.checkRate(userRateList);
    }

    @Override
    public Context getContext() {
        return this;
    }
}
