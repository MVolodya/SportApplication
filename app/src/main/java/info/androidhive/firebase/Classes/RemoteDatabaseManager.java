package info.androidhive.firebase.Classes;


import android.content.Context;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RemoteDatabaseManager {

    private DatabaseReference mDatabase;

    public RemoteDatabaseManager(Context context){

        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public void setUserData(String UID, String photoURL, String points){
        mDatabase.child("users").child(UID).child("photoURL").setValue(photoURL);
        mDatabase.child("users").child(UID).child("points").setValue(points);
    }


}
