package info.androidhive.firebase.Classes.Retrofit.LeagueTable;


import retrofit.Call;
import retrofit.http.GET;

public interface LeagueTableService {

    @GET("/v1/competitions/398/leagueTable")
    Call<LeagueTableResponse> tables();
}
