package info.androidhive.firebase.Activity.ResetPasswordActivity.Presenter;

import info.androidhive.firebase.Activity.ResetPasswordActivity.Callback.CallbackResetPassword;
import info.androidhive.firebase.Activity.ResetPasswordActivity.View.ResetPasswordView;
import info.androidhive.firebase.Classes.Managers.UserManager;

public class ResetPasswordPresenter implements CallbackResetPassword {

    private ResetPasswordView resetPasswordView;
    private UserManager userManager;

    public ResetPasswordPresenter(ResetPasswordView resetPasswordView) {
        this.resetPasswordView = resetPasswordView;
        userManager = new UserManager(resetPasswordView.getContext());
    }

    public void resetPassword(String email){
        userManager.resetPassword(email, this);
    }

    @Override
    public void ok() {
        resetPasswordView.resetOk();
    }
}
