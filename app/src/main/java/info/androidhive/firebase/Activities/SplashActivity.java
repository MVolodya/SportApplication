package info.androidhive.firebase.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import info.androidhive.firebase.Classes.Managers.RateManager;
import info.androidhive.firebase.Classes.Managers.UserRateManager;
import info.androidhive.firebase.Classes.Models.RatedMatchesToDB;
import info.androidhive.firebase.R;

public class SplashActivity extends Activity implements UserRateManager {

    private RateManager rateManager = new RateManager(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if(FirebaseAuth.getInstance().getCurrentUser()!=null){
            rateManager.getUsersRates(FirebaseAuth.getInstance().getCurrentUser().getDisplayName(), this);
        }else{
            Intent i = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(i);
            finish();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    @Override
    public void setUserRateList(List<RatedMatchesToDB> userRateList) {
       rateManager.checkRate(userRateList, FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
        Intent i = new Intent(SplashActivity.this, LoginActivity.class);
        startActivity(i);
        finish();
    }
}
