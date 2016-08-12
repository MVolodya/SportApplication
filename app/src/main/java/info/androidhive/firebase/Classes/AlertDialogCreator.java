package info.androidhive.firebase.Classes;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import info.androidhive.firebase.R;

public class AlertDialogCreator {

    private Context context;

    public AlertDialogCreator(Context context) {
        this.context = context;
    }


    public AlertDialog.Builder initDialogPhoto(View view, int dialogLayout) {

        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(context);

        return alertDialogBuilderUserInput;
    }


    public AlertDialog.Builder initDialogUsername(int dialogLayout) {

        LayoutInflater layoutInflaterUsername = LayoutInflater.from(context);
        final View view = layoutInflaterUsername.inflate(R.layout.dialog_username, null);

        AlertDialog.Builder alertDialogBuilderUsername = new AlertDialogCreator(context)
                .initDialogPhoto(view, R.layout.dialog_photo);
        alertDialogBuilderUsername.setView(view);



        return alertDialogBuilderUsername;
    }
}
