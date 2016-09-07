package info.androidhive.firebase.fragments.leagueFragment.view;

import java.util.List;

import info.androidhive.firebase.classes.retrofit.league.LeagueModel;

public interface LeagueView {
    void onSuccess(List<LeagueModel> leagueList);
    void onFail();
}
