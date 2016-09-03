package info.androidhive.firebase.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import info.androidhive.firebase.Classes.Models.DataHelper;
import info.androidhive.firebase.Classes.RecycleViewAdapters.DividerItemDecoration;
import info.androidhive.firebase.Classes.RecycleViewAdapters.HomeTeamPlayerAdapter;
import info.androidhive.firebase.Classes.Retrofit.ApiFactory;
import info.androidhive.firebase.Classes.Retrofit.Players.PlayersResponse;
import info.androidhive.firebase.Classes.Retrofit.Players.PlayersService;
import info.androidhive.firebase.R;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeTeamFragment extends Fragment implements Callback<PlayersResponse> {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager mLayoutManager;


    public HomeTeamFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_team, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_home_team_players);
        mLayoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        PlayersService service = ApiFactory.getPlayerService();
        Call<PlayersResponse> call = service.players(DataHelper.getInstance().getHomeTeamId());
        call.enqueue(this);


        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onResponse(Response<PlayersResponse> response) {
        if (response.isSuccess()) {
            PlayersResponse playersResponse = response.body();
            recyclerView.setLayoutManager(mLayoutManager);
            HomeTeamPlayerAdapter homeTeamPlayerAdapter = new HomeTeamPlayerAdapter(playersResponse.getPlayers());
            recyclerView.setAdapter(homeTeamPlayerAdapter);
        }
    }

    @Override
    public void onFailure(Throwable t) {

    }
}
