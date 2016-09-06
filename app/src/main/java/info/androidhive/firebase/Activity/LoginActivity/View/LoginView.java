package info.androidhive.firebase.Activity.LoginActivity.View;

import android.content.Context;
import com.google.firebase.auth.FirebaseAuth;

import info.androidhive.firebase.BaseView;

public interface LoginView extends BaseView {
    void loginOk();

    Context getContext();
    FirebaseAuth getAuth();
}
