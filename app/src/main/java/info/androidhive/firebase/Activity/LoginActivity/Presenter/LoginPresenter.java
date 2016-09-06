package info.androidhive.firebase.Activity.LoginActivity.Presenter;

import com.facebook.CallbackManager;
import com.facebook.login.widget.LoginButton;

import info.androidhive.firebase.Activity.LoginActivity.View.CallbackLogin;
import info.androidhive.firebase.Activity.LoginActivity.View.LoginView;
import info.androidhive.firebase.Classes.Managers.SignInManager;

public class LoginPresenter implements CallbackLogin {

    public LoginView loginView;
    public SignInManager signInManager;
    public CallbackManager callbackManager;

    public LoginPresenter(LoginView loginView) {
        this.loginView = loginView;
        signInManager = new SignInManager(loginView.getContext(), loginView.getAuth());
    }

    public void loginWithFacebook(LoginButton button){
        callbackManager = CallbackManager.Factory.create();
        signInManager.loginWithFacebook(button, callbackManager, this);
    }

    public void login(String email, String password){

        signInManager.loginWithEmailAndPassword(email, password, this);
    }

    @Override
    public void okLogin() {
        loginView.loginOk();
    }

    public CallbackManager getCallbackManager() {
        return callbackManager;
    }
}
