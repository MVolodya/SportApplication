package info.androidhive.firebase.Classes;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.View;

public class AlertDialogCreator {

    private Context context;

    public AlertDialogCreator (Context context){
        this.context = context;
    }

    public AlertDialog.Builder initDialogPhoto(View view, int dialogLayout){

        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(context);

        return alertDialogBuilderUserInput;
    }
}
