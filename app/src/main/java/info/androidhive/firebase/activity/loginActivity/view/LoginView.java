package info.androidhive.firebase.activity.loginActivity.view;

import android.content.Context;
import com.google.firebase.auth.FirebaseAuth;

import info.androidhive.firebase.baseView.BaseView;

public interface LoginView extends BaseView {
    void onSuccess();
    void onFail();
    void onEmptyEmail();
    void onEmptyPassword();

    Context getContext();
    FirebaseAuth getAuth();
}
