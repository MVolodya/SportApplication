package info.androidhive.firebase.classes.retrofit.players;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.Path;

/**
 * Created by andrii on 25.08.16.
 */
public interface PlayersService {
    @Headers("X-Auth-Token: 1dbb704646d347ab901a406231c160a5")
    @GET("/v1/teams/{id}/players")
    Call<PlayersResponse> players(@Path("id") int teamId);
}
