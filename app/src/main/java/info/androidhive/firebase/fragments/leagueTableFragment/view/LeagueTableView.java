package info.androidhive.firebase.fragments.leagueTableFragment.view;

import java.util.List;

import info.androidhive.firebase.classes.retrofit.leagueTable.Standing;

public interface LeagueTableView {
    void onSuccess(List<Standing> tables);
    void onFail();
}
