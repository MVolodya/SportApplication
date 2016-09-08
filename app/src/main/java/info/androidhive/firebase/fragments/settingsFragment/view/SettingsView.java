package info.androidhive.firebase.fragments.settingsFragment.view;

import android.app.Activity;

import info.androidhive.firebase.classes.managers.RemoteDatabaseManager;

public interface SettingsView {
    RemoteDatabaseManager getRemoteDatabaseManager();
    void updatePhotoSuccess(String url);
    void updateUsernameSuccess();
    void updateEmailSuccess();
    void updatePasswordSuccess();
}
