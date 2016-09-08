package info.androidhive.firebase.fragments.homeTeamFragment.view;

import info.androidhive.firebase.classes.retrofit.players.PlayersResponse;

public interface HomeTeamView {
    void onSuccess(PlayersResponse playersResponse);
    void onFail();
}
