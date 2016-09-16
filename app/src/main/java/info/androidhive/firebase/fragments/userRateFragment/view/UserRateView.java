package info.androidhive.firebase.fragments.userRateFragment.view;

import java.util.List;

import info.androidhive.firebase.classes.models.Rate;

public interface UserRateView {
    void addList(List<Rate> rates);
    void onFail();
    void onRateListSize();
}
