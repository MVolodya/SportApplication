package info.androidhive.firebase.activity.singupActivity;

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

import info.androidhive.firebase.activity.mainActivity.MainActivity;
import info.androidhive.firebase.activity.loginActivity.LoginActivity;
import info.androidhive.firebase.activity.singupActivity.presenter.SignUpPresenter;
import info.androidhive.firebase.activity.singupActivity.view.SignUpView;
import info.androidhive.firebase.classes.managers.ProgressDialogManager;
import info.androidhive.firebase.R;

public class SignupActivity extends AppCompatActivity implements SignUpView,
View.OnFocusChangeListener{

    private EditText inputEmail, inputPassword;
    private ProgressDialog progressDialog;
    private FirebaseAuth auth;
    private SignUpPresenter signUpPresenter;

    public static void start(Context context) {
        Intent starter = new Intent(context, SignupActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();
        signUpPresenter = new SignUpPresenter(this);


        Button btnSignUp = (Button) findViewById(R.id.sign_up_button);
        inputEmail = (EditText) findViewById(R.id.et_email);
        inputPassword = (EditText) findViewById(R.id.et_password);


        inputEmail.setOnFocusChangeListener(this);
        inputPassword.setOnFocusChangeListener(this);

        progressDialog = new ProgressDialog(this);




        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String email = inputEmail.getText().toString().trim();
                final String password = inputPassword.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), getString(R.string.enter_email), Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), getString(R.string.enter_pass), Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.length() < 6) {
                    Toast.makeText(getApplicationContext(), R.string.short_pass, Toast.LENGTH_SHORT).show();
                    return;
                }

                ProgressDialogManager.showProgressDialog(progressDialog,getString(R.string.sign_up));
                signUpPresenter.login(email, password);
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
        inputEmail.setError(getString(R.string.enter_correct_email));
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public FirebaseAuth getAuth() {
        return auth;
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