package info.androidhive.firebase.Classes.Retrofit.Match;

import android.util.Log;
import info.androidhive.firebase.Classes.Retrofit.ApiFactory;
import info.androidhive.firebase.Classes.Retrofit.Team.TeamResponse;
import info.androidhive.firebase.Classes.Retrofit.Team.TeamService;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;


public class TeamsImage implements Callback<TeamResponse> {

    private String teamImageLink;


    public TeamsImage(int teamId){
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
