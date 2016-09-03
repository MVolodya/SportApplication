package info.androidhive.firebase.Activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;

import java.io.IOException;

import info.androidhive.firebase.Classes.Managers.AlertDialogManager;
import info.androidhive.firebase.Classes.Managers.LocalDatabaseManager;
import info.androidhive.firebase.Classes.Managers.ProgressDialogManager;
import info.androidhive.firebase.Classes.Managers.RemoteDatabaseManager;
import info.androidhive.firebase.Classes.Managers.ResponseUrl;
import info.androidhive.firebase.Classes.Models.User;
import info.androidhive.firebase.Classes.Managers.UserManager;
import info.androidhive.firebase.Fragments.BottomSheetFaqFragment;
import info.androidhive.firebase.R;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener, ResponseUrl {

    public static final int PICK_IMAGE_REQUEST = 1;
    public static final int CAMERA_REQUEST = 2;


    private TextView etUsername;
    private TextView etEmail;
    private ImageView userPhoto;
    private User user;

    private FirebaseUser firebaseUser;
    private ProgressDialog mProgressDialog;
    private RemoteDatabaseManager remoteDatabaseManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        etUsername = (TextView) findViewById(R.id.username_setting);
        etEmail = (TextView) findViewById(R.id.email_setting);
        userPhoto = (ImageView) findViewById(R.id.imageViewPhoto);

        mProgressDialog = new ProgressDialog(this);
        remoteDatabaseManager = new RemoteDatabaseManager(this);
        LocalDatabaseManager localDatabaseManager = new LocalDatabaseManager(this);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        UserInfo userInfo = firebaseUser != null ? firebaseUser.getProviderData().get(1) : null;
        // Id of the provider (ex: google.com)
        String providerId = userInfo.getProviderId();

        user = LocalDatabaseManager.getUser();

        //start AlertDialog FAB -------------------------
        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.change_photo);
        floatingActionButton.setOnClickListener(this);
        //end /AlertDialog FAB


        //start AlertDialog username
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.usernameDialog);
        relativeLayout.setOnClickListener(this);
        //end AlertDialog username

        //start AlertDialog email
        relativeLayout = (RelativeLayout) findViewById(R.id.emailDialog);
        relativeLayout.setOnClickListener(this);
        //end AlertDialog email

        //start AlertDialog password
        relativeLayout = (RelativeLayout) findViewById(R.id.passDialog);
        relativeLayout.setOnClickListener(this);
        if (providerId.equals("facebook.com") || providerId.equals("google.com")) {
            TextView tvPassword = (TextView) relativeLayout.findViewById(R.id.password_setting);
            tvPassword.setEnabled(false);
            tvPassword.setTextColor(getResources().getColor(R.color.disabletext));
            relativeLayout.setEnabled(false);
            relativeLayout.setBackgroundColor(getResources().getColor(R.color.disableback));
        } else relativeLayout.setEnabled(true);
        //end AlertDialog password


        relativeLayout = (RelativeLayout) findViewById(R.id.bottomsheet_faq_relative);

        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new BottomSheetFaqFragment().show(getSupportFragmentManager(),
                        SettingsActivity.class.getSimpleName());
            }
        });



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

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.change_photo:

                AlertDialog.Builder alertDialogPhoto = AlertDialogManager.getPhotoAlertDialog(this);
                alertDialogPhoto.show();

                break;

            case R.id.usernameDialog:
                AlertDialog.Builder alertDialogUsername = AlertDialogManager.getAlertDialog(this
                );
                alertDialogUsername.setPositiveButton("Save",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                UserManager.updateUsername(AlertDialogManager.getInput().getText().toString());

                                LocalDatabaseManager.updateName(AlertDialogManager.getInput().getText().toString());

                                remoteDatabaseManager.updateUsername(firebaseUser.getDisplayName(),
                                        AlertDialogManager.getInput().getText().toString());
                                etUsername.setText(user.getName());

                                dialog.cancel();
                            }
                        });
                alertDialogUsername.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                alertDialogUsername.show();
                break;

            case R.id.emailDialog:
                AlertDialog.Builder alertDialogEmail = AlertDialogManager.getAlertDialog(this
                );
                alertDialogEmail.setPositiveButton("Save",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                UserManager.updateEmail(AlertDialogManager.getInput().getText().toString());
                                LocalDatabaseManager.updateEmail(AlertDialogManager.getInput().getText().toString());
                                etEmail.setText(user.getEmail());
                                dialog.cancel();
                            }
                        });
                alertDialogEmail.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                alertDialogEmail.show();
                break;

            case R.id.passDialog:
                AlertDialog.Builder alertDialogPassword =AlertDialogManager.getAlertDialog(this
                );
                alertDialogPassword.setPositiveButton("Save",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                UserManager.updatePassword(AlertDialogManager.getInput().getText().toString());
                                dialog.cancel();
                            }
                        });
                alertDialogPassword.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                alertDialogPassword.show();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {


                ProgressDialogManager.showProgressDialog(mProgressDialog, "Wait, while loading photo!");

                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                new RemoteDatabaseManager(this).uploadImage(bitmap, firebaseUser.getUid(), this);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {


            ProgressDialogManager.showProgressDialog(mProgressDialog, "Wait, while loading photo!");

            Bitmap photo = (Bitmap) data.getExtras().get("data");
            new RemoteDatabaseManager(this).uploadImage(photo, firebaseUser.getUid(), this);
        }
    }

    @Override
    public void setUrl(String url) {
        UserManager.updateUrl(url);
        LocalDatabaseManager.updateUrl(url);
        remoteDatabaseManager.updatePhotoUrl(firebaseUser.getDisplayName(), url);
        Glide.with(this)
                .load(user.getPhotoURL())
                .into(userPhoto);
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            ProgressDialogManager.hideProgressDialog(mProgressDialog);
        }
    }
}
