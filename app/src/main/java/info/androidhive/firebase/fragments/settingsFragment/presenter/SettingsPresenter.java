package info.androidhive.firebase.fragments.settingsFragment.presenter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Locale;

import info.androidhive.firebase.activity.mainActivity.MainActivity;
import info.androidhive.firebase.classes.managers.AlertDialogManager;
import info.androidhive.firebase.classes.managers.LocalDatabaseManager;
import info.androidhive.firebase.classes.managers.RemoteDatabaseManager;
import info.androidhive.firebase.classes.managers.UserManager;
import info.androidhive.firebase.fragments.settingsFragment.callback.UpdateCallback;
import info.androidhive.firebase.fragments.settingsFragment.view.SettingsView;

public class SettingsPresenter implements UpdateCallback {

    private SettingsView settingsView;

    public void setSettingsView(SettingsView settingsView) {
        this.settingsView = settingsView;
    }

    public void updatePhoto(Bitmap bitmap, String uid) {
        settingsView.getRemoteDatabaseManager().uploadImage(bitmap, uid, this);
    }

    public void updateUsername(RemoteDatabaseManager remoteDatabaseManager) {
        UserManager.updateUsername(AlertDialogManager.getInput().getText().toString());
        LocalDatabaseManager.updateName(AlertDialogManager.getInput().getText().toString());
        remoteDatabaseManager.updateUsername(FirebaseAuth.getInstance().getCurrentUser().getDisplayName(),
                AlertDialogManager.getInput().getText().toString(), this);
    }

    public void updateEmail() {
        UserManager.updateEmail(AlertDialogManager.getInput().getText().toString(), this);
        LocalDatabaseManager.updateEmail(AlertDialogManager.getInput().getText().toString());
    }

    public void updatePassword() {
        UserManager.updatePassword(AlertDialogManager.getInput().getText().toString(), this);
    }

    public void changeLanguage(String language, Context context){
        SharedPreferences sharedPref = context.getSharedPreferences("settings",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("language", language);
        editor.apply();

        settingsView.changeLanguageSuccess(language);
    }

    @Override
    public void updatePhotoSuccess(String url) {
        UserManager.updateUrl(url);
        LocalDatabaseManager.updateUrl(url);
        settingsView.getRemoteDatabaseManager().updatePhotoUrl(FirebaseAuth.getInstance()
                        .getCurrentUser().getDisplayName()
                , url);
        settingsView.updatePhotoSuccess(url);
    }

    @Override
    public void updateUsernameSuccess() {
        settingsView.updateUsernameSuccess();
    }

    @Override
    public void updateEmailSuccess() {
        settingsView.updateEmailSuccess();
    }

    @Override
    public void updatePasswordSuccess() {
        settingsView.updatePasswordSuccess();
    }
}
