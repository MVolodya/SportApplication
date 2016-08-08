package info.androidhive.firebase.Activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import info.androidhive.firebase.Classes.DatabaseManager;
import info.androidhive.firebase.Classes.ProgressDialogManager;
import info.androidhive.firebase.Classes.User;
import info.androidhive.firebase.R;

public class SettingsActivity extends AppCompatActivity {

    private TextView etUsername;
    private TextView etEmail;
    private Button btnSave;
    private Button btnChangePassword;
    private ImageView userPhoto;
    private LinearLayout linearLayout;

    private User user;
    private DatabaseManager databaseManager;

    private FirebaseAuth auth;
    private FirebaseUser firebaseUser;
    private ProgressDialog mProgressDialog;
    private ProgressDialogManager progressDialogManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Toolbar toolbar = (Toolbar) findViewById(R.id.anim_toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        etUsername = (TextView) findViewById(R.id.username_setting);
        etEmail = (TextView) findViewById(R.id.email_setting);
        userPhoto = (ImageView) findViewById(R.id.imageViewPhoto);

        databaseManager = new DatabaseManager(this);
        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getInstance().getCurrentUser();
        user = databaseManager.getUser();

        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);

        if (user.getName() != null) collapsingToolbar.setTitle(user.getName());
        else collapsingToolbar.setTitle("Anonymous");

        setUserInformation();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

    private void setUserInformation() {

        if (user.getName() != null) etUsername.setText(user.getName());
        else etUsername.setText("Anonymous");

        if (user.getEmail() != null) etEmail.setText(user.getEmail());
        else etEmail.setText("Anonymous@Anonymous.com");

        if (user.getPhotoURL() != null) {
            Glide.with(this)
                    .load(user.getPhotoURL())
                    .into(userPhoto);
        } else userPhoto.setImageResource(R.drawable.prof);

        //progressDialogManager.hideProgressDialog();

    }
}
