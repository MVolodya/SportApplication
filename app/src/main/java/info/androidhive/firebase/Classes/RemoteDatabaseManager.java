package info.androidhive.firebase.Classes;


import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.ArrayRes;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class RemoteDatabaseManager {

    private FirebaseStorage storage;
    private DatabaseReference mDatabase;
    private Uri downloadUrl;
    private ProgressDialogManager progressDialogManager;
    private Context context;
    private ArrayList<RatedMatches> ratedMatches = new ArrayList<>();;

    public RemoteDatabaseManager(Context context) {
        this.context = context;
        storage = FirebaseStorage.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        ratedMatches = new ArrayList<>();
    }

    public void setUserData(String name) {
//        mDatabase.child(name)
//                .child("currentPoints")
//                .setValue("100");
    }

    public void setRateToDatabase(String name, String matchId, String points, String typeOfRate) {

        long currentTime = System.currentTimeMillis();
        RatedMatches ratedMatchesClass = RatedMatches.getInstance();
        ratedMatchesClass.setMatchId(matchId);
        ratedMatchesClass.setPoints(points);
        ratedMatchesClass.setTypeOfRate(typeOfRate);

        ratedMatches.add(ratedMatchesClass);

        mDatabase.child(name)
                .setValue(new RatedUser(name, String.valueOf(100-Double.parseDouble(points)), ratedMatches));
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
            public void onFailure(@NonNull Exception exception) {}
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                downloadUrl = taskSnapshot.getDownloadUrl();
                responseUrl.setUrl(downloadUrl.toString());

            }
        });

    }


}
