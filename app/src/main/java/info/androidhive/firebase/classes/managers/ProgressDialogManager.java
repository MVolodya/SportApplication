package info.androidhive.firebase.classes.managers;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;

public class ProgressDialogManager {

    public static void showProgressDialog(@NonNull ProgressDialog mProgressDialog,@NonNull String msg) {
            mProgressDialog.setMessage(msg);
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.setCancelable(false);
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.show();
    }

    public static void hideProgressDialog(@NonNull ProgressDialog mProgressDialog) {
            mProgressDialog.dismiss();
    }
}
