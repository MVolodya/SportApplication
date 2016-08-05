package info.androidhive.firebase.Fragments;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mikhaellopez.circularimageview.CircularImageView;

import info.androidhive.firebase.Activities.LoginActivity;
import info.androidhive.firebase.Classes.DatabaseManager;
import info.androidhive.firebase.Classes.ProgressDialogManager;
import info.androidhive.firebase.Classes.SignInManager;
import info.androidhive.firebase.Classes.User;
import info.androidhive.firebase.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingFragment extends Fragment implements View.OnFocusChangeListener, View.OnClickListener {

    private View view;
    private EditText etUsername;
    private EditText etEmail;
    private Button btnSave;
    private Button btnChangePassword;
    private CircularImageView userPhoto;
    private LinearLayout linearLayout;

    private User user;
    private DatabaseManager databaseManager;

    private FirebaseAuth auth;
    private FirebaseUser firebaseUser;
    private ProgressDialog mProgressDialog;
    private ProgressDialogManager progressDialogManager;

    public SettingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_setting, container, false);

        etUsername = (EditText) view.findViewById(R.id.username_setting);
        etEmail = (EditText) view.findViewById(R.id.email_setting);
        userPhoto = (CircularImageView) view.findViewById(R.id.imageViewPhoto);
        btnSave = (Button) view.findViewById(R.id.buttonSave);
        btnChangePassword = (Button) view.findViewById(R.id.buttonChangePassword);

        etUsername.setOnFocusChangeListener(this);
        etEmail.setOnFocusChangeListener(this);
        btnSave.setOnClickListener(this);
        btnChangePassword.setOnClickListener(this);

        linearLayout = (LinearLayout) view.findViewById(R.id.layout_main);

        databaseManager = new DatabaseManager(view.getContext());
        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getInstance().getCurrentUser();
        user = databaseManager.getUser();

        mProgressDialog = new ProgressDialog(view.getContext());
        mProgressDialog.setMessage("Wait...");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setIndeterminate(true);

        setUserInformation();

        // Inflate the layout for this fragment
        return view;
    }


    private void setUserInformation() {

        if (user.getName() != null) etUsername.setText(user.getName());
        else etUsername.setText("Anonymous");

        if (user.getEmail() != null) etEmail.setText(user.getEmail());
        else etEmail.setText("Anonymous@Anonymous.com");

        if (user.getPhotoURL() != null) {
            Glide.with(view.getContext())
                    .load(user.getPhotoURL())
                    .into(userPhoto);
        } else userPhoto.setImageResource(R.drawable.prof);

        //progressDialogManager.hideProgressDialog();

    }


    public void hideKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void changeEmail() {

        mProgressDialog.show();

        if (firebaseUser != null) {

            //change email
            firebaseUser.updateEmail(etEmail.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                mProgressDialog.hide();
                                Snackbar snackbar = Snackbar
                                        .make(linearLayout, "Email address is updated. Please sign in with new email id!", Snackbar.LENGTH_LONG)
                                        .setAction("OK", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                SignInManager.signOut();
                                                startActivity(new Intent(view.getContext(), LoginActivity.class));
                                                getActivity().finish();
                                            }
                                        });
                                snackbar.show();
                            } else {
                              mProgressDialog.hide();
                                Snackbar snackbar = Snackbar
                                        .make(linearLayout, "Error!"+task.getException(), Snackbar.LENGTH_LONG);
                                snackbar.show();
                            }
                        }
                    });
        } else Toast.makeText(view.getContext(), "firebaseuser is null", Toast.LENGTH_SHORT).show();

    }

    private void changePassword() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(view.getContext());
        alertDialog.setTitle("PASSWORD");
        alertDialog.setMessage("Enter new password");

        final EditText input = new EditText(view.getContext());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        alertDialog.setView(input);


        alertDialog.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String password = input.getText().toString();
                        if (firebaseUser != null) {
                            mProgressDialog.show();
                            firebaseUser.updatePassword(password)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                progressDialogManager.hideProgressDialog();
                                                Snackbar snackbar = Snackbar
                                                        .make(linearLayout, "Password is updated. Please sign in with new password!", Snackbar.LENGTH_LONG)
                                                        .setAction("OK", new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View view) {
                                                                SignInManager.signOut();
                                                                startActivity(new Intent(view.getContext(), LoginActivity.class));
                                                                getActivity().finish();
                                                            }
                                                        });
                                                snackbar.show();
                                            } else {
                                                mProgressDialog.hide();
                                                Snackbar snackbar = Snackbar
                                                        .make(linearLayout, "Error!", Snackbar.LENGTH_LONG);
                                                snackbar.show();
                                            }
                                        }
                                    });
                        } else
                            Toast.makeText(view.getContext(), "firebaseuser is null", Toast.LENGTH_SHORT).show();
                    }
                });

        alertDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        alertDialog.show();
    }


    @Override
    public void onFocusChange(View view, boolean hasFocus) {

        if (!hasFocus) {
            hideKeyboard();
        }
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.buttonSave:
                changeEmail();
                break;
            case R.id.buttonChangePassword:
                changePassword();
                break;
        }
    }
}
