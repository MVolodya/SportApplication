package info.androidhive.firebase.activity.resetPasswordActivity.presenter;

import android.text.TextUtils;

import info.androidhive.firebase.activity.resetPasswordActivity.callback.CallbackResetPassword;
import info.androidhive.firebase.activity.resetPasswordActivity.view.ResetPasswordView;
import info.androidhive.firebase.classes.managers.DataGetter;
import info.androidhive.firebase.classes.managers.UserManager;

public class ResetPasswordPresenter implements CallbackResetPassword {

    private ResetPasswordView resetPasswordView;
    private UserManager userManager;

    public ResetPasswordPresenter(ResetPasswordView resetPasswordView) {
        this.resetPasswordView = resetPasswordView;
        userManager = new UserManager(resetPasswordView.getContext());
    }

    public void resetPassword(String email){
        if (TextUtils.isEmpty(email)) {
            resetPasswordView.onErrorValidEmail();
            return;
        }
        if(new DataGetter().isEmailValid(email))
        userManager.resetPassword(email, this);
        else resetPasswordView.onFail();
    }

    @Override
    public void onSuccess() {
        resetPasswordView.onSuccess();
    }
}
