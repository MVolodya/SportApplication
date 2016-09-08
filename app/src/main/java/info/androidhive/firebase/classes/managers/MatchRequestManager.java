package info.androidhive.firebase.classes.managers;

import java.util.List;

import info.androidhive.firebase.classes.models.Rate;
import info.androidhive.firebase.classes.models.RatedMatchesToDB;
import info.androidhive.firebase.classes.retrofit.ApiFactory;
import info.androidhive.firebase.classes.retrofit.rateMatch.RateMatchResponse;
import info.androidhive.firebase.classes.retrofit.rateMatch.RateMatchService;
import info.androidhive.firebase.fragments.currentUserRateFragment.CallbackRate;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;

public class MatchRequestManager {

    public void getRate(final int id, final int i, final List<RatedMatchesToDB> ratedMatchesList, final CallbackRate callbackRate){
        RateMatchService service = ApiFactory.getRateMatchService();
        Call<RateMatchResponse> call = service.match(id);

        call.enqueue(new Callback<RateMatchResponse>() {
            @Override
            public void onResponse(Response<RateMatchResponse> response) {
                RateMatchResponse rateMatchResponse = response.body();
                Rate rate = new Rate();
                rate.setHomeTeamName(rateMatchResponse.getFixture().getHomeTeamName());
                rate.setAwayTeamName(rateMatchResponse.getFixture().getAwayTeamName());
                rate.setPoints(ratedMatchesList.get(i).getPoints());
                rate.setType(ratedMatchesList.get(i).getTypeOfRate());
                rate.setStatus(ratedMatchesList.get(i).getStatus());
                callbackRate.addRateToList(rate);
            }
            @Override
            public void onFailure(Throwable t) {}
        });
    }
}
