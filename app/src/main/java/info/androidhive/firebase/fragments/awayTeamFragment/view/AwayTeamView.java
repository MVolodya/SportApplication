package info.androidhive.firebase.fragments.awayTeamFragment.view;

import info.androidhive.firebase.classes.retrofit.players.PlayersResponse;

public interface AwayTeamView {
   void onSuccess(PlayersResponse playersResponse);
   void onFail();
}
