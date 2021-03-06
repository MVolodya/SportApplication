package info.androidhive.firebase.Classes;

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
        this.mProgressDialog = mProgressDialog;
    }

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(context);
            mProgressDialog.setMessage("Wait...");
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }
}
