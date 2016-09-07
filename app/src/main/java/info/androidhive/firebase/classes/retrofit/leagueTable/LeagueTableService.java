package info.androidhive.firebase.classes.retrofit.leagueTable;


import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.Path;

public interface LeagueTableService {
    @Headers("X-Auth-Token: 1dbb704646d347ab901a406231c160a5")
    @GET("/v1/competitions/{id}/leagueTable")
    Call<LeagueTableResponse> tables(@Path("id") int leagueId);
}
