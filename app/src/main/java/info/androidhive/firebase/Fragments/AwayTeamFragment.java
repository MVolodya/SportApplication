package info.androidhive.firebase.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import info.androidhive.firebase.Classes.Models.DataHelper;
import info.androidhive.firebase.Classes.RecycleViewAdapters.AwayTeamPlayerAdapter;
import info.androidhive.firebase.Classes.RecycleViewAdapters.DividerItemDecoration;
import info.androidhive.firebase.Classes.RecycleViewAdapters.HomeTeamPlayerAdapter;
import info.androidhive.firebase.Classes.Retrofit.ApiFactory;
import info.androidhive.firebase.Classes.Retrofit.Players.PlayersResponse;
import info.androidhive.firebase.Classes.Retrofit.Players.PlayersService;
import info.androidhive.firebase.R;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;

public class AwayTeamFragment extends Fragment implements Callback<PlayersResponse> {

    private RecyclerView recyclerView;
    private TextView msg;
    private RecyclerView.LayoutManager mLayoutManager;


    public AwayTeamFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_away_team, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_away_team_players);
        msg = (TextView)view.findViewById(R.id.textViewAwayMsg);
        mLayoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        msg.setVisibility(View.GONE);

        PlayersService service = ApiFactory.getPlayerService();
        Call<PlayersResponse> call = service.players(DataHelper.getInstance().getAwayTeamId());
        call.enqueue(this);


        // Inflate the layout for this fragment
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
}