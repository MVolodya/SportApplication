package info.androidhive.firebase.Classes.Managers;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import info.androidhive.firebase.Activities.SettingsActivity;
import info.androidhive.firebase.R;


public class AlertDialogManager {

    private static EditText input;

    public static AlertDialog.Builder getAlertDialog(Context context, String hint, String title) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle(R.string.edit_email);

        input = new EditText(context);
        LinearLayout.LayoutParams lpEmail = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setSelection(input.getText().length());
        input.setPadding(20, 30, 20, 30);
        input.setHint(R.string.enter_new_email);
        input.setLayoutParams(lpEmail);
        alertDialog.setView(input);

        return alertDialog;
    }

    public static AlertDialog.Builder getPhotoAlertDialog(final Context context) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.dialog_photo, null);

        AlertDialog.Builder alertDialogBuilderPhoto = new AlertDialog.Builder(context);
        alertDialogBuilderPhoto.setView(view);

        final AlertDialog alertDialogFAB = alertDialogBuilderPhoto.create();
        //alertDialogFAB.show();

        Button buttonGallery = (Button) view.findViewById(R.id.buttonGallery);
        buttonGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                ((SettingsActivity) context).startActivityForResult(Intent.createChooser(intent, "Select Picture"), SettingsActivity.PICK_IMAGE_REQUEST);
                alertDialogFAB.dismiss();
                alertDialogFAB.hide();
            }
        });
        Button buttonCamera = (Button) view.findViewById(R.id.buttonCamera);
        buttonCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                ((SettingsActivity) context).startActivityForResult(cameraIntent, SettingsActivity.CAMERA_REQUEST);
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
