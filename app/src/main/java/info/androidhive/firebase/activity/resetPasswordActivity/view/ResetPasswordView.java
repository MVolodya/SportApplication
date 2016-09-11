package info.androidhive.firebase.activity.resetPasswordActivity.view;

import android.content.Context;

import info.androidhive.firebase.baseView.BaseView;

public interface ResetPasswordView extends BaseView {
    void onSuccess();
    void onFail();
    Context getContext();
}
