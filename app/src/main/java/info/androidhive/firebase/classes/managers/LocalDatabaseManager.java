package info.androidhive.firebase.classes.managers;

import android.content.Context;
import android.net.Uri;

import info.androidhive.firebase.classes.models.User;
import io.realm.Realm;
import io.realm.RealmConfiguration;

public class LocalDatabaseManager {

    private static Realm sUserRealm;

    public LocalDatabaseManager(Context contextFromActivity) {
        sUserRealm = Realm.getInstance(new RealmConfiguration.Builder(contextFromActivity)
                .build());
    }

    public static void setUser(String name, String email, Uri photoUrl) {
        sUserRealm.beginTransaction();
        User user = sUserRealm.createObject(User.class);
        user.setName(name);
        user.setEmail(email);
        if(photoUrl != null)
        user.setPhotoURL(photoUrl.toString());
        else user.setPhotoURL(null);
        sUserRealm.commitTransaction();
    }

    public static void updateName(String name){
        sUserRealm.beginTransaction();
        User user = getUser();
        user.setName(name);
        sUserRealm.commitTransaction();
    }

    public static void updateEmail(String email){
        sUserRealm.beginTransaction();
        User user = getUser();
        user.setEmail(email);
        sUserRealm.commitTransaction();
    }

    public static void updateUrl(String url){
        sUserRealm.beginTransaction();
        User user = getUser();
        user.setPhotoURL(url);
        sUserRealm.commitTransaction();
    }

    public static User getUser() {
        return sUserRealm.where(User.class)
                .findFirst();
    }

    public static void close() {
        sUserRealm.close();
    }

    public static void delete(){
        sUserRealm.beginTransaction();
        sUserRealm.delete(getUser().getClass());
        sUserRealm.commitTransaction();
    }


}
