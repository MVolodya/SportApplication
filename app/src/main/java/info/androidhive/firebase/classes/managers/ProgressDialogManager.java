package info.androidhive.firebase.classes.managers;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;


/**
 * Created by andri on 26.07.2016.
 */
public class ProgressDialogManager {

    public static void showProgressDialog(@NonNull ProgressDialog mProgressDialog, String msg) {
            mProgressDialog.setMessage(msg);
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.setCancelable(false);
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.show();
    }

    public static void hideProgressDialog(@NonNull ProgressDialog mProgressDialog) {
            mProgressDialog.hide();
            mProgressDialog.dismiss();
    }
}
