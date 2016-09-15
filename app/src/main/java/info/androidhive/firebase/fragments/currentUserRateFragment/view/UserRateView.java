package info.androidhive.firebase.fragments.currentUserRateFragment.view;

import java.util.List;

import info.androidhive.firebase.R;
import info.androidhive.firebase.classes.models.Rate;
import info.androidhive.firebase.classes.models.RatedMatchesToDB;

public interface UserRateView {
    void addList(List<Rate> rates);
    void onFail();
    void onRateListSize();
}
