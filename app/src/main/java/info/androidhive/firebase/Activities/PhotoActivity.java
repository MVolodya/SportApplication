package info.androidhive.firebase.Activities;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;

import info.androidhive.firebase.Classes.Managers.LocalDatabaseManager;
import info.androidhive.firebase.Classes.Managers.ProgressDialogManager;
import info.androidhive.firebase.Classes.Managers.RemoteDatabaseManager;
import info.androidhive.firebase.Classes.Managers.ResponseUrl;
import info.androidhive.firebase.Classes.Managers.UserManager;
import info.androidhive.firebase.Classes.Models.User;
import info.androidhive.firebase.R;

public class PhotoActivity extends MainActivity implements ResponseUrl {

    public static final int PICK_IMAGE_REQUEST = 1;
    public static final int CAMERA_REQUEST = 2;

    private ImageView userPhoto;
    private User user;

    private FirebaseUser firebaseUser;
    private ProgressDialog mProgressDialog;
    private RemoteDatabaseManager remoteDatabaseManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        userPhoto = (ImageView)findViewById(R.id.imageViewPhoto);
        mProgressDialog = new ProgressDialog(this);
        remoteDatabaseManager = new RemoteDatabaseManager(this);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    }

//    public AlertDialog.Builder getPhotoAlertDialogFromClass(){
//
//        AlertDialog.Builder alertDialogBuilderPhoto = new AlertDialog.Builder(this);
//        alertDialogBuilderPhoto.setView(view);
//
//        final AlertDialog alertDialogFAB = alertDialogBuilderPhoto.create();
//
//        Button buttonGallery = (Button) view.findViewById(R.id.buttonGallery);
//        buttonGallery.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent();
//                intent.setType("image/*");
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                startActivityForResult(Intent.createChooser(intent, "Select Picture"), SettingsFragment.PICK_IMAGE_REQUEST);
//                alertDialogFAB.dismiss();
//                alertDialogFAB.hide();
//            }
//        });
//        Button buttonCamera = (Button) view.findViewById(R.id.buttonCamera);
//        buttonCamera.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//                startActivityForResult(cameraIntent, SettingsFragment.CAMERA_REQUEST);
//                alertDialogFAB.dismiss();
//                alertDialogFAB.hide();
//            }
//        });
//
//        return alertDialogBuilderPhoto;
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                ProgressDialogManager.showProgressDialog(mProgressDialog, "Wait, while loading photo!");
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                new RemoteDatabaseManager(this).uploadImage(bitmap, firebaseUser.getUid(), this);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            ProgressDialogManager.showProgressDialog(mProgressDialog, "Wait, while loading photo!");
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            new RemoteDatabaseManager(this).uploadImage(photo, firebaseUser.getUid(), this);
        }
    }



    @Override
    public void setUrl(String url) {
        UserManager.updateUrl(url);
        LocalDatabaseManager.updateUrl(url);
        remoteDatabaseManager.updatePhotoUrl(firebaseUser.getDisplayName(), url);
        Glide.with(this)
                .load(user.getPhotoURL())
                .into(userPhoto);
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            ProgressDialogManager.hideProgressDialog(mProgressDialog);
        }
    }
}
