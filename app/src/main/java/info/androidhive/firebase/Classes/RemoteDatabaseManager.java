package info.androidhive.firebase.Classes;


import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

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

    public void setUserData(String UID, String photoURL, String points) {
        mDatabase.child("users").child(UID).child("photoURL").setValue(photoURL);
        mDatabase.child("users").child(UID).child("points").setValue(points);
    }

    public void uploadImage(Bitmap bitmap, final ResponseUrl responseUrl) {

//        ProgressDialog progressDialog = new ProgressDialog(context);
//        ProgressDialogManager dialogManager = new ProgressDialogManager(context,progressDialog);
//        progressDialogManager.showProgressDialog();

        StorageReference storageRef = storage.getReferenceFromUrl("gs://sportapp-28cf4.appspot.com");
        StorageReference mountainsRef = storageRef.child("images");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = mountainsRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
               // progressDialogManager.hideProgressDialog();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                // progressDialogManager.hideProgressDialog();
                downloadUrl = taskSnapshot.getDownloadUrl();
                responseUrl.setUrl(downloadUrl.toString());
                //progressDialogManager.hideProgressDialog();
            }
        });
       // return downloadUrl.toString();
    }


}
