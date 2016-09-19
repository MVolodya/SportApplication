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

        final TextView pointsTv = (TextView) promptView
                .findViewById(R.id.user_points_tv);
        final EditText inputTv = (EditText) promptView
                .findViewById(R.id.category_et);
        final TextView sumTv = (TextView) promptView
                .findViewById(R.id.sum_tv);
        final TextView typeTv = (TextView) promptView
                .findViewById(R.id.type_tv);

        typeTv.setText(typeOfRate);
        pointsTv.setText(user.getCurrentPoints());

        inputTv.requestFocus();
        inputTv.setHint(R.string.points);
        inputTv.setTextColor(Color.BLACK);
        inputTv.setInputType(InputType.TYPE_CLASS_NUMBER);
        inputTv.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!inputTv.getText().toString().isEmpty()) {
                    double currentSum = (coff * Integer.parseInt(inputTv.getText().toString()));
                    sumTv.setText(String.format("%.1f", currentSum));
                    sumTv.setText(String.valueOf(format.format(currentSum)));
                } else {
                    sumTv.setText("0");
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
                        if (inputTv.getText().toString().isEmpty()) {
                            inputTv.setError(context.getString(R.string.enter_rate));
                        } else {
                            double enterPoints = Double.parseDouble(inputTv.getText().toString());
                            double userPoints = Double.parseDouble(user.getCurrentPoints());
                            if (userPoints - enterPoints >= 0) {
                                new RateManager(context).setRate(userFB != null ? userFB.getDisplayName() : null,
                                        String.valueOf(matchId),String.valueOf(format.format(
                                                sumTv.getText().toString()
                                        )),
                                        inputTv.getText().toString(),
                                        typeOfRate);
                                dialog.hide();
                            }else {
                                    inputTv.setError(context.getString(R.string.enough_points));
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
