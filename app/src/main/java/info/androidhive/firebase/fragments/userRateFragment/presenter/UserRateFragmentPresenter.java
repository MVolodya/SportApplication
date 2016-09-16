package info.androidhive.firebase.fragments.userRateFragment.presenter;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import info.androidhive.firebase.classes.managers.MatchRequestManager;
import info.androidhive.firebase.classes.models.Rate;
import info.androidhive.firebase.classes.models.RatedMatchesToDB;
import info.androidhive.firebase.classes.models.RatedUser;
import info.androidhive.firebase.fragments.userRateFragment.callback.CallbackRate;
import info.androidhive.firebase.fragments.userRateFragment.view.UserRateView;

public class UserRateFragmentPresenter implements CallbackRate {

    private UserRateView mUserRateView;
    private MatchRequestManager mMatchRequestManager;
    private List<Rate> rates = new ArrayList<>();
    private int mListSize;

    public void setUserRateView(UserRateView userRateView) {
        this.mUserRateView = userRateView;
    }

    public void getUserRates(String username) {
        mMatchRequestManager = new MatchRequestManager();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child(username)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        RatedUser ratedUser = dataSnapshot.getValue(RatedUser.class);
                                                        final List<RatedMatchesToDB> ratedMatchesList = ratedUser.getRatedMatches();
                                                        if (ratedMatchesList != null && ratedMatchesList.size() > 0) {
                                                            mListSize = ratedMatchesList.size();
                                                            for (RatedMatchesToDB ratedMatches : ratedMatchesList) {
                                                                mMatchRequestManager.getRate(
                                                                        Integer.parseInt(ratedMatches.getMatchId()),
                                                                        ratedMatches,
                                                                        UserRateFragmentPresenter.this
                                                                );
                                                            }
                                                        } else mUserRateView.onRateListSize();
                                                    }

                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {
                                                        mUserRateView.onFail();
                                                    }
                                                }
                );
    }

    @Override
    public void addRateToList(Rate rate) {
        rates.add(rate);
        if (mListSize == rates.size()) {
            mUserRateView.addList(rates);
        }
    }

    @Override
    public void onError() {
        mUserRateView.onFail();
    }
}
