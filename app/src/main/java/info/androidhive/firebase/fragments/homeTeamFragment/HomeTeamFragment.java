package info.androidhive.firebase.fragments.homeTeamFragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import info.androidhive.firebase.classes.models.DataHelper;
import info.androidhive.firebase.classes.recycleViewAdapters.DividerItemDecoration;
import info.androidhive.firebase.classes.recycleViewAdapters.HomeTeamPlayerAdapter;
import info.androidhive.firebase.classes.retrofit.ApiFactory;
import info.androidhive.firebase.classes.retrofit.players.PlayersResponse;
import info.androidhive.firebase.classes.retrofit.players.PlayersService;
import info.androidhive.firebase.R;
import info.androidhive.firebase.fragments.homeTeamFragment.presenter.HomeTeamPresenter;
import info.androidhive.firebase.fragments.homeTeamFragment.view.HomeTeamView;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeTeamFragment extends Fragment implements Callback<PlayersResponse>,
        HomeTeamView{

    private RecyclerView recyclerView;
    private TextView msg;
    private RecyclerView.LayoutManager mLayoutManager;
    private HomeTeamPresenter homeTeamPresenter;

    public HomeTeamFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_team, container, false);
        homeTeamPresenter = new HomeTeamPresenter();
        homeTeamPresenter.setHomeTeamView(this);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_home_team_players);
        msg = (TextView)view.findViewById(R.id.textViewHomeMsg);
        mLayoutManager = new LinearLayoutManager(view.getContext());

        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        msg.setVisibility(View.GONE);

        homeTeamPresenter.showHomeTeam();
        return view;
    }

    @Override
    public void onResponse(Response<PlayersResponse> response) {
        if (response.isSuccess()) {
            PlayersResponse playersResponse = response.body();

            if (playersResponse.getPlayers().size() == 0) {
                msg.setVisibility(View.VISIBLE);
            } else {
                recyclerView.setLayoutManager(mLayoutManager);
                HomeTeamPlayerAdapter homeTeamPlayerAdapter = new HomeTeamPlayerAdapter(playersResponse.getPlayers());
                recyclerView.setAdapter(homeTeamPlayerAdapter);
            }
        }
    }

    @Override
    public void onFailure(Throwable t) {

    }

    @Override
    public void onSuccess(PlayersResponse playersResponse) {
        if (playersResponse.getPlayers().size() == 0) {
            msg.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setLayoutManager(mLayoutManager);
            HomeTeamPlayerAdapter homeTeamPlayerAdapter = new HomeTeamPlayerAdapter(playersResponse.getPlayers());
            recyclerView.setAdapter(homeTeamPlayerAdapter);
        }
    }

    @Override
    public void onFail() {
        Toast.makeText(getContext(), R.string.wait_sec, Toast.LENGTH_LONG).show();}
}
