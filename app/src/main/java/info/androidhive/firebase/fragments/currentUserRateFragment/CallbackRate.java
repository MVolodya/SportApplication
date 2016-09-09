package info.androidhive.firebase.fragments.currentUserRateFragment;

import info.androidhive.firebase.classes.models.Rate;

public interface CallbackRate {
    void addRateToList(Rate rate);
    void onError();
}
