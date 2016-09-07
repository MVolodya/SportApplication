package info.androidhive.firebase.classes.managers;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import info.androidhive.firebase.fragments.SettingsFragment;
import info.androidhive.firebase.R;


public class AlertDialogManager {

    private static EditText input;

    public static AlertDialog.Builder getAlertDialog(Context context, String msg){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle("Edit");

        input = new EditText(context);
        LinearLayout.LayoutParams lpEmail = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setSelection(input.getText().length());
        input.setPadding(20, 30, 20, 30);
        input.setHint(msg);
        input.setLayoutParams(lpEmail);
        alertDialog.setView(input);

        return alertDialog;
    }

    public static AlertDialog.Builder getPhotoAlertDialog(final Context context,
                                                          final SettingsFragment settingsFragment){
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.dialog_photo, null);

        AlertDialog.Builder alertDialogBuilderPhoto = new AlertDialog.Builder(context);
        alertDialogBuilderPhoto.setView(view);

        final AlertDialog alertDialogFAB = alertDialogBuilderPhoto.create();

        Button buttonGallery = (Button) view.findViewById(R.id.buttonGallery);
        buttonGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                settingsFragment.startActivityForResult(Intent.createChooser(intent, "Select Picture"), SettingsFragment.PICK_IMAGE_REQUEST);
                alertDialogFAB.dismiss();
                alertDialogFAB.hide();
            }
        });
        Button buttonCamera = (Button) view.findViewById(R.id.buttonCamera);
        buttonCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                settingsFragment.startActivityForResult(cameraIntent, SettingsFragment.CAMERA_REQUEST);
                alertDialogFAB.dismiss();
                alertDialogFAB.hide();
            }
        });

        return alertDialogBuilderPhoto;
    }

    public static EditText getInput() {
        return input;
    }
}
