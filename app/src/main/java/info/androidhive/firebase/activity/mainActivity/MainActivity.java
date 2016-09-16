package info.androidhive.firebase.activity.mainActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import info.androidhive.firebase.activity.navigationDrawerActivity.NavigationDrawerActivity;
import info.androidhive.firebase.activity.loginActivity.LoginActivity;
import info.androidhive.firebase.activity.mainActivity.presenter.MainActivityPresenter;
import info.androidhive.firebase.activity.mainActivity.view.MainActivityView;
import info.androidhive.firebase.classes.managers.LocalDatabaseManager;
import info.androidhive.firebase.classes.models.User;
import info.androidhive.firebase.fragments.mainFragment.MainFragment;
import info.androidhive.firebase.R;

public class MainActivity extends NavigationDrawerActivity implements MainActivityView {

    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    private Toolbar toolbar;

    public static void start(Context context) {
        Intent starter = new Intent(context, MainActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MainActivityPresenter mainActivityPresenter = new MainActivityPresenter();
        mainActivityPresenter.setView(this);
        mainActivityPresenter.checkConnection();

        //get firebase mAuth instance
        mAuth = FirebaseAuth.getInstance();
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        User userCustom = LocalDatabaseManager.getUser();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if (mUser == null && AccessToken.getCurrentAccessToken() == null) {
                    // mUser mAuth state is changed - mUser is null
                    // launch login activity
                    LoginActivity.start(getApplicationContext());
                    finish();
                }
            }
        };

        toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initializeNavigationDrawer(toolbar, userCustom, this);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.nd_fragment_container, new MainFragment(), "main")
                .commit();
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalDatabaseManager.close();
    }

    public void showToolbar() {
        toolbar.setVisibility(View.VISIBLE);
    }

    public void hideToolbar() {
        toolbar.setVisibility(View.GONE);
    }

    public void lockSwipe() {
        resultDrawer.getDrawerLayout().setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    public void unlockSwipe() {
        resultDrawer.getDrawerLayout().setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
    }

    public ActionBar getToolbar() {
        return getSupportActionBar();
    }
    // close ND on button back
    @Override
    public void onBackPressed() {
        if (resultDrawer != null && resultDrawer.isDrawerOpen())
            resultDrawer.closeDrawer();
        else if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
            toolbar.setVisibility(View.VISIBLE);
        } else super.onBackPressed();
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void setConnectionState(String msg, int color) {
        View view = findViewById(R.id.nd_fragment_container);
        Snackbar snackbar = Snackbar
                .make(view, msg, Snackbar.LENGTH_LONG);
        View sbView = snackbar.getView();
        sbView.setBackgroundColor(color);
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        snackbar.show();
    }
}

