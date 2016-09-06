package info.androidhive.firebase.Activity.SingupActivity.Presenter;

import info.androidhive.firebase.Activity.SingupActivity.View.CallbackSignUp;
import info.androidhive.firebase.Activity.SingupActivity.View.SignUpView;
import info.androidhive.firebase.Classes.Managers.SignInManager;

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
