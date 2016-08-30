package info.androidhive.firebase.Activities;

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

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import info.androidhive.firebase.Classes.Utils.ConnectivityReceiver;
import info.androidhive.firebase.Classes.Managers.LocalDatabaseManager;
import info.androidhive.firebase.Classes.Models.User;
import info.androidhive.firebase.Fragments.MainFragment;
import info.androidhive.firebase.R;

public class MainActivity extends NavigationDrawerActivity {

    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;
    private LocalDatabaseManager localDatabaseManager;
    private FirebaseUser user;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        boolean isConnected = ConnectivityReceiver.isOnline(this);
        showSnack(isConnected);

        //get firebase auth instance
        auth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        localDatabaseManager = new LocalDatabaseManager(this);
        User userCustom = localDatabaseManager.getUser();

//        RemoteDatabaseManager remoteDatabaseManager = new RemoteDatabaseManager(this);
//        remoteDatabaseManager.setUserData(user.getUid(),"jjj","1000");

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if (user == null && AccessToken.getCurrentAccessToken() == null) {
                    // user auth state is changed - user is null
                    // launch login activity
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();
                }
            }
        };

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        try {
            initializeNavigationDrawer(toolbar, userCustom);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new MainFragment(), "main")
                .commit();

    }



    @Override
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(authListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (authListener != null) {
            auth.removeAuthStateListener(authListener);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalDatabaseManager.close();
    }

    public void showToolbar(){
        toolbar.setVisibility(View.VISIBLE);
    }

    public void hideToolbar(){
        toolbar.setVisibility(View.GONE);
    }

    public void lockSwipe(){
        result.getDrawerLayout().setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    public void unlockSwipe(){
        result.getDrawerLayout().setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
    }

    public ActionBar getToolbar(){
        return getSupportActionBar();
    }

    // Showing the status in Snackbar
    private void showSnack(boolean isConnected) {

        View view = findViewById(R.id.container);
        String message;
        int color;
        if (isConnected) {
            message = "Good! Connected to Internet";
            color = Color.WHITE;
        } else {
            message = "Sorry! Not connected to internet";
            color = Color.WHITE;
        }

        Snackbar snackbar = Snackbar
                .make(view, message, Snackbar.LENGTH_LONG);

        View sbView = snackbar.getView();

        if (isConnected) {
            sbView.setBackgroundColor(getResources().getColor(R.color.snackbar_ok));
        } else {
            sbView.setBackgroundColor(getResources().getColor(R.color.snackbar));
        }

        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(color);
        snackbar.show();
    }

    // close ND on button back
    @Override
    public void onBackPressed() {

        if (result != null && result.isDrawerOpen())
            result.closeDrawer();
            else if (getFragmentManager().getBackStackEntryCount() > 0) {
                getFragmentManager().popBackStack();
                toolbar.setVisibility(View.VISIBLE);
            } else super.onBackPressed();
        }



}

