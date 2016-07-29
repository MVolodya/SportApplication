package info.androidhive.firebase.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import info.androidhive.firebase.Classes.RecycleViewClasses.LeagueAdapter;
import info.androidhive.firebase.Classes.Retrofit.ApiFactory;
import info.androidhive.firebase.Classes.Retrofit.League.LeagueService;
import info.androidhive.firebase.Classes.Retrofit.League.LeagueModel;
import info.androidhive.firebase.R;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class LeagueFragment extends Fragment implements Callback<List<LeagueModel>> {

    private List<LeagueModel> leagueList = new ArrayList<>();
    private RecyclerView recyclerView;
    private LeagueAdapter mAdapter;
    private View view;

    public LeagueFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_league, container, false);

        LeagueService service = ApiFactory.getFootballService();
        Call<List<LeagueModel>> call = service.leagues();
        call.enqueue(this);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);




        //prepareMovieData();

        return view;
    }

    @Override
    public void onResponse(Response<List<LeagueModel>> response) {
        if (response.isSuccess()) {
            leagueList = response.body();

            mAdapter = new LeagueAdapter(leagueList);

            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(view.getContext());
            recyclerView.setLayoutManager(mLayoutManager);
           // recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(mAdapter);
            //Log.d("Retrofit", football.get(0).getId()+ " " + football.get(0).getCaption());
        }
    }

    @Override
    public void onFailure(Throwable t) {}


    private void prepareMovieData() {
        mAdapter.notifyDataSetChanged();
    }
}
