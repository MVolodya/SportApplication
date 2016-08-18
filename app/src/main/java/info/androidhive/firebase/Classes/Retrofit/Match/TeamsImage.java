package info.androidhive.firebase.Classes.Retrofit.Match;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import java.io.InputStream;
import java.net.URL;

import info.androidhive.firebase.Classes.ImageUrlResponse;
import info.androidhive.firebase.Classes.RecycleViewClasses.MatchAdapter;
import info.androidhive.firebase.Classes.Retrofit.ApiFactory;
import info.androidhive.firebase.Classes.Retrofit.RateMatch.RateMatchResponse;
import info.androidhive.firebase.Classes.Retrofit.Team.TeamResponse;
import info.androidhive.firebase.Classes.Retrofit.Team.TeamService;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;


public class TeamsImage implements Callback<TeamResponse> {

    private String teamImageLink;


    public TeamsImage(int teamId, ImageUrlResponse imageUrlResponse){
        TeamService service = ApiFactory.getTeamService();
        Call<TeamResponse> call = service.teams(teamId);
        call.enqueue(this);
    }

    @Override
    public void onResponse(Response<TeamResponse> response) {
        if (response.isSuccess()) {

            TeamResponse teamResponse = response.body();

        }
    }

    @Override
    public void onFailure(Throwable t) {
        Log.d("imageTeam",""+t);
    }


}
