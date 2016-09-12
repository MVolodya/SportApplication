package info.androidhive.firebase.fragments.currentUserRateFragment.presenter;

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
import info.androidhive.firebase.fragments.currentUserRateFragment.callback.CallbackRate;
import info.androidhive.firebase.fragments.currentUserRateFragment.view.UserRateView;

public class UserRateFragmentPresenter implements CallbackRate {

    private UserRateView userRateView;
    private MatchRequestManager matchRequestManager;
    private List<Rate> rates = new ArrayList<>();

    public void setUserRateView(UserRateView userRateView) {
        this.userRateView = userRateView;
    }

    public void getUserRates(String username){
        matchRequestManager = new MatchRequestManager();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child(username)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        RatedUser ratedUser = dataSnapshot.getValue(RatedUser.class);
                                                        final List<RatedMatchesToDB> ratedMatchesList = ratedUser.getRatedMatches();
                                                        final List<Rate> rateList = new ArrayList<>();
                                                        userRateView.setRatedMathList(ratedMatchesList);
                                                        if (ratedMatchesList != null && ratedMatchesList.size()>0) {
                                                            for (int i = 0; i < ratedMatchesList.size(); i++) {
                                                               matchRequestManager.getRate(
                                                                       Integer.parseInt(ratedMatchesList.get(i).getMatchId()),
                                                                       i,
                                                                       ratedMatchesList,
                                                                       UserRateFragmentPresenter.this
                                                               );
                                                            }
                                                        }else userRateView.onRateListSize();
                                                    }

                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {userRateView.onFail();}
                                                }

                );
    }

    @Override
    public void addRateToList(Rate rate) {
        userRateView.addItemToList(rate);
    }

    @Override
    public void onError() {
        userRateView.onFail();
    }
}
