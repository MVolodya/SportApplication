package info.androidhive.firebase.fragments.homeTeamFragment.presenter;

import android.view.View;

import info.androidhive.firebase.classes.models.DataHelper;
import info.androidhive.firebase.classes.recycleViewAdapters.HomeTeamPlayerAdapter;
import info.androidhive.firebase.classes.retrofit.ApiFactory;
import info.androidhive.firebase.classes.retrofit.players.PlayersResponse;
import info.androidhive.firebase.classes.retrofit.players.PlayersService;
import info.androidhive.firebase.fragments.homeTeamFragment.view.HomeTeamView;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;

public class HomeTeamPresenter {

    private HomeTeamView homeTeamView;

    public void setHomeTeamView(HomeTeamView homeTeamView) {
        this.homeTeamView = homeTeamView;
    }

    public void showHomeTeam(){
        PlayersService service = ApiFactory.getPlayerService();
        Call<PlayersResponse> call = service.players(DataHelper.getInstance().getHomeTeamId());
        call.enqueue(new Callback<PlayersResponse>() {
            @Override
            public void onResponse(Response<PlayersResponse> response) {
                if(response.body() != null){
                    PlayersResponse playersResponse = response.body();
                    homeTeamView.onSuccess(playersResponse);
                } else homeTeamView.onFail();
            }

            @Override
            public void onFailure(Throwable t) {
                homeTeamView.onFail();
            }
        });
    }
}
