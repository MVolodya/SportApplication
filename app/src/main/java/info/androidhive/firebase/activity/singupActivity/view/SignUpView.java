package info.androidhive.firebase.activity.singupActivity.view;

import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;

public interface SignUpView {
    void okSignUp();
    Context getContext();
    FirebaseAuth getAuth();
}
