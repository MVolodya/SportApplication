package info.androidhive.firebase.Classes.Managers;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.NonNull;


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
