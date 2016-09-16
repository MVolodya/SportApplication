package info.androidhive.firebase.fragments.matchFragment.presenter;

import java.util.List;

import info.androidhive.firebase.classes.managers.DataGetter;
import info.androidhive.firebase.classes.models.DataHelper;
import info.androidhive.firebase.classes.retrofit.ApiFactory;
import info.androidhive.firebase.classes.retrofit.match.Fixture;
import info.androidhive.firebase.classes.retrofit.match.MatchResponse;
import info.androidhive.firebase.classes.retrofit.match.MatchService;
import info.androidhive.firebase.fragments.matchFragment.view.MatchFragmentView;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;

public class MatchFragmentPresenter {

    private MatchFragmentView matchFragmentView;

    public void setMatchFragmentView(MatchFragmentView matchFragmentView) {
        this.matchFragmentView = matchFragmentView;
    }

    public void getMatchList(){
        MatchService service = ApiFactory.getMatchService();
        Call<MatchResponse> call = service.matches();
        call.enqueue(new Callback<MatchResponse>() {
            @Override
            public void onResponse(Response<MatchResponse> response) {
                if(response.body()!=null)
                matchFragmentView.onSuccess(response.body());
                else matchFragmentView.onFail();
            }

            @Override
            public void onFailure(Throwable t) {matchFragmentView.onFail();}
        });
    }

    public void saveMatchData(List<Fixture> matches, int position){
        DataHelper dataHelper = DataHelper.getInstance();
        dataHelper.setMatchId(new DataGetter().getMatchId(matches.get(position)
                .getLinks().getSelf().getHref()));
        dataHelper.setHomeTeamId(new DataGetter().getTeamId(matches.get(position)
                .getLinks().getHomeTeam().getHref()));
        dataHelper.setAwayTeamId(new DataGetter().getTeamId(matches.get(position)
                .getLinks().getAwayTeam().getHref()));
    }
}
