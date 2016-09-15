package info.androidhive.firebase.classes.managers;

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

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import info.androidhive.firebase.R;
import info.androidhive.firebase.classes.models.RatedUser;
import info.androidhive.firebase.classes.models.User;


public class MaterialDialogManager {

    private final Context context;

    public MaterialDialogManager(Context context, View view) {
        this.context = context;
    }

    public AlertDialog openRateDialogBox(final double coff, final int matchId, final String typeOfRate,
                                         final RatedUser user) {

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View promptView = layoutInflater.inflate(R.layout.dialog_rate, null);
        final AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle(R.string.enter_points);
        alert.setView(promptView);
        alert.setPositiveButton(R.string.set_rate, null);
        alert.setNegativeButton(R.string.cancel, null);

        final DecimalFormat format = new DecimalFormat("#0.0", new DecimalFormatSymbols(Locale.US));
        format.setDecimalSeparatorAlwaysShown(false);

        final TextView points = (TextView) promptView
                .findViewById(R.id.textViewUserPoints);
        final EditText input = (EditText) promptView
                .findViewById(R.id.etCategory);
        final TextView sum = (TextView) promptView
                .findViewById(R.id.textViewSum);
        final TextView type = (TextView) promptView
                .findViewById(R.id.textViewType);

        type.setText(typeOfRate);
        points.setText(user.getCurrentPoints());

        input.requestFocus();
        input.setHint(R.string.points);
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
                    sum.setText(String.valueOf(format.format(currentSum)));
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
                final FirebaseUser userFB = FirebaseAuth.getInstance().getCurrentUser();

                Button b = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        if (input.getText().toString().isEmpty()) {
                            input.setError(context.getString(R.string.enter_rate));
                        } else {
                            double enterPoints = Double.parseDouble(input.getText().toString());
                            double userPoints = Double.parseDouble(user.getCurrentPoints());
                            if (userPoints - enterPoints >= 0) {
                                new RateManager(context).setRate(userFB != null ? userFB.getDisplayName() : null,
                                        String.valueOf(matchId), sum.getText().toString(),
                                        input.getText().toString(),
                                        typeOfRate);
                                dialog.hide();
                            }else {
                                input.setError(context.getString(R.string.enough_points));
                            }
                        }
                    }
                });
            }
        });

        alert.setNegativeButton(context.getString(R.string.cancel),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {}
                });

        return dialog;
    }

}
