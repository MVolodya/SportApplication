package info.androidhive.firebase.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import info.androidhive.firebase.Classes.DataHelper;
import info.androidhive.firebase.Classes.RecycleViewClasses.AwayTeamPlayerAdapter;
import info.androidhive.firebase.Classes.RecycleViewClasses.DividerItemDecoration;
import info.androidhive.firebase.Classes.RecycleViewClasses.HomeTeamPlayerAdapter;
import info.androidhive.firebase.Classes.Retrofit.ApiFactory;
import info.androidhive.firebase.Classes.Retrofit.Players.Player;
import info.androidhive.firebase.Classes.Retrofit.Players.PlayersResponse;
import info.androidhive.firebase.Classes.Retrofit.Players.PlayersService;
import info.androidhive.firebase.R;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;

public class AwayTeamFragment extends Fragment implements Callback<PlayersResponse> {

    private View view;
    private PlayersResponse playersResponse;
    private RecyclerView recyclerView;
    private AwayTeamPlayerAdapter awayTeamPlayerAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


    public AwayTeamFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_away_team, container, false);
        recyclerView = (RecyclerView)view.findViewById(R.id.recycler_view_away_team_players);
        mLayoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        PlayersService service = ApiFactory.getPlayerService();
        Call<PlayersResponse> call = service.players(DataHelper.getInstance().getAwayTeamId());
        call.enqueue(this);


        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onResponse(Response<PlayersResponse> response) {
        if (response.isSuccess()) {
            playersResponse = response.body();
            recyclerView.setLayoutManager(mLayoutManager);
            awayTeamPlayerAdapter = new AwayTeamPlayerAdapter(playersResponse.getPlayers());
            recyclerView.setAdapter(awayTeamPlayerAdapter);
        }
    }

    @Override
    public void onFailure(Throwable t) {

    }
}