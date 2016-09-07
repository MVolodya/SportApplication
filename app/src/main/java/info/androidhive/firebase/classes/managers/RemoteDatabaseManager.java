package info.androidhive.firebase.classes.managers;


import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
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

import info.androidhive.firebase.classes.models.RatedMatchesToDB;
import info.androidhive.firebase.classes.models.RatedUser;

public class RemoteDatabaseManager {

    private final FirebaseStorage storage;
    private final DatabaseReference mDatabase;
    private Uri downloadUrl;
    private ProgressDialogManager progressDialogManager;
    private final Context context;


    public RemoteDatabaseManager(Context context) {
        this.context = context;
        storage = FirebaseStorage.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }


    public void setRateToDatabase(final String name, String matchId, final String pointsRate,
                                  final String points,
                                  String typeOfRate) {
        final RatedMatchesToDB ratedMatchesClass = RatedMatchesToDB.getInstance();
        ratedMatchesClass.setMatchId(matchId);
        ratedMatchesClass.setPoints(pointsRate);
        ratedMatchesClass.setTypeOfRate(typeOfRate);
        ratedMatchesClass.setStatus("unchecked");

        mDatabase.child(name).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                RatedUser ratedUser = dataSnapshot.getValue(RatedUser.class);
                ratedUser.setName(name);
                ratedUser.setCurrentPoints(String.valueOf(
                        Double.parseDouble(ratedUser.getCurrentPoints()) - Double.parseDouble(points)));

                List<RatedMatchesToDB> ratedMatches;

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
                responseUrl.setUrl(downloadUrl != null ? downloadUrl.toString() : null);
            }
        });

    }


    public void setUserData(final String displayName, final String photoUri) {
        mDatabase.child(displayName).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    mDatabase.child(displayName).child("name").setValue(displayName);
                    mDatabase.child(displayName).child("currentPoints").setValue("100");
                    mDatabase.child(displayName).child("photoUrl").setValue(photoUri);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(context, databaseError.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void updatePhotoUrl(final String displayName, final String photoUri) {
        mDatabase.child(displayName).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    mDatabase.child(displayName).child("photoUrl").setValue(photoUri);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(context, databaseError.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void updateUsername(final String displayName, final String newName) {
        mDatabase.child(displayName).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    RatedUser ratedUser = dataSnapshot.getValue(RatedUser.class);
                    RatedUser newRatedUser = new RatedUser();

                    newRatedUser.setName(newName);
                    newRatedUser.setCurrentPoints(ratedUser.getCurrentPoints());
                    newRatedUser.setPhotoUrl(ratedUser.getPhotoUrl());
                    newRatedUser.setRatedMatches(ratedUser.getRatedMatches());

                    mDatabase.child(newName).setValue(newRatedUser);
                    mDatabase.child(displayName).removeValue();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(context, databaseError.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
