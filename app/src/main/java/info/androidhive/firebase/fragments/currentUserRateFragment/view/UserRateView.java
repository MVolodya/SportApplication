package info.androidhive.firebase.fragments.currentUserRateFragment.view;

import java.util.List;

import info.androidhive.firebase.classes.models.Rate;
import info.androidhive.firebase.classes.models.RatedMatchesToDB;

public interface UserRateView {
    void setRatedMathList(List<RatedMatchesToDB> ratedMatchesList);
    void addItemToList(Rate rate);
    void onFail();
    void onRateListSize();
}
