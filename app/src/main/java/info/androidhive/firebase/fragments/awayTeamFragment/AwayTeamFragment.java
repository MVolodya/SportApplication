package info.androidhive.firebase.fragments.awayTeamFragment;


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
import info.androidhive.firebase.fragments.awayTeamFragment.presenter.AwayTeamPresenter;
import info.androidhive.firebase.fragments.awayTeamFragment.view.AwayTeamView;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;

public class AwayTeamFragment extends Fragment implements AwayTeamView{

    private RecyclerView recyclerView;
    private TextView msg;
    private RecyclerView.LayoutManager mLayoutManager;

    private AwayTeamPresenter awayTeamPresenter;
    public AwayTeamFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_away_team, container, false);

        awayTeamPresenter = new AwayTeamPresenter();
        awayTeamPresenter.setAwayTeamView(this);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_away_team_players);
        msg = (TextView)view.findViewById(R.id.textViewAwayMsg);
        mLayoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        msg.setVisibility(View.GONE);

        awayTeamPresenter.showAwayTeam();

        return view;
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
        Toast.makeText(getContext(), R.string.wait_sec, Toast.LENGTH_LONG).show();
    }
}