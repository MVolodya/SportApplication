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
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;

import java.io.IOException;

import info.androidhive.firebase.Classes.Utils.AlertDialogCreator;
import info.androidhive.firebase.Classes.Managers.LocalDatabaseManager;
import info.androidhive.firebase.Classes.Managers.ProgressDialogManager;
import info.androidhive.firebase.Classes.Managers.RemoteDatabaseManager;
import info.androidhive.firebase.Classes.Managers.ResponseUrl;
import info.androidhive.firebase.Classes.Models.User;
import info.androidhive.firebase.Classes.Managers.UserManager;
import info.androidhive.firebase.Fragments.BottomSheetFaqFragment;
import info.androidhive.firebase.R;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener, ResponseUrl {

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int CAMERA_REQUEST = 2;


    private TextView etUsername;
    private TextView etEmail;
    private ImageView userPhoto;
    private User user;
    private LocalDatabaseManager localDatabaseManager;

    private FirebaseAuth auth;
    private FirebaseUser firebaseUser;
    private ProgressDialog mProgressDialog;
    private ProgressDialogManager progressDialogManager;
    private RelativeLayout relativeLayout;
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

        remoteDatabaseManager = new RemoteDatabaseManager(this);
        localDatabaseManager = new LocalDatabaseManager(this);
        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getInstance().getCurrentUser();

        UserInfo userInfo = firebaseUser.getProviderData().get(1);
        // Id of the provider (ex: google.com)
        String providerId = userInfo.getProviderId();

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
        if (providerId.equals("facebook.com") || providerId.equals("google.com")) {
            TextView tvPassword = (TextView) relativeLayout.findViewById(R.id.password_setting);
            tvPassword.setEnabled(false);
            tvPassword.setTextColor(getResources().getColor(R.color.disabletext));
            relativeLayout.setEnabled(false);
            relativeLayout.setBackgroundColor(getResources().getColor(R.color.disableback));
        } else relativeLayout.setEnabled(true);
        //end AlertDialog password

        //start BottomSheetFAQ
        relativeLayout = (RelativeLayout) findViewById(R.id.bottomsheet_faq_relative);
        // View bottomSheetView = findViewById(R.id.bottomSheet);
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

                Button buttonGallery = (Button) view.findViewById(R.id.buttonGallery);
                buttonGallery.setOnClickListener(new View.OnClickListener() {
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

                Button buttonCamera = (Button) view.findViewById(R.id.buttonCamera);
                buttonCamera.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(cameraIntent, CAMERA_REQUEST);
                        alertDialogFAB.dismiss();
                        alertDialogFAB.hide();
                    }
                });


                break;

            case R.id.usernameDialog:

                AlertDialog.Builder alertDialogUsername = new AlertDialog.Builder(view.getContext());
                alertDialogUsername.setTitle("Edit name");

                final EditText inputUsername = new EditText(view.getContext());
                LinearLayout.LayoutParams lpUsername = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                inputUsername.setSelection(inputUsername.getText().length());
                inputUsername.setPadding(20, 30, 20, 30);
                inputUsername.setHint("Enter new name");
                inputUsername.setLayoutParams(lpUsername);
                alertDialogUsername.setView(inputUsername);


                alertDialogUsername.setPositiveButton("Save",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                UserManager.updateUsername(inputUsername.getText().toString());
                                LocalDatabaseManager.updateName(inputUsername.getText().toString());
                                remoteDatabaseManager.updateUsername(firebaseUser.getDisplayName(),
                                        inputUsername.getText().toString());
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
                AlertDialog.Builder alertDialogEmail = new AlertDialog.Builder(view.getContext());
                alertDialogEmail.setTitle("Edit email");

                final EditText inputEmail = new EditText(view.getContext());
                LinearLayout.LayoutParams lpEmail = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                inputEmail.setSelection(inputEmail.getText().length());
                inputEmail.setPadding(20, 30, 20, 30);
                inputEmail.setHint("Enter new email");
                inputEmail.setLayoutParams(lpEmail);
                alertDialogEmail.setView(inputEmail);

                alertDialogEmail.setPositiveButton("Save",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                UserManager.updateEmail(inputEmail.getText().toString());
                                LocalDatabaseManager.updateEmail(inputEmail.getText().toString());
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
                AlertDialog.Builder alertDialogPassword = new AlertDialog.Builder(view.getContext());
                alertDialogPassword.setTitle("Edit password");

                final EditText inputPassword = new EditText(view.getContext());
                LinearLayout.LayoutParams lpPassword = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                inputPassword.setSelection(inputPassword.getText().length());
                inputPassword.setPadding(20, 30, 20, 30);
                inputPassword.setHint("Enter password");
                inputPassword.setLayoutParams(lpPassword);
                alertDialogPassword.setView(inputPassword);

                alertDialogPassword.setPositiveButton("Save",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                UserManager.updatePassword(inputPassword.getText().toString());
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

                mProgressDialog = new ProgressDialog(this);
                mProgressDialog.setMessage("Wait, while loading photo!");
                mProgressDialog.setCanceledOnTouchOutside(false);
                mProgressDialog.setCancelable(false);
                mProgressDialog.setIndeterminate(true);
                mProgressDialog.show();

                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                new RemoteDatabaseManager(this).uploadImage(bitmap, firebaseUser.getUid(), this);


            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {

            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("Wait, while loading photo!");
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.setCancelable(false);
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.show();

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
            mProgressDialog.hide();
            mProgressDialog.dismiss();
        }

    }


}
