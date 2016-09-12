package info.androidhive.firebase.classes.managers;

import android.content.Context;
import android.net.Uri;

import info.androidhive.firebase.classes.models.User;
import io.realm.Realm;
import io.realm.RealmConfiguration;


public class LocalDatabaseManager {

    private static Realm userRealm;


    public LocalDatabaseManager(Context contextFromActivity) {
        Context context = contextFromActivity;
        userRealm = Realm.getInstance(new RealmConfiguration.Builder(contextFromActivity)
                .build());
    }

    public static void setUser(String name, String email, Uri photoUrl) {

        userRealm.beginTransaction();
        User user = userRealm.createObject(User.class);

        user.setName(name);
        user.setEmail(email);

        if(photoUrl != null)
        user.setPhotoURL(photoUrl.toString());
        else user.setPhotoURL(null);

        userRealm.commitTransaction();
    }

    public static void updateName(String name){
        userRealm.beginTransaction();
        User user = getUser();

        user.setName(name);

        userRealm.commitTransaction();
    }

    public static void updateEmail(String email){
        userRealm.beginTransaction();
        User user = getUser();

        user.setEmail(email);

        userRealm.commitTransaction();
    }

    public static void updateUrl(String url){
        userRealm.beginTransaction();
        User user = getUser();

        user.setPhotoURL(url);

        userRealm.commitTransaction();
    }

    public static User getUser() {
        return userRealm.where(User.class)
                .findFirst();
    }


    public static void close() {
        userRealm.close();
    }

    public static void delete(){
        userRealm.beginTransaction();
        userRealm.delete(getUser().getClass());
        userRealm.commitTransaction();
    }


}
