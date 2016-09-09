package info.androidhive.firebase.classes.managers;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
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

import info.androidhive.firebase.activity.loginActivity.LoginActivity;
import info.androidhive.firebase.activity.singupActivity.SignupActivity;
import info.androidhive.firebase.activity.loginActivity.callback.CallbackLogin;
import info.androidhive.firebase.activity.singupActivity.callback.CallbackSignUp;
import info.androidhive.firebase.R;


public class SignInManager {

    private static final String TAG = "facebookLogin";

    private final Context context;
    private final FirebaseAuth auth;
    //private final ProgressDialog mProgressDialog;
    private CallbackLogin callbackLogin;

    public SignInManager(Context context, FirebaseAuth auth) {
        this.context = context;
        this.auth = auth;
        //mProgressDialog = new ProgressDialog(context);
        LocalDatabaseManager localDatabaseManager = new LocalDatabaseManager(context);
    }

    public void signUpWithEmailAndPassword(String email, final String password, final CallbackSignUp callbackSignUp){
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener((SignupActivity) context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Toast.makeText(context, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                        //ProgressDialogManager.hideProgressDialog(mProgressDialog);
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(context, "Authentication failed." + task.getException(),
                                    Toast.LENGTH_SHORT).show();
                        } else {

                            long currentTime = System.currentTimeMillis();

                            if(auth.getCurrentUser().getDisplayName() != null) {

                                LocalDatabaseManager.setUser(auth.getCurrentUser().getDisplayName(),
                                        auth.getCurrentUser().getEmail(),
                                        Uri.parse(context.getString(R.string.user_photo_url)));

                                new RemoteDatabaseManager(context)
                                        .setUserData(auth.getCurrentUser().getDisplayName(),
                                                context.getString(R.string.user_photo_url));
                            } else {

                                LocalDatabaseManager.setUser("Anonymous"+currentTime,
                                        auth.getCurrentUser().getEmail(),
                                        Uri.parse(context.getString(R.string.user_photo_url)));

                                UserManager.updateUsername("Anonymous"+currentTime);
                                UserManager.updateUrl(context.getString(R.string.user_photo_url));

                                new RemoteDatabaseManager(context)
                                        .setUserData("Anonymous"+currentTime,
                                                context.getString(R.string.user_photo_url));
                            }
                            callbackSignUp.okSignUp();
                        }
                    }
                });
    }

    public void loginWithEmailAndPassword(String email,final String password, final CallbackLogin callbackLogin){
        //authenticate user
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(((LoginActivity)context), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            // there was an error
                            if (password.length() < 6) {
                                ((LoginActivity)context).getInputPassword().setError(context.getString(R.string.minimum_password));
                            } else {
                                Toast.makeText(context, context.getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
                            }
                        } else {
                            FirebaseUser fbUser = FirebaseAuth.getInstance().getCurrentUser();

                            if(fbUser != null){
                                LocalDatabaseManager.setUser(fbUser.getDisplayName(),
                                        fbUser.getEmail(),
                                        fbUser.getPhotoUrl());

                                callbackLogin.okLogin();
                            }else
                                Toast.makeText(context, "Wrong with fbUser", Toast.LENGTH_SHORT).show();
                        }
                    }

                });
    }

    public void loginWithFacebook(LoginButton loginButton, final CallbackManager callbackManager,
                                  final CallbackLogin callbackLogin) {
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken(), callbackLogin);
            }

            @Override
            public void onCancel() {}

            @Override
            public void onError(FacebookException error) {}
        });
    }


    private void handleFacebookAccessToken(AccessToken token, final CallbackLogin callbackLogin) {
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
                            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

                            if((firebaseUser != null ? firebaseUser.getDisplayName() : null) != null)
                            new RemoteDatabaseManager(context)
                                    .setUserData(firebaseUser.getDisplayName(),
                                            profile.getProfilePictureUri(500, 281).toString());
                            else new RemoteDatabaseManager(context)
                                    .setUserData("Anonymous"+System.currentTimeMillis(),
                                            context.getString(R.string.user_photo_url));

                            LocalDatabaseManager.setUser(firebaseUser.getDisplayName(),
                                    firebaseUser.getEmail(),
                                    firebaseUser.getPhotoUrl());
                            callbackLogin.okLogin();
                        }
                    }
                });
    }

    public static void signOut(){
        FirebaseAuth.getInstance().signOut();
        LoginManager.getInstance().logOut();
    }




}