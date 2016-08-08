package info.androidhive.firebase.Classes.Retrofit.LeagueTable;


import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;

public interface LeagueTableService {

    @GET("/v1/competitions/{id}/leagueTable")
    Call<LeagueTableResponse> tables(@Path("id") int leagueId);
}
