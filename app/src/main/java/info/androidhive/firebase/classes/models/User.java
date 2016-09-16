package info.androidhive.firebase.classes.models;

import io.realm.RealmObject;

public class User extends RealmObject {

    private String mName;
    private String mEmail;
    private String mPhotoURL;

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String mEmail) {
        this.mEmail = mEmail;
    }

    public String getPhotoURL() {
        return mPhotoURL;
    }

    public void setPhotoURL(String mPhotoURL) {
        this.mPhotoURL = mPhotoURL;
    }
}