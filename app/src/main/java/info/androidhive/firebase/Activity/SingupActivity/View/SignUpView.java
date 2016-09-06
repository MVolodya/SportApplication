package info.androidhive.firebase.Activity.SingupActivity.View;

import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;

public interface SignUpView {
    void okSignUp();
    Context getContext();
    FirebaseAuth getAuth();
}
