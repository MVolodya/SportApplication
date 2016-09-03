package info.androidhive.firebase.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.login.widget.LoginButton;
import com.google.firebase.auth.FirebaseAuth;

import info.androidhive.firebase.Classes.Managers.LocalDatabaseManager;
import info.androidhive.firebase.Classes.Managers.ProgressDialogManager;
import info.androidhive.firebase.Classes.Managers.SignInManager;
import info.androidhive.firebase.R;


public class LoginActivity extends AppCompatActivity implements View.OnFocusChangeListener {

    private EditText inputEmail, inputPassword;

    private CallbackManager callbackManager;
    private SignInManager signInManager;

    private ProgressDialog mProgressDialog;
    private ProgressDialogManager dialogManager;

    public static Activity loginActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loginActivity = this;

        //create facebook callback
        callbackManager = CallbackManager.Factory.create();

        FirebaseAuth auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null || AccessToken.getCurrentAccessToken() != null) {
            // user auth state is changed - user is null
            // launch login activity
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }

        // set the view now
        setContentView(R.layout.activity_login);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        signInManager = new SignInManager(this, auth);
        LocalDatabaseManager localDatabaseManager = new LocalDatabaseManager(this);
        mProgressDialog = new ProgressDialog(this);

        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        Button btnSignup = (Button) findViewById(R.id.btn_signup);
        Button btnLogin = (Button) findViewById(R.id.btn_login);
        Button btnReset = (Button) findViewById(R.id.btn_reset_password);

        inputEmail.setOnFocusChangeListener(this);
        inputPassword.setOnFocusChangeListener(this);


        //[START FACEBOOK SIGNIN]
        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("email", "public_profile");
        signInManager.loginWithFacebook(loginButton, callbackManager);
        //[END FACEBOOK SIGNIN]


        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignupActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ResetPasswordActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = inputEmail.getText().toString();
                final String password = inputPassword.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                ProgressDialogManager.showProgressDialog(mProgressDialog, "Sign in");
                signInManager.loginWithEmailAndPassword(email, password);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        if (!hasFocus) {
            hideKeyboard(view);
        }
    }

    public EditText getInputPassword() {
        return inputPassword;
    }

    public ProgressDialogManager getDialogManager() {
        return dialogManager;
    }
}


