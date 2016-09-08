package info.androidhive.firebase.fragments.matchFragment.view;

import info.androidhive.firebase.classes.retrofit.match.MatchResponse;

public interface MatchFragmentView {
    void onSuccess(MatchResponse matchResponse);
    void onFail();
}
