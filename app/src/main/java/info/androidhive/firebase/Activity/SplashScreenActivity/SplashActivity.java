package info.androidhive.firebase.Activity.SplashScreenActivity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import info.androidhive.firebase.Activity.LoginActivity.LoginActivity;
import info.androidhive.firebase.Activity.SplashScreenActivity.Presenter.SplashScreenPresenter;
import info.androidhive.firebase.Activity.SplashScreenActivity.View.SplashScreenView;
import info.androidhive.firebase.Classes.Managers.RateManager;
import info.androidhive.firebase.Classes.Models.RatedMatchesToDB;
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
