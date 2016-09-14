package info.androidhive.firebase.fragments.rateFragment.presenter;

import android.net.Uri;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import info.androidhive.firebase.R;
import info.androidhive.firebase.classes.models.DataHelper;
import info.androidhive.firebase.classes.models.RatedUser;
import info.androidhive.firebase.classes.models.User;
import info.androidhive.firebase.classes.retrofit.ApiFactory;
import info.androidhive.firebase.classes.retrofit.rateMatch.RateMatchResponse;
import info.androidhive.firebase.classes.retrofit.rateMatch.RateMatchService;
import info.androidhive.firebase.classes.retrofit.team.TeamResponse;
import info.androidhive.firebase.classes.retrofit.team.TeamService;
import info.androidhive.firebase.fragments.rateFragment.view.RateView;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;

public class RatePresenter {

    private RateView rateView;

    public void setRateView(RateView rateView) {
        this.rateView = rateView;
    }

    public void showRateMatch(){
        RateMatchService service = ApiFactory.getRateMatchService();
        Call<RateMatchResponse> call = service.match(DataHelper.getInstance().getMatchId());
        call.enqueue(new Callback<RateMatchResponse>() {
            @Override
            public void onResponse(Response<RateMatchResponse> response) {
                rateView.onSuccess(response.body());
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    public void getUserData(){
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child(FirebaseAuth.getInstance().getCurrentUser().getDisplayName())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        RatedUser user = dataSnapshot.getValue(RatedUser.class);
                        rateView.setUser(user);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {}
                });
    }

    public void getHomeTeamPhotoUrl(int id){
        DataHelper.getInstance().setHomeTeamId(id);
        TeamService serviceTeam = ApiFactory.getTeamService();
        Call<TeamResponse> callTeam = serviceTeam.teams(id);
        callTeam.enqueue(new Callback<TeamResponse>() {
            @Override
            public void onResponse(Response<TeamResponse> response) {
                TeamResponse teamResponse = response.body();
                if(teamResponse!=null) {
                    String linkTeamImage = teamResponse.getCrestUrl();
                    rateView.onSuccessHomeImageUrl(linkTeamImage);
                } else rateView.onFail();
            }

            @Override
            public void onFailure(Throwable t) {rateView.onFail();}
        });
    }

    public void getAwayTeamPhotoUrl(int id){
        DataHelper.getInstance().setAwayTeamId(id);
        TeamService serviceTeam = ApiFactory.getTeamService();
        Call<TeamResponse> callTeam = serviceTeam.teams(id);
        callTeam.enqueue(new Callback<TeamResponse>() {
            @Override
            public void onResponse(Response<TeamResponse> response) {
                TeamResponse teamResponse = response.body();
                if(teamResponse!=null) {
                    String linkTeamImage = teamResponse.getCrestUrl();
                    rateView.onSuccessAwayImageUrl(linkTeamImage);
                }else rateView.onFail();
            }

            @Override
            public void onFailure(Throwable t) {rateView.onFail();}
        });
    }
}
