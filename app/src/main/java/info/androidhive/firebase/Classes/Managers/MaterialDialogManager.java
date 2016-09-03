package info.androidhive.firebase.Classes.Managers;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import info.androidhive.firebase.R;


public class MaterialDialogManager {

    private Context context;
    private View view;

    public MaterialDialogManager(Context context, View view) {
        this.context = context;
        this.view = view;
    }

    public AlertDialog openDialogBox(final double coff, final int matchId, final String typeOfRate) {

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View promptView = layoutInflater.inflate(R.layout.dialog_rate, null);
        final AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle("Enter points...");
        alert.setView(promptView);
        alert.setPositiveButton("Set rate", null);
        alert.setNegativeButton("Cancel", null);

        final EditText input = (EditText) promptView
                .findViewById(R.id.etCategory);
        final TextView sum = (TextView) promptView
                .findViewById(R.id.textViewSum);
        final TextView type = (TextView) promptView
                .findViewById(R.id.textViewType);

        type.setText(typeOfRate);

        input.requestFocus();
        input.setHint("Points...");
        input.setTextColor(Color.BLACK);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);

        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!input.getText().toString().isEmpty()) {
                    double currentSum = (coff * Integer.parseInt(input.getText().toString()));
                    sum.setText(String.format("%.1f", currentSum));
                } else {
                    sum.setText("0");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        final AlertDialog dialog = alert.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                Button b = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        if (input.getText().toString().isEmpty()) {
                            input.setError("Enter rate!");
                        } else {
                            new RateManager(context).setRate(user.getDisplayName(),
                                    String.valueOf(matchId) ,sum.getText().toString(),
                                    input.getText().toString(),
                                    typeOfRate);
                            dialog.hide();
                        }
                    }
                });
            }
        });

        alert.setNegativeButton("CANCEL",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                });

        return dialog;
    }

}
