package info.androidhive.firebase.fragments.homeTeamFragment.presenter;

import info.androidhive.firebase.classes.models.DataHelper;
import info.androidhive.firebase.classes.retrofit.ApiFactory;
import info.androidhive.firebase.classes.retrofit.players.PlayersResponse;
import info.androidhive.firebase.classes.retrofit.players.PlayersService;
import info.androidhive.firebase.fragments.homeTeamFragment.view.HomeTeamView;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;

public class HomeTeamPresenter {

    private HomeTeamView mHomeTeamView;

    public void setHomeTeamView(HomeTeamView homeTeamView) {
        this.mHomeTeamView = homeTeamView;
    }

    public void showHomeTeam(){
        PlayersService service = ApiFactory.getPlayerService();
        Call<PlayersResponse> call = service.players(DataHelper.getInstance().getHomeTeamId());
        call.enqueue(new Callback<PlayersResponse>() {

            @Override
            public void onResponse(Response<PlayersResponse> response) {
                if(response.body() != null){
                    PlayersResponse playersResponse = response.body();
                    mHomeTeamView.onSuccess(playersResponse);
                } else mHomeTeamView.onFail();
            }

            @Override
            public void onFailure(Throwable t) {
                mHomeTeamView.onFail();
            }
        });
    }
}
