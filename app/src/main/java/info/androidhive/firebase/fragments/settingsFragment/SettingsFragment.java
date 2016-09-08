package info.androidhive.firebase.fragments.settingsFragment;

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
import android.support.v4.app.Fragment;
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

import info.androidhive.firebase.R;
import info.androidhive.firebase.classes.managers.AlertDialogManager;
import info.androidhive.firebase.classes.managers.LocalDatabaseManager;
import info.androidhive.firebase.classes.managers.ProgressDialogManager;
import info.androidhive.firebase.classes.managers.RemoteDatabaseManager;
import info.androidhive.firebase.classes.managers.ResponseUrl;
import info.androidhive.firebase.classes.managers.UserManager;
import info.androidhive.firebase.classes.models.User;
import info.androidhive.firebase.fragments.bottomSheetFragment.BottomSheetFaqFragment;
import info.androidhive.firebase.fragments.settingsFragment.presenter.SettingsPresenter;
import info.androidhive.firebase.fragments.settingsFragment.view.SettingsView;

public class SettingsFragment extends Fragment implements View.OnClickListener, SettingsView {

    public static final int PICK_IMAGE_REQUEST = 1;
    public static final int CAMERA_REQUEST = 2;

    private View view;
    private TextView etUsername;
    private TextView etEmail;
    private ImageView userPhoto;
    private CollapsingToolbarLayout collapsingToolbarLayout;

    private User user;
    private FirebaseUser firebaseUser;
    private ProgressDialog mProgressDialog;
    private RemoteDatabaseManager remoteDatabaseManager;
    private SettingsPresenter settingsPresenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_settings, container, false);
        Toolbar toolbar = (Toolbar)view.findViewById(R.id.toolbarSettings);
        toolbar.showOverflowMenu();

        settingsPresenter = new SettingsPresenter();
        settingsPresenter.setSettingsView(this);

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
                new BottomSheetFaqFragment().show(getFragmentManager(),
                       null);
            }
        });



        collapsingToolbarLayout = (CollapsingToolbarLayout)view.findViewById(R.id.collapsing_toolbar_setting);

        if (user.getName() != null) collapsingToolbarLayout.setTitle(user.getName());
        else collapsingToolbarLayout.setTitle("Anonymous");

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
                                settingsPresenter.updateUsername(remoteDatabaseManager);
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
                                settingsPresenter.updateEmail();
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
                                settingsPresenter.updatePassword();
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
                settingsPresenter.updatePhoto(bitmap, firebaseUser.getUid());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (requestCode == CAMERA_REQUEST && resultCode == getActivity().RESULT_OK) {
            ProgressDialogManager.showProgressDialog(mProgressDialog, "Wait, while loading photo!");
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            settingsPresenter.updatePhoto(photo, firebaseUser.getUid());
        }
    }



    @Override
    public RemoteDatabaseManager getRemoteDatabaseManager() {
        return remoteDatabaseManager;
    }

    @Override
    public void updatePhotoSuccess(String url) {
        Glide.with(this)
                .load(url)
                .into(userPhoto);
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            ProgressDialogManager.hideProgressDialog(mProgressDialog);
        }
    }

    @Override
    public void updateUsernameSuccess() {
        etUsername.setText(user.getName());
        collapsingToolbarLayout.setTitle(user.getName());
    }

    @Override
    public void updateEmailSuccess() {
        etEmail.setText(user.getEmail());
    }

    @Override
    public void updatePasswordSuccess() {}
}