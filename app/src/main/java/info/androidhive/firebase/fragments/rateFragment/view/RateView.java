package info.androidhive.firebase.fragments.rateFragment.view;

import info.androidhive.firebase.classes.retrofit.rateMatch.RateMatchResponse;

public interface RateView {
    void onSuccess(RateMatchResponse rateMatchResponse);
    void onSuccessHomeImageUrl(String url);
    void onFailHomeImageUrl();
    void onSuccessAwayImageUrl(String url);
    void onFailAwayImageUrl();
    void onFail();
}
