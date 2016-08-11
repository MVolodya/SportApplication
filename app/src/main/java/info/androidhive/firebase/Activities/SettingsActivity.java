package info.androidhive.firebase.Activities;

import android.app.ProgressDialog;
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
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;

import info.androidhive.firebase.Classes.AlertDialogCreator;
import info.androidhive.firebase.Classes.LocalDatabaseManager;
import info.androidhive.firebase.Classes.ProgressDialogManager;
import info.androidhive.firebase.Classes.RemoteDatabaseManager;
import info.androidhive.firebase.Classes.ResponseUrl;
import info.androidhive.firebase.Classes.User;
import info.androidhive.firebase.Classes.UserManager;
import info.androidhive.firebase.Fragments.BottomSheetFaqFragment;
import info.androidhive.firebase.R;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener, ResponseUrl {

    private int PICK_IMAGE_REQUEST = 1;
    private String photoUrl;

    private TextView etUsername;
    private TextView etEmail;
    private Button btnSave;
    private Button btnChangePassword;
    private ImageView userPhoto;
    private LinearLayout linearLayout;

    private User user;
    private LocalDatabaseManager localDatabaseManager;

    private FirebaseAuth auth;
    private FirebaseUser firebaseUser;
    private ProgressDialog mProgressDialog;
    private ProgressDialogManager progressDialogManager;
    private RelativeLayout relativeLayout;


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

        localDatabaseManager = new LocalDatabaseManager(this);
        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getInstance().getCurrentUser();

        user = LocalDatabaseManager.getUser();

        progressDialogManager = new ProgressDialogManager(this, mProgressDialog);

        //start AlertDialog FAB -------------------------
        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.change_photo);
        floatingActionButton.setOnClickListener(this);
        //end /AlertDialog FAB


        //start AlertDialog username
        relativeLayout = (RelativeLayout) findViewById(R.id.usernameDialog);
        relativeLayout.setOnClickListener(this);
        //end AlertDialog username

        //start AlertDialog email
        relativeLayout = (RelativeLayout) findViewById(R.id.emailDialog);
        relativeLayout.setOnClickListener(this);
        //end AlertDialog email

        //start AlertDialog password
        relativeLayout = (RelativeLayout) findViewById(R.id.passDialog);
        relativeLayout.setOnClickListener(this);
        //end AlertDialog password

        //start BottomSheetFAQ
        relativeLayout = (RelativeLayout) findViewById(R.id.bottomsheet_faq_relative);
        View bottomSheetView = findViewById(R.id.bottomSheet);
        // BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetView);
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new BottomSheetFaqFragment().show(getSupportFragmentManager(),
                        SettingsActivity.class.getSimpleName());
            }
        });
        //end BottomSheetFAQ


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

                LayoutInflater layoutInflater = LayoutInflater.from(this);
                view = layoutInflater.inflate(R.layout.dialog_photo, null);

                AlertDialog.Builder alertDialogBuilderPhoto = new AlertDialogCreator(this)
                        .initDialogPhoto(view, R.layout.dialog_photo);
                alertDialogBuilderPhoto.setView(view);

                final AlertDialog alertDialogFAB = alertDialogBuilderPhoto.create();
                alertDialogFAB.show();

                Button button = (Button) view.findViewById(R.id.buttonGallery);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
                        alertDialogFAB.dismiss();
                        alertDialogFAB.hide();
                    }
                });


                break;

//            case R.id.usernameDialog:
//                AlertDialog alertDialogUsername = new AlertDialogCreator(this)
//                        .initDialog(view, R.layout.dialog_username).create();
//                alertDialogUsername.show();
//                break;
//
//            case R.id.emailDialog:
//                AlertDialog alertDialogEmail = new AlertDialogCreator(this)
//                        .initDialog(view, R.layout.dialog_email).create();
//                alertDialogEmail.show();
//                break;
//
//            case R.id.passDialog:
//                AlertDialog alertDialogPassword = new AlertDialogCreator(this)
//                        .initDialog(view, R.layout.dialog_password).create();
//                alertDialogPassword.show();
//                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("Wait...");
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.setCancelable(false);
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.show();
        }

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {

                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                new RemoteDatabaseManager(this).uploadImage(bitmap, this);


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void setUrl(String url) {

        UserManager.changeProfile(url);

        LocalDatabaseManager.updateUrl(url);

        Glide.with(this)
                .load(user.getPhotoURL())
                .into(userPhoto);

        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
            mProgressDialog.dismiss();
        }

    }



}
