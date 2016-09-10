package info.androidhive.firebase.classes.managers;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import java.util.Locale;

import info.androidhive.firebase.fragments.settingsFragment.SettingsFragment;
import info.androidhive.firebase.R;


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

    public static AlertDialog.Builder getLanguageAlertDialog(final Context context) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.dialog_language, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setView(view);

        final AlertDialog alertDialog = alertDialogBuilder.create();

        RadioButton rbEnglish = (RadioButton) view.findViewById(R.id.radioButtonEnglish);
        RadioButton rbUkrainian = (RadioButton) view.findViewById(R.id.radioButtonUK);

        rbEnglish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Locale locale = new Locale("en");
                Locale.setDefault(locale);
                Configuration config = new Configuration();
                config.locale = locale;
                context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
                Intent i = context.getPackageManager()
                        .getLaunchIntentForPackage( context.getPackageName() );
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(i);
            }
        });

        rbUkrainian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Locale locale = new Locale("uk");
                Locale.setDefault(locale);
                Configuration config = new Configuration();
                config.locale = locale;
                Intent i = context.getPackageManager()
                        .getLaunchIntentForPackage( context.getPackageName() );
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(i);
            }
        });

        return alertDialogBuilder;
    }

    public static EditText getInput() {
        return input;
    }
}
