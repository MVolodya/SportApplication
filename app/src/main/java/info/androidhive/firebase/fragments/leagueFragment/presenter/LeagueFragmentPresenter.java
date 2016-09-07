package info.androidhive.firebase.fragments.leagueFragment.presenter;

import java.util.List;

import info.androidhive.firebase.classes.models.DataHelper;
import info.androidhive.firebase.classes.retrofit.ApiFactory;
import info.androidhive.firebase.classes.retrofit.league.LeagueModel;
import info.androidhive.firebase.classes.retrofit.league.LeagueService;
import info.androidhive.firebase.fragments.leagueFragment.view.LeagueView;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;

public class LeagueFragmentPresenter {

    private LeagueView leagueView;

    public void setLeagueView(LeagueView leagueView) {
        this.leagueView = leagueView;
    }

    public void sendRequest(){
        LeagueService service = ApiFactory.getLeagueService();
        Call<List<LeagueModel>> call = service.leagues();
        call.enqueue(new Callback<List<LeagueModel>>() {
            @Override
            public void onResponse(Response<List<LeagueModel>> response) {
                leagueView.onSuccess(response.body());
            }

            @Override
            public void onFailure(Throwable t) {
                leagueView.onFail();
            }
        });
    }

    public void setLeagueData(int id, String name){
        DataHelper dataHelper = DataHelper.getInstance();
        dataHelper.setId(id);
        dataHelper.setLeagueName(name);
    }
}
