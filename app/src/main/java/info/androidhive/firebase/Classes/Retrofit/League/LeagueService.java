package info.androidhive.firebase.Classes.Retrofit.League;

import java.util.List;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Headers;

public interface LeagueService {
    @Headers("X-Auth-Token: 1dbb704646d347ab901a406231c160a5")
    @GET("/v1/competitions/?season=2016")
    Call<List<LeagueModel>> leagues();
}