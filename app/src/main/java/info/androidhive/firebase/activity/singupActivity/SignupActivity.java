package info.androidhive.firebase.activity.singupActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import info.androidhive.firebase.activity.mainActivity.MainActivity;
import info.androidhive.firebase.activity.loginActivity.LoginActivity;
import info.androidhive.firebase.activity.singupActivity.presenter.SignUpPresenter;
import info.androidhive.firebase.activity.singupActivity.view.SignUpView;
import info.androidhive.firebase.classes.managers.ProgressDialogManager;
import info.androidhive.firebase.R;

public class SignupActivity extends AppCompatActivity implements SignUpView,
        View.OnFocusChangeListener {

    private EditText etInputEmail;
    private EditText etInputPassword;
    private ProgressDialog progressDialog;
    private FirebaseAuth mAuth;
    private SignUpPresenter signUpPresenter;

    public static void start(Context context) {
        Intent starter = new Intent(context, SignupActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //Get Firebase mAuth instance
        mAuth = FirebaseAuth.getInstance();
        signUpPresenter = new SignUpPresenter(this);

        Button btnSignUp = (Button) findViewById(R.id.btn_sign_up);
        etInputEmail = (EditText) findViewById(R.id.et_email);
        etInputPassword = (EditText) findViewById(R.id.et_password);

        etInputEmail.setOnFocusChangeListener(this);
        etInputPassword.setOnFocusChangeListener(this);

        progressDialog = new ProgressDialog(this);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = etInputEmail.getText().toString().trim();
                final String password = etInputPassword.getText().toString().trim();
                ProgressDialogManager.showProgressDialog(progressDialog, getString(R.string.sign_up));
                signUpPresenter.signUp(email, password);
            }
        });
    }

    @Override
    public void onSuccess() {
        ProgressDialogManager.hideProgressDialog(progressDialog);
        MainActivity.start(this);
        finish();
        LoginActivity.sLoginActivity.finish();
    }

    @Override
    public void onFail() {
        ProgressDialogManager.hideProgressDialog(progressDialog);
        etInputEmail.setError(getString(R.string.enter_correct_email));
    }

    @Override
    public void onEmptyEmail() {
        ProgressDialogManager.hideProgressDialog(progressDialog);
        Toast.makeText(getApplicationContext(), getString(R.string.enter_email), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onEmptyPassword() {
        ProgressDialogManager.hideProgressDialog(progressDialog);
        Toast.makeText(getApplicationContext(), getString(R.string.enter_pass), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onShortPassword() {
        ProgressDialogManager.hideProgressDialog(progressDialog);
        Toast.makeText(getApplicationContext(), R.string.short_pass, Toast.LENGTH_SHORT).show();
    }

    @Override
    public Context getContext() {
        return this;
    }

    public FirebaseAuth getmAuth() {
        return mAuth;
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