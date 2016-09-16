package info.androidhive.firebase.classes.managers;

import info.androidhive.firebase.classes.models.Rate;
import info.androidhive.firebase.classes.models.RatedMatchesToDB;
import info.androidhive.firebase.classes.retrofit.ApiFactory;
import info.androidhive.firebase.classes.retrofit.rateMatch.RateMatchResponse;
import info.androidhive.firebase.classes.retrofit.rateMatch.RateMatchService;
import info.androidhive.firebase.fragments.currentUserRateFragment.callback.CallbackRate;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;

public class MatchRequestManager {

    public void getRate(final int id, final RatedMatchesToDB ratedMatches, final CallbackRate callbackRate){
        RateMatchService service = ApiFactory.getRateMatchService();
        Call<RateMatchResponse> call = service.match(id);
        call.enqueue(new Callback<RateMatchResponse>() {
            @Override
            public void onResponse(Response<RateMatchResponse> response) {
                RateMatchResponse rateMatchResponse = response.body();
                if(rateMatchResponse!=null) {
                    Rate rate = new Rate();
                    rate.setHomeTeamName(rateMatchResponse.getFixture().getHomeTeamName());
                    rate.setAwayTeamName(rateMatchResponse.getFixture().getAwayTeamName());
                    rate.setPoints(ratedMatches.getPoints());
                    rate.setType(ratedMatches.getTypeOfRate());
                    rate.setStatus(ratedMatches.getStatus());
                    rate.setId(ratedMatches.getMatchId());
                    callbackRate.addRateToList(rate);
                } else callbackRate.onError();
            }
            @Override
            public void onFailure(Throwable t) {}
        });
    }
}
