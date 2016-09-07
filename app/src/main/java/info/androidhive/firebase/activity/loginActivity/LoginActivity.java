package info.androidhive.firebase.activity.loginActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
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
import com.facebook.login.widget.LoginButton;
import com.google.firebase.auth.FirebaseAuth;

import info.androidhive.firebase.activity.mainActivity.MainActivity;
import info.androidhive.firebase.activity.resetPasswordActivity.ResetPasswordActivity;
import info.androidhive.firebase.activity.singupActivity.SignupActivity;
import info.androidhive.firebase.activity.loginActivity.presenter.LoginPresenter;
import info.androidhive.firebase.activity.loginActivity.view.LoginView;
import info.androidhive.firebase.classes.managers.ProgressDialogManager;
import info.androidhive.firebase.classes.managers.SignInManager;
import info.androidhive.firebase.R;


public class LoginActivity extends AppCompatActivity implements View.OnFocusChangeListener,
        LoginView{

    private EditText inputEmail, inputPassword;
    private SignInManager signInManager;
    private ProgressDialog mProgressDialog;
    private ProgressDialogManager dialogManager;
    private LoginPresenter loginPresenter;

    public static Activity loginActivity;

    public static void start(Context context) {
        Intent starter = new Intent(context, LoginActivity.class);
        context.startActivity(starter);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loginActivity = this;
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

        mProgressDialog = new ProgressDialog(this);
        loginPresenter = new LoginPresenter(this);
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);

        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        Button btnSignup = (Button) findViewById(R.id.btn_signup);
        Button btnLogin = (Button) findViewById(R.id.btn_login);
        Button btnReset = (Button) findViewById(R.id.btn_reset_password);

        inputEmail.setOnFocusChangeListener(this);
        inputPassword.setOnFocusChangeListener(this);

        //[START FACEBOOK SIGNIN]
        final LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("email", "public_profile");
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginPresenter.loginWithFacebook(loginButton);
                ProgressDialogManager.showProgressDialog(mProgressDialog, "Sign in");
            }
        });
        //[END FACEBOOK SIGNIN]

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               SignupActivity.start(getContext());
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ResetPasswordActivity.start(getContext());
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
                loginPresenter.login(email, password);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        loginPresenter.getCallbackManager().onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void loginOk() {
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        ProgressDialogManager.hideProgressDialog(mProgressDialog);
        finish();
    }



    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public FirebaseAuth getAuth() {
        return FirebaseAuth.getInstance();
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

    public EditText getInputPassword() {
        return inputPassword;
    }

}


