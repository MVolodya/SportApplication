package info.androidhive.firebase.Classes.Retrofit.Match;

import retrofit.Call;
import retrofit.http.GET;

/**
 * Created by andri on 29.07.2016.
 */
public interface MatchService {

    @GET("/v1/competitions/398/fixtures?matchday=8")
    Call<FixtResponse> matches();

}
