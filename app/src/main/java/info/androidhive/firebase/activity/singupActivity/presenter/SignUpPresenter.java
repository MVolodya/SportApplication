package info.androidhive.firebase.activity.singupActivity.presenter;

import info.androidhive.firebase.activity.singupActivity.callback.CallbackSignUp;
import info.androidhive.firebase.activity.singupActivity.view.SignUpView;
import info.androidhive.firebase.classes.managers.SignInManager;

public class SignUpPresenter implements CallbackSignUp {

    private SignUpView signUpView;
    public SignInManager signInManager;

    public SignUpPresenter(SignUpView signUpView) {
        this.signUpView = signUpView;
        signInManager = new SignInManager(signUpView.getContext(), signUpView.getAuth());
    }

    public void login(String email,final String password){
        signInManager.signUpWithEmailAndPassword(email, password, this);
    }

    @Override
    public void okSignUp() {
        signUpView.okSignUp();
    }
}
