package info.androidhive.firebase.fragments.leagueTableFragment.presenter;

import info.androidhive.firebase.classes.models.DataHelper;
import info.androidhive.firebase.classes.retrofit.ApiFactory;
import info.androidhive.firebase.classes.retrofit.leagueTable.LeagueTableResponse;
import info.androidhive.firebase.classes.retrofit.leagueTable.LeagueTableService;
import info.androidhive.firebase.fragments.leagueTableFragment.view.LeagueTableView;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;

public class LeagueTablePresenter {

    private LeagueTableView mLeagueTableView;

    public void setLeagueTableView(LeagueTableView leagueTableView) {
        this.mLeagueTableView = leagueTableView;
    }

    public void showLeagueTeam(){
        DataHelper dataHelper = DataHelper.getInstance();
        int id = dataHelper.getId();
        LeagueTableService service = ApiFactory.getTableService();
        Call<LeagueTableResponse> call = service.tables(id);
        call.enqueue(new Callback<LeagueTableResponse>() {
            @Override
            public void onResponse(Response<LeagueTableResponse> response) {
                if(response.body()!=null)
                mLeagueTableView.onSuccess(response.body().getStanding());
                else mLeagueTableView.onFail();
            }

            @Override
            public void onFailure(Throwable t) {
                mLeagueTableView.onFail();
            }
        });
    }
}
