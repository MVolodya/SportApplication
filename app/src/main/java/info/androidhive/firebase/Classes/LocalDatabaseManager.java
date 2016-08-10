package info.androidhive.firebase.Classes;

import android.content.Context;

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

    public void setUser(String name, String email, String photoUrl) {

        userRealm.beginTransaction();
        User user = userRealm.createObject(User.class);

        user.setName(name);
        user.setEmail(email);
        user.setPhotoURL(photoUrl);

        userRealm.commitTransaction();
    }

    public User getUser() {

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
