package info.androidhive.firebase.activity.singupActivity.presenter;

import android.text.TextUtils;

import info.androidhive.firebase.activity.singupActivity.callback.CallbackSignUp;
import info.androidhive.firebase.activity.singupActivity.view.SignUpView;
import info.androidhive.firebase.classes.managers.DataGetter;
import info.androidhive.firebase.classes.managers.SignInManager;

public class SignUpPresenter implements CallbackSignUp {

    private SignUpView signUpView;
    private SignInManager signInManager;

    public SignUpPresenter(SignUpView signUpView) {
        this.signUpView = signUpView;
        signInManager = new SignInManager(signUpView.getContext(), signUpView.getmAuth());
    }

    public void signUp(String email, final String password){

        if(!new DataGetter().isEmailValid(email)){
            signUpView.onValidEmail();
            return;
        }
        if (TextUtils.isEmpty(email)) {
            signUpView.onEmptyEmail();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            signUpView.onEmptyPassword();
            return;
        }

        if (password.length() < 6) {
            signUpView.onShortPassword();
            return;
        }

        signInManager.signUpWithEmailAndPassword(email, password, this);
    }

    @Override
    public void onSuccess() {
        signUpView.onSuccess();
    }

    @Override
    public void onFail() {
        signUpView.onFail();
    }
}
