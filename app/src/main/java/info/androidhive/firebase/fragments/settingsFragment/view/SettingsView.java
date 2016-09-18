package info.androidhive.firebase.fragments.settingsFragment.view;

import android.widget.EditText;

import info.androidhive.firebase.classes.managers.RemoteDatabaseManager;

public interface SettingsView {
    RemoteDatabaseManager getRemoteDatabaseManager();
    void updatePhotoSuccess(String url);
    void updateUsernameSuccess();
    void updateEmailSuccess();
    void onValidEmail();
    void updatePasswordSuccess();
    void changeLanguageSuccess(String lan);
    void onEmptyField(EditText editText);
}
