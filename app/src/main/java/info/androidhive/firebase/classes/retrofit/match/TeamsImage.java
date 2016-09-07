package info.androidhive.firebase.classes.retrofit.match;

import android.util.Log;
import info.androidhive.firebase.classes.retrofit.ApiFactory;
import info.androidhive.firebase.classes.retrofit.team.TeamResponse;
import info.androidhive.firebase.classes.retrofit.team.TeamService;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;


class TeamsImage implements Callback<TeamResponse> {

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
