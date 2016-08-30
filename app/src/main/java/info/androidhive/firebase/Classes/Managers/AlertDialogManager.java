package info.androidhive.firebase.Classes.Managers;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;


public class AlertDialogManager {

    private static EditText input;

    public static AlertDialog.Builder getAlertDialog(Context context, String hint, String title){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle("Edit email");

        input = new EditText(context);
        LinearLayout.LayoutParams lpEmail = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setSelection(input.getText().length());
        input.setPadding(20, 30, 20, 30);
        input.setHint("Enter new email");
        input.setLayoutParams(lpEmail);
        alertDialog.setView(input);

        return alertDialog;
    }

    public static EditText getInput() {
        return input;
    }
}
