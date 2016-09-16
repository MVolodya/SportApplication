package info.androidhive.firebase.activity.loginActivity.presenter;

import android.text.TextUtils;

import com.facebook.CallbackManager;
import com.facebook.login.widget.LoginButton;

import info.androidhive.firebase.activity.loginActivity.callback.CallbackLogin;
import info.androidhive.firebase.activity.loginActivity.view.LoginView;
import info.androidhive.firebase.classes.managers.DataGetter;
import info.androidhive.firebase.classes.managers.SignInManager;

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
        if (TextUtils.isEmpty(email)) {
            loginView.onEmptyEmail();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            loginView.onEmptyPassword();
            return;
        }

        if(new DataGetter().isEmailValid(email))
        signInManager.loginWithEmailAndPassword(email, password, this);
        else loginView.onFail();
    }

    @Override
    public void okLogin() {
        loginView.onSuccess();
    }

    @Override
    public void onFailCallback() {
        loginView.onFail();
    }

    public CallbackManager getCallbackManager() {
        return callbackManager;
    }
}
