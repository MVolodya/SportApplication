package info.androidhive.firebase.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.facebook.AccessToken;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mikepenz.materialdrawer.Drawer;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import info.androidhive.firebase.Classes.DatabaseManager;
import info.androidhive.firebase.Classes.NavigationDrawerManager;
import info.androidhive.firebase.Classes.User;
import info.androidhive.firebase.Fragments.MainFragment;
import info.androidhive.firebase.R;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;
    private Drawer result;
    private DatabaseManager databaseManager;
    private FirebaseUser user;

    private NavigationDrawerManager drawerManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //get firebase auth instance
        auth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        databaseManager = new DatabaseManager(this);
        User userCustom = databaseManager.getUser();

        drawerManager = new NavigationDrawerManager(this, userCustom);

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

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        try {
            result = drawerManager.initializeNavigationDrawer(toolbar);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, new MainFragment())
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
        DatabaseManager.close();
    }

    // close ND on button back
    @Override
    public void onBackPressed() {
        if (result != null && result.isDrawerOpen()) {
            result.closeDrawer();
        } else if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else super.onBackPressed();
    }

}

