package info.androidhive.firebase.classes.retrofit.match;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Headers;

public interface MatchService {

    @Headers("X-Auth-Token: 1dbb704646d347ab901a406231c160a5")
    @GET("v1/fixtures")
    Call<MatchResponse> matches();

}
