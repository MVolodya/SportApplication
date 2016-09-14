package info.androidhive.firebase.classes.managers;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import de.hdodenhof.circleimageview.CircleImageView;
import info.androidhive.firebase.R;
import info.androidhive.firebase.classes.models.DataHelper;
import info.androidhive.firebase.fragments.settingsFragment.SettingsFragment;


public class AlertDialogManager {

    private static EditText input;

    public static AlertDialog.Builder getAlertDialog(Context context, String msg) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle(R.string.edit);

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
                                                          final SettingsFragment settingsFragment) {
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
                settingsFragment.startActivityForResult(Intent.createChooser(intent, context.getString(R.string.select_picture)), SettingsFragment.PICK_IMAGE_REQUEST);
                alertDialogFAB.hide();
            }
        });
        Button buttonCamera = (Button) view.findViewById(R.id.buttonCamera);
        buttonCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                settingsFragment.startActivityForResult(cameraIntent, SettingsFragment.CAMERA_REQUEST);
                alertDialogFAB.hide();
            }
        });

        return alertDialogBuilderPhoto;
    }

    public static AlertDialog getLanguageAlertDialog(final Context context) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View promptView = layoutInflater.inflate(R.layout.dialog_language, null);
        final android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(context);
        alert.setView(promptView);
        alert.setPositiveButton(R.string.save, null);
        alert.setNegativeButton(R.string.cancel, null);

        final AlertDialog dialog = alert.create();
        final CircleImageView en = (CircleImageView)promptView.findViewById(R.id.imgEnglish);
        final CircleImageView uk = (CircleImageView)promptView.findViewById(R.id.imgUkrainian);

        en.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                en.setBorderColor(Color.parseColor("#ff6861"));
                uk.setBorderColor(Color.parseColor("#ffffff"));
                DataHelper.getInstance().setLanguage("en");
            }
        });

        uk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uk.setBorderColor(Color.parseColor("#ff6861"));
                en.setBorderColor(Color.parseColor("#ffffff"));
                DataHelper.getInstance().setLanguage("uk");
            }
        });
        return dialog;
    }

    public static EditText getInput() {
        return input;
    }
}
