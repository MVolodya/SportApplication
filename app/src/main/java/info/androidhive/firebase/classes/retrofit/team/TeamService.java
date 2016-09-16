package info.androidhive.firebase.classes.retrofit.team;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.Path;

public interface TeamService {
        @Headers("X-Auth-Token: 1dbb704646d347ab901a406231c160a5")
        @GET("/v1/teams/{id}")
        Call<TeamResponse> teams(@Path("id") int teamId);
}
