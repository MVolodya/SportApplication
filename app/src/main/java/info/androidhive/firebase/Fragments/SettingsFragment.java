package info.androidhive.firebase.Fragments;

import android.support.v4.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import info.androidhive.firebase.Classes.Managers.UserManager;
import info.androidhive.firebase.Classes.Models.User;
import info.androidhive.firebase.R;

public class SettingsFragment extends Fragment implements View.OnClickListener, ResponseUrl {

    public static final int PICK_IMAGE_REQUEST = 1;
    public static final int CAMERA_REQUEST = 2;

    private View view;
    private TextView etUsername;
    private TextView etEmail;
    private ImageView userPhoto;
    private User user;

    private FirebaseUser firebaseUser;
    private ProgressDialog mProgressDialog;
    private RemoteDatabaseManager remoteDatabaseManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_settings, container, false);
        Toolbar toolbar = (Toolbar)view.findViewById(R.id.toolbar);


        etUsername = (TextView)view.findViewById(R.id.username_setting);
        etEmail = (TextView)view.findViewById(R.id.email_setting);
        userPhoto = (ImageView)view.findViewById(R.id.imageViewPhoto);

        mProgressDialog = new ProgressDialog(getActivity());
        remoteDatabaseManager = new RemoteDatabaseManager(getActivity());
        LocalDatabaseManager localDatabaseManager = new LocalDatabaseManager(getActivity());
        FirebaseAuth auth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        UserInfo userInfo = firebaseUser != null ? firebaseUser.getProviderData().get(1) : null;
        // Id of the provider (ex: google.com)
        String providerId = userInfo.getProviderId();

        user = LocalDatabaseManager.getUser();

        //start AlertDialog FAB -------------------------
        FloatingActionButton floatingActionButton = (FloatingActionButton)view.findViewById(R.id.change_photo);
        floatingActionButton.setOnClickListener(this);
        //end /AlertDialog FAB


        //start AlertDialog username
        RelativeLayout relativeLayout = (RelativeLayout)view.findViewById(R.id.usernameDialog);
        relativeLayout.setOnClickListener(this);
        //end AlertDialog username

        //start AlertDialog email
        relativeLayout = (RelativeLayout)view.findViewById(R.id.emailDialog);
        relativeLayout.setOnClickListener(this);
        //end AlertDialog email

        //start AlertDialog password
        relativeLayout = (RelativeLayout)view.findViewById(R.id.passDialog);
        relativeLayout.setOnClickListener(this);
        if (providerId.equals("facebook.com") || providerId.equals("google.com")) {
            TextView tvPassword = (TextView) relativeLayout.findViewById(R.id.password_setting);
            tvPassword.setEnabled(false);
            tvPassword.setTextColor(getResources().getColor(R.color.disabletext));
            relativeLayout.setEnabled(false);
            relativeLayout.setBackgroundColor(getResources().getColor(R.color.disableback));
        } else relativeLayout.setEnabled(true);
        //end AlertDialog password


        relativeLayout = (RelativeLayout)view.findViewById(R.id.bottomsheet_faq_relative);

        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                new BottomSheetFaqFragment().show(view.getSupportFragmentManager(),
//                        SettingsActivity.class.getSimpleName());
            }
        });



        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout)view.findViewById(R.id.collapsing_toolbar);

        if (user.getName() != null) collapsingToolbar.setTitle(user.getName());
        else collapsingToolbar.setTitle("Anonymous");

        setUserInformation();
        return view;
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
                AlertDialog.Builder alertDialogPhoto = new AlertDialogManager().getPhotoAlertDialog(getActivity(), this);
                alertDialogPhoto.show();
                break;

            case R.id.usernameDialog:
                AlertDialog.Builder alertDialogUsername = AlertDialogManager.getAlertDialog(getActivity(),
                        "Enter new name");
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
                AlertDialog.Builder alertDialogEmail = AlertDialogManager.getAlertDialog(getActivity(),
                        "Enter new email");
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
                AlertDialog.Builder alertDialogPassword =AlertDialogManager.getAlertDialog(getActivity(),
                        "Enter new password");
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == getActivity().RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                ProgressDialogManager.showProgressDialog(mProgressDialog, "Wait, while loading photo!");
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                new RemoteDatabaseManager(getActivity()).uploadImage(bitmap, firebaseUser.getUid(), this);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (requestCode == CAMERA_REQUEST && resultCode == getActivity().RESULT_OK) {
            ProgressDialogManager.showProgressDialog(mProgressDialog, "Wait, while loading photo!");
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            new RemoteDatabaseManager(getActivity()).uploadImage(photo, firebaseUser.getUid(), this);
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