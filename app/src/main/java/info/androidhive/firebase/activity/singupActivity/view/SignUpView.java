package info.androidhive.firebase.activity.singupActivity.view;

import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;

import info.androidhive.firebase.baseView.BaseView;

public interface SignUpView extends BaseView {
    void onSuccess();
    void onFail();
    void onEmptyEmail();
    void onEmptyPassword();
    void onShortPassword();
    Context getContext();
    FirebaseAuth getmAuth();
}
