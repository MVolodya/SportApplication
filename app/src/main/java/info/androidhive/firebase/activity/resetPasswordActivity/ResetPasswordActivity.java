package info.androidhive.firebase.activity.resetPasswordActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import info.androidhive.firebase.activity.resetPasswordActivity.presenter.ResetPasswordPresenter;
import info.androidhive.firebase.activity.resetPasswordActivity.view.ResetPasswordView;
import info.androidhive.firebase.classes.managers.ProgressDialogManager;
import info.androidhive.firebase.R;

public class ResetPasswordActivity extends AppCompatActivity implements ResetPasswordView,
        View.OnFocusChangeListener {

    private EditText inputEmail;
    private ProgressDialog progressDialog;
    private ProgressDialogManager dialogManager;
    private ResetPasswordPresenter resetPasswordPresenter;

    public static void start(Context context) {
        Intent starter = new Intent(context, ResetPasswordActivity.class);
        context.startActivity(starter);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        inputEmail = (EditText) findViewById(R.id.et_email);
        Button btnReset = (Button) findViewById(R.id.btn_reset);

        progressDialog = new ProgressDialog(this);
        resetPasswordPresenter = new ResetPasswordPresenter(this);

        inputEmail.setOnFocusChangeListener(this);
        FirebaseAuth auth = FirebaseAuth.getInstance();


        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = inputEmail.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplication(), R.string.enter_reg_email, Toast.LENGTH_SHORT).show();
                    return;
                }

                ProgressDialogManager.showProgressDialog(progressDialog, getString(R.string.sign_in));
                resetPasswordPresenter.resetPassword(email);
            }
        });
    }

    @Override
    public void onSuccess() {
        ProgressDialogManager.hideProgressDialog(progressDialog);
    }

    @Override
    public void onFail() {
        ProgressDialogManager.hideProgressDialog(progressDialog);
        inputEmail.setError(getString(R.string.enter_correct_email));
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        if (!hasFocus) {
            hideKeyboard(view);
        }
    }

    @Override
    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
