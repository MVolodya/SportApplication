package info.androidhive.firebase.activity.singupActivity.presenter;

import android.text.TextUtils;
import android.widget.Toast;

import info.androidhive.firebase.R;
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

        if(new DataGetter().isEmailValid(email))
        signInManager.signUpWithEmailAndPassword(email, password, this);
        else signUpView.onFail();
    }

    @Override
    public void onSuccess() {
        signUpView.onSuccess();
    }
}
