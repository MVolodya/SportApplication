package info.androidhive.firebase.Classes.Retrofit.Match;

import retrofit.Call;
import retrofit.http.GET;

/**
 * Created by andri on 29.07.2016.
 */
public interface MatchService {

    @GET("v1/fixtures")
    Call<MatchResponse> matches();

}
