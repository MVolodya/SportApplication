package info.androidhive.firebase.Classes.Managers;

import android.content.Context;
import android.net.Uri;

import info.androidhive.firebase.Classes.Models.User;
import io.realm.Realm;
import io.realm.RealmConfiguration;


public class LocalDatabaseManager {

    public Context context;

    private static Realm userRealm;


    public LocalDatabaseManager(Context contextFromActivity) {
        this.context = contextFromActivity;
        userRealm = Realm.getInstance(new RealmConfiguration.Builder(contextFromActivity)
                .name("User.realm")
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
        User result = userRealm.where(User.class)
                .findFirst();
        return result;
    }


    public static void close() {
        userRealm.close();
    }

    public static void delete(){
        userRealm.beginTransaction();
        userRealm.deleteAll();
        userRealm.commitTransaction();
        userRealm.close();
    }


}
