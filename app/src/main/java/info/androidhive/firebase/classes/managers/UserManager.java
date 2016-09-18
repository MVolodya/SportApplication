package info.androidhive.firebase.classes.managers;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import info.androidhive.firebase.R;
import info.androidhive.firebase.activity.resetPasswordActivity.callback.CallbackResetPassword;
import info.androidhive.firebase.fragments.settingsFragment.callback.UpdateCallback;

public class UserManager {

    private final Context context;

    public UserManager(Context context) {
        this.context = context;
    }

    public void resetPassword(String email, final CallbackResetPassword callbackResetPassword) {
        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(context, R.string.reset_instruction, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, R.string.failed_send_instruction, Toast.LENGTH_SHORT).show();
                        }
                        callbackResetPassword.onSuccess();
                    }
                });
    }

    public static void updateUrl(final String photoUri) {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setPhotoUri(Uri.parse(photoUri))
                .build();
        firebaseUser.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {}
                    }
                });
    }

    public static void updateUsername(String username) {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(username)
                .build();
       firebaseUser.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {}
                    }
                });
    }

    public static void updateEmail(String email, final UpdateCallback updateCallback) {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            //change email
            firebaseUser.updateEmail(email)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                updateCallback.updateEmailSuccess();
                            }else {
                               updateCallback.onFailUpdateEmail();
                            }
                        }
                    });
        }
    }

    public static void updatePassword(String password, final UpdateCallback updateCallback) {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            //change email
            firebaseUser.updatePassword(password)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                updateCallback.updatePasswordSuccess();
                            }
                        }
                    });
        }
    }
}
