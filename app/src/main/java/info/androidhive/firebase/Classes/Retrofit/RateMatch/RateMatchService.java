package info.androidhive.firebase.Classes.Retrofit.RateMatch;

import info.androidhive.firebase.Classes.Retrofit.Match.MatchResponse;
import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.Path;

/**
 * Created by andrii on 15.08.16.
 */
public interface RateMatchService {
        @Headers("X-Auth-Token: 1dbb704646d347ab901a406231c160a5")
        @GET("v1/fixtures/{matchId}")
        Call<RateMatchResponse> match(@Path("matchId") int matchId);

    }

