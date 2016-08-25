package info.androidhive.firebase.Classes;


import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.ArrayRes;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import info.androidhive.firebase.Classes.RecycleViewClasses.AllUsersAdapter;

public class RemoteDatabaseManager {

    private FirebaseStorage storage;
    private DatabaseReference mDatabase;
    private Uri downloadUrl;
    private ProgressDialogManager progressDialogManager;
    private Context context;


    public RemoteDatabaseManager(Context context) {
        this.context = context;
        storage = FirebaseStorage.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }


    public void setRateToDatabase(final String name, String matchId, final String points, String typeOfRate) {
        final RatedMatches ratedMatchesClass = RatedMatches.getInstance();
        ratedMatchesClass.setMatchId(matchId);
        ratedMatchesClass.setPoints(points);
        ratedMatchesClass.setTypeOfRate(typeOfRate);

        mDatabase.child(name).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                RatedUser ratedUser = dataSnapshot.getValue(RatedUser.class);
                ratedUser.setName(name);
                ratedUser.setCurrentPoints(String.valueOf(
                        Double.parseDouble(ratedUser.getCurrentPoints()) - Double.parseDouble(points)));

                ArrayList<RatedMatches> ratedMatches;

                if (dataSnapshot.getValue(RatedUser.class).getRatedMatches() != null) {
                    ratedMatches = dataSnapshot.getValue(RatedUser.class).getRatedMatches();
                    ratedMatches.add(ratedMatchesClass);
                    ratedUser.setRatedMatches(ratedMatches);
                    mDatabase.child(name)
                            .setValue(ratedUser);
                } else {
                    ratedMatches = new ArrayList<>();
                    ratedMatches.add(ratedMatchesClass);
                    ratedUser.setRatedMatches(ratedMatches);
                    mDatabase.child(name)
                            .setValue(ratedUser);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(context, databaseError.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void uploadImage(Bitmap bitmap, String uId, final ResponseUrl responseUrl) {

        StorageReference storageRef = storage.getReferenceFromUrl("gs://sportapp-28cf4.appspot.com");
        StorageReference mountainsRef = storageRef.child("images" + uId);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = mountainsRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                downloadUrl = taskSnapshot.getDownloadUrl();
                responseUrl.setUrl(downloadUrl.toString());
            }
        });

    }


    public void setUserData(final String displayName) {
        mDatabase.child(displayName).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    mDatabase.child(displayName).child("name").setValue(displayName);
                    mDatabase.child(displayName).child("currentPoints").setValue("100");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(context, databaseError.toString(), Toast.LENGTH_SHORT).show();
            }
        });



    }
}