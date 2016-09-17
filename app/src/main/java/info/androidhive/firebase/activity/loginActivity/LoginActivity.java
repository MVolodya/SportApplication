package info.androidhive.firebase.activity.loginActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.login.widget.LoginButton;
import com.google.firebase.auth.FirebaseAuth;

import info.androidhive.firebase.R;
import info.androidhive.firebase.activity.loginActivity.presenter.LoginPresenter;
import info.androidhive.firebase.activity.loginActivity.view.LoginView;
import info.androidhive.firebase.activity.mainActivity.MainActivity;
import info.androidhive.firebase.activity.resetPasswordActivity.ResetPasswordActivity;
import info.androidhive.firebase.activity.singupActivity.SignupActivity;
import info.androidhive.firebase.classes.managers.ProgressDialogManager;
import info.androidhive.firebase.classes.managers.SignInManager;

public class LoginActivity extends AppCompatActivity implements View.OnFocusChangeListener,
        LoginView{

    private EditText emailEt;
    private EditText passwordEt;
    private SignInManager mSignInManager;
    private ProgressDialog mProgressDialog;
    private ProgressDialogManager mDialogManager;
    private LoginPresenter loginPresenter;
    private LoginButton facebookLoginBtn;
    private Button signUpBtn;
    private Button loginBtn;
    private Button resetBtn;

    public static Activity sLoginActivity;

    public static void start(Context context) {
        Intent starter = new Intent(context, LoginActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sLoginActivity = this;
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null || AccessToken.getCurrentAccessToken() != null) {
            // user auth state is changed - user is null
            // launch login activity
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }

        // set the view now
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        mProgressDialog = new ProgressDialog(this);
        loginPresenter = new LoginPresenter(this);
        emailEt = (EditText) findViewById(R.id.email_et);
        passwordEt = (EditText) findViewById(R.id.password_et);
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.login_progress_bar);
        signUpBtn = (Button) findViewById(R.id.sign_up_btn);
        loginBtn = (Button) findViewById(R.id.login_btn);
        resetBtn = (Button) findViewById(R.id.reset_password_btn);

        emailEt.setOnFocusChangeListener(this);
        passwordEt.setOnFocusChangeListener(this);

        //[START FACEBOOK SIGN IN]
        facebookLoginBtn = (LoginButton) findViewById(R.id.facebook_login_btn);
        facebookLoginBtn.setReadPermissions("email", "public_profile");
        facebookLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginPresenter.loginWithFacebook(facebookLoginBtn);
                ProgressDialogManager.showProgressDialog(mProgressDialog, getString(R.string.sign_in));
            }
        });
        //[END FACEBOOK SIGN IN]

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               SignupActivity.start(getContext());
            }
        });

        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ResetPasswordActivity.start(getContext());
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = emailEt.getText().toString();
                final String password = passwordEt.getText().toString();
                ProgressDialogManager.showProgressDialog(mProgressDialog, getString(R.string.sign_in));
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
    public void onSuccess() {
        ProgressDialogManager.hideProgressDialog(mProgressDialog);
        facebookLoginBtn.setEnabled(false);
        loginBtn.setEnabled(false);
        signUpBtn.setEnabled(false);
        resetBtn.setEnabled(false);
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish();
    }

    @Override
    public void onFail() {
        ProgressDialogManager.hideProgressDialog(mProgressDialog);
        SignInManager.signOut();
        Toast.makeText(LoginActivity.this, R.string.enter_correct_email, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onEmptyEmail() {
        ProgressDialogManager.hideProgressDialog(mProgressDialog);
        Toast.makeText(LoginActivity.this, R.string.enter_email, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onEmptyPassword() {
        ProgressDialogManager.hideProgressDialog(mProgressDialog);
        Toast.makeText(LoginActivity.this, R.string.enter_pass, Toast.LENGTH_SHORT).show();
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

    public EditText getInputPasswordEt() {
        return passwordEt;
    }

}


