package info.androidhive.firebase.Classes.Managers;

import android.app.ProgressDialog;
import android.content.Context;


/**
 * Created by andri on 26.07.2016.
 */
public class ProgressDialogManager {

    private ProgressDialog mProgressDialog;
    private Context context;

    public ProgressDialogManager(Context context, ProgressDialog mProgressDialog){
        this.context = context;
    }

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(context);
            mProgressDialog.setMessage("Wait...");
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.setCancelable(false);
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.show();
        }
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
            mProgressDialog.dismiss();
        }
    }
}
