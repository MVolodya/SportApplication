package info.androidhive.firebase.fragments.awayTeamFragment.presenter;

import info.androidhive.firebase.classes.models.DataHelper;
import info.androidhive.firebase.classes.retrofit.ApiFactory;
import info.androidhive.firebase.classes.retrofit.players.PlayersResponse;
import info.androidhive.firebase.classes.retrofit.players.PlayersService;
import info.androidhive.firebase.fragments.awayTeamFragment.view.AwayTeamView;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;

public class AwayTeamPresenter {

    private AwayTeamView mAwayTeamView;

    public void setAwayTeamView(AwayTeamView awayTeamView) {
        this.mAwayTeamView = awayTeamView;
    }

    public void showAwayTeam(){
        PlayersService service = ApiFactory.getPlayerService();
        Call<PlayersResponse> call = service.players(DataHelper.getInstance().getAwayTeamId());
        call.enqueue(new Callback<PlayersResponse>() {
            @Override
            public void onResponse(Response<PlayersResponse> response) {
                if(response.body() != null)
                mAwayTeamView.onSuccess(response.body());
                else mAwayTeamView.onFail();
            }

            @Override
            public void onFailure(Throwable t) {
                mAwayTeamView.onFail();
            }
        });
    }
}
