package info.androidhive.firebase.fragments.settingsFragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import info.androidhive.firebase.R;
import info.androidhive.firebase.activity.mainActivity.MainActivity;
import info.androidhive.firebase.activity.navigationDrawerActivity.NavigationDrawerActivity;
import info.androidhive.firebase.activity.splashScreenActivity.SplashActivity;
import info.androidhive.firebase.classes.managers.AlertDialogManager;
import info.androidhive.firebase.classes.managers.LocalDatabaseManager;
import info.androidhive.firebase.classes.managers.ProgressDialogManager;
import info.androidhive.firebase.classes.managers.RemoteDatabaseManager;
import info.androidhive.firebase.classes.models.DataHelper;
import info.androidhive.firebase.classes.models.User;
import info.androidhive.firebase.fragments.bottomSheetFragment.BottomSheetFaqFragment;
import info.androidhive.firebase.fragments.settingsFragment.presenter.SettingsPresenter;
import info.androidhive.firebase.fragments.settingsFragment.view.SettingsView;

public class SettingsFragment extends Fragment implements View.OnClickListener, SettingsView {

    public static final int PICK_IMAGE_REQUEST = 1;
    public static final int CAMERA_REQUEST = 2;

    private TextView usernameTv;
    private TextView emailTv;
    private ImageView userPhoto;
    private CollapsingToolbarLayout collapsingToolbarLayout;

    private User mUser;
    private FirebaseUser mFirebaseUser;
    private ProgressDialog mProgressDialog;
    private RemoteDatabaseManager mRemoteDatabaseManager;
    private SettingsPresenter settingsPresenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.settings_toolbar);
        ((MainActivity)getContext()).setSupportActionBar(toolbar);
        ((MainActivity)getContext()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        ((MainActivity)getContext()).getSupportActionBar().setHomeButtonEnabled(true);
        toolbar.showOverflowMenu();

        settingsPresenter = new SettingsPresenter();
        settingsPresenter.setSettingsView(this);

        usernameTv = (TextView) view.findViewById(R.id.username_setting_tv);
        emailTv = (TextView) view.findViewById(R.id.email_setting_tv);
        TextView languageTv = (TextView) view.findViewById(R.id.language_change_tv);
        userPhoto = (ImageView) view.findViewById(R.id.photo_iv);
        RelativeLayout relativeLayoutChangeLan = (RelativeLayout) view.findViewById(R.id.change_lan_rl);
        relativeLayoutChangeLan.setOnClickListener(this);

        SharedPreferences sharedPref = getActivity().getSharedPreferences("settings", Context.MODE_PRIVATE);
        if(sharedPref.getString("language","en").equalsIgnoreCase("en")) languageTv.setText(
                R.string.english);
        else if(sharedPref.getString("language","en").equalsIgnoreCase("uk")) languageTv.setText(
                R.string.ukrainian);

        mProgressDialog = new ProgressDialog(getActivity());
        mRemoteDatabaseManager = new RemoteDatabaseManager(getActivity());
        LocalDatabaseManager localDatabaseManager = new LocalDatabaseManager(getActivity());
        FirebaseAuth auth = FirebaseAuth.getInstance();
        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        UserInfo userInfo = mFirebaseUser != null ? mFirebaseUser.getProviderData().get(1) : null;
        // Id of the provider (ex: google.com)
        String providerId = userInfo.getProviderId();

        mUser = LocalDatabaseManager.getUser();

        //start AlertDialog FAB -------------------------
        FloatingActionButton floatingActionButton = (FloatingActionButton) view.findViewById(R.id.change_photo_fab);
        floatingActionButton.setOnClickListener(this);
        //end /AlertDialog FAB

        //start AlertDialog username
        RelativeLayout relativeLayout = (RelativeLayout) view.findViewById(R.id.username_relative);
        relativeLayout.setOnClickListener(this);
        //end AlertDialog username

        //start AlertDialog email
        relativeLayout = (RelativeLayout) view.findViewById(R.id.email_relative);
        relativeLayout.setOnClickListener(this);
        //end AlertDialog email

        //start AlertDialog password
        relativeLayout = (RelativeLayout) view.findViewById(R.id.password_relative);
        relativeLayout.setOnClickListener(this);
        if (providerId.equals("facebook.com") || providerId.equals("google.com")) {
            TextView tvPassword = (TextView) relativeLayout.findViewById(R.id.password_setting_tv);
            tvPassword.setEnabled(false);
            tvPassword.setTextColor(getResources().getColor(R.color.disabletext));
            relativeLayout.setEnabled(false);
            relativeLayout.setBackgroundColor(getResources().getColor(R.color.disableback));
        } else relativeLayout.setEnabled(true);
        //end AlertDialog password

        relativeLayout = (RelativeLayout) view.findViewById(R.id.bottom_sheet_faq_relative);
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new BottomSheetFaqFragment().show(getFragmentManager(),
                       null);
            }
        });

        collapsingToolbarLayout = (CollapsingToolbarLayout) view.findViewById(R.id.setting_collapsing_toolbar);

        if (mUser.getName() != null) collapsingToolbarLayout.setTitle(mUser.getName());
        else collapsingToolbarLayout.setTitle("Anonymous");

        setUserInformation();
        return view;
    }

    private void setUserInformation() {
        if (mUser.getName() != null) usernameTv.setText(mUser.getName());
        else usernameTv.setText("Anonymous");

        if (mUser.getEmail() != null) emailTv.setText(mUser.getEmail());
        else emailTv.setText("Anonymous@Anonymous.com");

        if (mUser.getPhotoURL() != null) {
            Glide.with(this)
                    .load(mUser.getPhotoURL())
                    .into(userPhoto);
        } else userPhoto.setImageResource(R.drawable.prof);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.change_photo_fab:
                AlertDialog.Builder alertDialogPhoto = new AlertDialogManager().getPhotoAlertDialog(getActivity(), this);
                alertDialogPhoto.show();
                break;
            case R.id.username_relative:
                AlertDialog.Builder alertDialogUsername = AlertDialogManager.getAlertDialog(getActivity(),
                        R.layout.dialog_username);
                alertDialogUsername.setPositiveButton(getContext().getString(R.string.save),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                settingsPresenter.updateUsername(mRemoteDatabaseManager);
                            }
                        });
                alertDialogUsername.setNegativeButton(getContext().getString(R.string.cancel),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                alertDialogUsername.show();
                break;
            case R.id.email_relative:
                AlertDialog.Builder alertDialogEmail = AlertDialogManager.getAlertDialog(getActivity(),
                        R.layout.dialog_email);
                alertDialogEmail.setPositiveButton(getContext().getString(R.string.save),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                settingsPresenter.updateEmail();
                            }
                        });
                alertDialogEmail.setNegativeButton(getContext().getString(R.string.cancel),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                alertDialogEmail.show();
                break;
            case R.id.password_relative:
                AlertDialog.Builder alertDialogPassword = AlertDialogManager.getAlertDialog(getActivity(),
                        R.layout.dialog_password);
                alertDialogPassword.setPositiveButton(getContext().getString(R.string.save),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                settingsPresenter.updatePassword();
                            }
                        });
                alertDialogPassword.setNegativeButton(getContext().getString(R.string.cancel),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                alertDialogPassword.show();
                break;
            case R.id.change_lan_rl:
                LayoutInflater layoutInflater = LayoutInflater.from(getContext());
                View promptView = layoutInflater.inflate(R.layout.dialog_language, null);
                final AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                alert.setView(promptView);
                alert.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        settingsPresenter.changeLanguage(DataHelper.getInstance().getLanguage(),
                                getContext());
                    }
                });
                alert.setNegativeButton(R.string.cancel, null);

                final AlertDialog dialog = alert.create();
                dialog.show();
                final CircleImageView enIv = (CircleImageView)promptView.findViewById(R.id.english_iv);
                final CircleImageView ukIv = (CircleImageView)promptView.findViewById(R.id.ukrainian_iv);
                dialog.getButton(dialog.BUTTON1).setEnabled(false);
                enIv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.getButton(dialog.BUTTON1).setEnabled(true);
                        enIv.setBorderColor(Color.parseColor("#628f3e"));
                        ukIv.setBorderColor(Color.parseColor("#ffffff"));
                        DataHelper.getInstance().setLanguage("en");
                    }
                });

                ukIv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.getButton(dialog.BUTTON1).setEnabled(true);
                        ukIv.setBorderColor(Color.parseColor("#628f3e"));
                        enIv.setBorderColor(Color.parseColor("#ffffff"));
                        DataHelper.getInstance().setLanguage("uk");
                    }
                });
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == getActivity().RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                ProgressDialogManager.showProgressDialog(mProgressDialog, getContext().getString(R.string.wait_loading_photo));
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                Bitmap bMapScaled = Bitmap.createScaledBitmap(bitmap, 900, 800, true);
                settingsPresenter.updatePhoto(bMapScaled, mFirebaseUser.getUid());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (requestCode == CAMERA_REQUEST && resultCode == getActivity().RESULT_OK) {
            ProgressDialogManager.showProgressDialog(mProgressDialog, getContext().getString(R.string.wait_loading_photo));
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            Bitmap bMapScaled = Bitmap.createScaledBitmap(photo, 900, 800, true);
            settingsPresenter.updatePhoto(bMapScaled, mFirebaseUser.getUid());
        }
    }

    @Override
    public RemoteDatabaseManager getRemoteDatabaseManager() {
        return mRemoteDatabaseManager;
    }

    @Override
    public void updatePhotoSuccess(String url) {
        ProgressDialogManager.hideProgressDialog(mProgressDialog);
        Glide.with(this)
                .load(url)
                .into(userPhoto);
        ((NavigationDrawerActivity)getContext()).updateProfilePhoto(url);
        Toast.makeText(getContext(), R.string.photo_update, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void updateUsernameSuccess() {
        usernameTv.setText(mUser.getName());
        collapsingToolbarLayout.setTitle(mUser.getName());
        ((NavigationDrawerActivity)getContext()).updateProfileUsername(mUser.getName());
        Toast.makeText(getContext(), R.string.name_update, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void updateEmailSuccess() {
        emailTv.setText(mUser.getEmail());
        ((NavigationDrawerActivity)getContext()).updateProfileEmail(mUser.getEmail());
        Toast.makeText(getContext(), R.string.email_update, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void updatePasswordSuccess() {
        Toast.makeText(getContext(), R.string.password_update, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void changeLanguageSuccess(String lan) {
        Intent intent = new Intent(getContext(), SplashActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        getContext().startActivity(intent);
    }

    @Override
    public void onEmptyField(EditText editText) {
        Toast.makeText(getContext(), R.string.fill_field, Toast.LENGTH_SHORT).show();
    }
}