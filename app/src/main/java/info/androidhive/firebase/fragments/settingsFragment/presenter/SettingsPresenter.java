package info.androidhive.firebase.fragments.settingsFragment.presenter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.text.TextUtils;

import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

import info.androidhive.firebase.classes.managers.AlertDialogManager;
import info.androidhive.firebase.classes.managers.DataGetter;
import info.androidhive.firebase.classes.managers.LocalDatabaseManager;
import info.androidhive.firebase.classes.managers.RemoteDatabaseManager;
import info.androidhive.firebase.classes.managers.UserManager;
import info.androidhive.firebase.fragments.settingsFragment.callback.UpdateCallback;
import info.androidhive.firebase.fragments.settingsFragment.view.SettingsView;

public class SettingsPresenter implements UpdateCallback {

    private SettingsView mSettingsView;

    public void setSettingsView(SettingsView settingsView) {
        this.mSettingsView = settingsView;
    }

    public void updatePhoto(Bitmap bitmap, String uid) {
        mSettingsView.getRemoteDatabaseManager().uploadImage(bitmap, uid, this);
    }

    public void updateUsername(RemoteDatabaseManager remoteDatabaseManager) {
        if (!TextUtils.isEmpty(AlertDialogManager.getsEtInput().getText().toString())) {
            UserManager.updateUsername(AlertDialogManager.getsEtInput().getText().toString());
            LocalDatabaseManager.updateName(AlertDialogManager.getsEtInput().getText().toString());
            remoteDatabaseManager.updateUsername(FirebaseAuth.getInstance().getCurrentUser().getDisplayName(),
                    AlertDialogManager.getsEtInput().getText().toString(), this);
        } else mSettingsView.onEmptyField(AlertDialogManager.getsEtInput());
    }

    public void updateEmail(String email) {
        if(!new DataGetter().isEmailValid(email)){
            mSettingsView.onValidEmail();
            return;
        }
        if (!TextUtils.isEmpty(email)) {
            UserManager.updateEmail(email, this);
            LocalDatabaseManager.updateEmail(email);
        } else mSettingsView.onEmptyField(AlertDialogManager.getsEtInput());
    }

    public void updatePassword() {
        if (!TextUtils.isEmpty(AlertDialogManager.getsEtInput().getText().toString()))
            UserManager.updatePassword(AlertDialogManager.getsEtInput().getText().toString(), this);
        else mSettingsView.onEmptyField(AlertDialogManager.getsEtInput());
    }

    public void changeLanguage(String language, Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("language", language);
        editor.apply();
        mSettingsView.changeLanguageSuccess(language);
    }

    @Override
    public void updatePhotoSuccess(String url) {
        UserManager.updateUrl(url);
        LocalDatabaseManager.updateUrl(url);
        mSettingsView.getRemoteDatabaseManager().updatePhotoUrl(FirebaseAuth.getInstance()
                        .getCurrentUser().getDisplayName()
                , url);
        mSettingsView.updatePhotoSuccess(url);
    }

    @Override
    public void updateUsernameSuccess() {
        mSettingsView.updateUsernameSuccess();
    }

    @Override
    public void updateEmailSuccess() {
        mSettingsView.updateEmailSuccess();
    }

    @Override
    public void updatePasswordSuccess() {
        mSettingsView.updatePasswordSuccess();
    }

    @Override
    public void onFailUpdateEmail() {
        mSettingsView.onFailUpdate();
    }
}
