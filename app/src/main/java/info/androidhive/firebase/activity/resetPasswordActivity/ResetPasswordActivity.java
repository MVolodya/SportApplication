package info.androidhive.firebase.activity.resetPasswordActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import info.androidhive.firebase.activity.resetPasswordActivity.presenter.ResetPasswordPresenter;
import info.androidhive.firebase.activity.resetPasswordActivity.view.ResetPasswordView;
import info.androidhive.firebase.classes.managers.ProgressDialogManager;
import info.androidhive.firebase.R;

public class ResetPasswordActivity extends AppCompatActivity implements ResetPasswordView {

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

        inputEmail = (EditText) findViewById(R.id.email);
        Button btnReset = (Button) findViewById(R.id.btn_reset_password);
        Button btnBack = (Button) findViewById(R.id.btn_back);
        progressDialog = new ProgressDialog(this);
        resetPasswordPresenter = new ResetPasswordPresenter(this);

        FirebaseAuth auth = FirebaseAuth.getInstance();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = inputEmail.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplication(), "Enter your registered email id", Toast.LENGTH_SHORT).show();
                    return;
                }

                ProgressDialogManager.showProgressDialog(progressDialog, "Sign in");
                resetPasswordPresenter.resetPassword(email);
            }
        });
    }

    @Override
    public void resetOk() {
        ProgressDialogManager.hideProgressDialog(progressDialog);
    }

    @Override
    public Context getContext() {
        return this;
    }
}
