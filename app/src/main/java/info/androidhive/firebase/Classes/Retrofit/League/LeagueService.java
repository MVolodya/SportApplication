package info.androidhive.firebase.Classes.Retrofit.League;

import java.util.List;

import retrofit.Call;
import retrofit.http.GET;

public interface LeagueService {
    @GET("/v1/competitions/?season=2016")
    Call<List<LeagueModel>> leagues();
}