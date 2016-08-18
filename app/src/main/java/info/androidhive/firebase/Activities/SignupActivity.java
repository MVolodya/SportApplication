package info.androidhive.firebase.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import info.androidhive.firebase.Classes.LocalDatabaseManager;
import info.androidhive.firebase.Classes.ProgressDialogManager;
import info.androidhive.firebase.Classes.RemoteDatabaseManager;
import info.androidhive.firebase.Classes.UserManager;
import info.androidhive.firebase.R;

public class SignupActivity extends AppCompatActivity {

    private EditText inputEmail, inputPassword;
    private Button btnSignIn, btnSignUp, btnResetPassword;
    private ProgressDialog progressDialog;
    private FirebaseAuth auth;
    private ProgressDialogManager dialogManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        final LocalDatabaseManager localDatabaseManager = new LocalDatabaseManager(this);

        btnSignIn = (Button) findViewById(R.id.sign_in_button);
        btnSignUp = (Button) findViewById(R.id.sign_up_button);
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        btnResetPassword = (Button) findViewById(R.id.btn_reset_password);

        dialogManager = new ProgressDialogManager(this, progressDialog);

        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignupActivity.this, ResetPasswordActivity.class));
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                finish();
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.length() < 6) {
                    Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                    return;
                }

                dialogManager.showProgressDialog();
                //create user
                auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Toast.makeText(SignupActivity.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                                dialogManager.hideProgressDialog();
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                if (!task.isSuccessful()) {
                                    Toast.makeText(SignupActivity.this, "Authentication failed." + task.getException(),
                                            Toast.LENGTH_SHORT).show();
                                } else {

                                    long currentTime = System.currentTimeMillis();

                                    if(auth.getCurrentUser().getDisplayName() != null) {

                                        localDatabaseManager.setUser(auth.getCurrentUser().getDisplayName(),
                                                auth.getCurrentUser().getEmail(),
                                                auth.getCurrentUser().getPhotoUrl());

                                        new RemoteDatabaseManager(getApplicationContext())
                                                .setUserData(auth.getCurrentUser().getDisplayName());
                                    } else {

                                        localDatabaseManager.setUser("Anonymous"+currentTime,
                                                auth.getCurrentUser().getEmail(),
                                                auth.getCurrentUser().getPhotoUrl());

                                        UserManager.updateUsername("Anonymous"+currentTime);

                                        new RemoteDatabaseManager(getApplicationContext())
                                                .setUserData("Anonymous"+currentTime);
                                    }

                                    startActivity(new Intent(SignupActivity.this, MainActivity.class));
                                    finish();
                                    LoginActivity.loginActivity.finish();
                                }
                            }
                        });

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        dialogManager.hideProgressDialog();
    }
}