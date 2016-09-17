package info.androidhive.firebase.classes.managers;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import de.hdodenhof.circleimageview.CircleImageView;
import info.androidhive.firebase.R;
import info.androidhive.firebase.classes.models.DataHelper;
import info.androidhive.firebase.fragments.settingsFragment.SettingsFragment;


public class AlertDialogManager {

    private static EditText sInputEt;

    public static AlertDialog.Builder getAlertDialog(Context context, int resource) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(resource, null);
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setView(view);
        sInputEt = (EditText)view.findViewById(R.id.user_input_et);
        return alertDialog;
    }

    public static AlertDialog.Builder getPhotoAlertDialog(final Context context,
                                                          final SettingsFragment settingsFragment) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.dialog_photo, null);

        AlertDialog.Builder alertDialogBuilderPhoto = new AlertDialog.Builder(context);
        alertDialogBuilderPhoto.setView(view);

        final AlertDialog alertDialogFAB = alertDialogBuilderPhoto.create();

        Button galleryBtn = (Button) view.findViewById(R.id.gallery_btn);
        galleryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                settingsFragment.startActivityForResult(Intent.createChooser(intent, context.getString(R.string.select_picture)), SettingsFragment.PICK_IMAGE_REQUEST);
                alertDialogFAB.hide();
            }
        });
        Button cameraBtn = (Button) view.findViewById(R.id.camera_btn);
        cameraBtn.setOnClickListener(new View.OnClickListener() {
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
        final AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setView(promptView);
        alert.setPositiveButton(R.string.save, null);
        alert.setNegativeButton(R.string.cancel, null);

        final AlertDialog dialog = alert.create();
        dialog.show();
        final CircleImageView enIv = (CircleImageView)promptView.findViewById(R.id.english_iv);
        final CircleImageView ukIv = (CircleImageView)promptView.findViewById(R.id.ukrainian_iv);
        dialog.getButton(dialog.BUTTON1).setEnabled(false);
        enIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.getButton(dialog.BUTTON1).setEnabled(true);
                enIv.setBorderColor(Color.parseColor("#ff6861"));
                ukIv.setBorderColor(Color.parseColor("#ffffff"));
                DataHelper.getInstance().setLanguage("en");
            }
        });

        ukIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.getButton(dialog.BUTTON1).setEnabled(true);
                ukIv.setBorderColor(Color.parseColor("#ff6861"));
                enIv.setBorderColor(Color.parseColor("#ffffff"));
                DataHelper.getInstance().setLanguage("uk");
            }
        });
        return dialog;
    }

    public static EditText getsEtInput() {
        return sInputEt;
    }
}
