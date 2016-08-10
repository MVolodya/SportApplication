package info.androidhive.firebase.Classes;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import info.androidhive.firebase.Activities.MainActivity;


public class SignInManager {

    private static final String TAG = "facebookLogin";

    private Context context;
    private LocalDatabaseManager localDatabaseManager;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private ProgressDialog mProgressDialog;
    private ProgressDialogManager dialogManager;

    public SignInManager(Context context, FirebaseAuth auth) {
        this.context = context;
        this.auth = auth;
    }

    public void loginWithFacebook(LoginButton loginButton, CallbackManager callbackManager) {
        localDatabaseManager = new LocalDatabaseManager(context);
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException error) {
            }

        });
    }


    private void handleFacebookAccessToken(AccessToken token) {
        //Log.d(TAG, "handleFacebookAccessToken:" + token);

        dialogManager = new ProgressDialogManager(context,mProgressDialog);
        dialogManager.showProgressDialog();

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        auth.signInWithCredential(credential)
                .addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(context, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }else {
                            Profile profile = Profile.getCurrentProfile();
                            FirebaseUser firebaseUser = auth.getInstance().getCurrentUser();
                            localDatabaseManager.setUser(firebaseUser.getDisplayName(), firebaseUser.getEmail(),
                                    profile.getProfilePictureUri(950, 810).toString());
                            context.startActivity(new Intent(context, MainActivity.class));
                            ((Activity)context).finish();
                        }
                        dialogManager.hideProgressDialog();

                    }
                });
    }



    public static void signOut(){
        FirebaseAuth.getInstance().signOut();
        LoginManager.getInstance().logOut();
        LocalDatabaseManager.delete();
    }




}
