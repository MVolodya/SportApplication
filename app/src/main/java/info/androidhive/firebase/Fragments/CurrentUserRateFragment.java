package info.androidhive.firebase.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;

import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import info.androidhive.firebase.Activities.MainActivity;
import info.androidhive.firebase.Classes.Models.DataHelper;
import info.androidhive.firebase.Classes.Models.Rate;
import info.androidhive.firebase.Classes.Models.RatedMatchesToDB;
import info.androidhive.firebase.Classes.Models.RatedUser;
import info.androidhive.firebase.Classes.RecycleViewAdapters.ClickListener;
import info.androidhive.firebase.Classes.RecycleViewAdapters.DividerItemDecoration;
import info.androidhive.firebase.Classes.RecycleViewAdapters.MatchAdapter;
import info.androidhive.firebase.Classes.RecycleViewAdapters.RecyclerTouchListener;
import info.androidhive.firebase.Classes.RecycleViewAdapters.UsersRateAdapter;
import info.androidhive.firebase.Classes.Retrofit.ApiFactory;

import info.androidhive.firebase.Classes.Retrofit.RateMatch.Fixture;
import info.androidhive.firebase.Classes.Retrofit.RateMatch.RateMatchResponse;
import info.androidhive.firebase.Classes.Retrofit.RateMatch.RateMatchService;
import info.androidhive.firebase.R;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class CurrentUserRateFragment extends Fragment {

    private View view;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private CircularProgressView progressView;
    private RateMatchResponse rateMatchResponse;
    private Fixture matches;


    public CurrentUserRateFragment() {
        // Required empty public constructor
    }

    public static CurrentUserRateFragment newInstance() {

        Bundle args = new Bundle();

        CurrentUserRateFragment fragment = new CurrentUserRateFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_cuurent_user_rate, container, false);

        progressView = (CircularProgressView) view.findViewById(R.id.progress_view_user_rate);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayoutRate);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_user_rate);
        mLayoutManager = new LinearLayoutManager(view.getContext());

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                DataHelper dataHelper = DataHelper.getInstance();
                dataHelper.setMatchId(Integer.parseInt(MatchAdapter.getMatchId(matches
                        .getLinks().getSelf().getHref())));
                dataHelper.setHomeTeamId(getTeamId(matches.getLinks().getHomeTeam().getHref()));
                dataHelper.setAwayTeamId(getTeamId(matches.getLinks().getAwayTeam().getHref()));

                Fragment fr =getActivity().getSupportFragmentManager().findFragmentById(R.id.container);

                if(!(fr instanceof RateFragment)){
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .setCustomAnimations(R.anim.enter_anim, R.anim.exit_anim)
                            .add(R.id.container, new RateFragment())
                            .addToBackStack(null)
                            .commit();
                }

                ((MainActivity)view.getContext()).hideToolbar();
                ((MainActivity)view.getContext()).lockSwipe();
            }

            @Override
            public void onLongClick(View view, int position) {}
        }));

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getUsersRates(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
            }
        });

        getUsersRates(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        FragmentManager fm = getFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            fm.popBackStack();
        }
        return super.onCreateAnimation(transit, enter, nextAnim);
    }


    private void getUsersRates(final String username) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

        progressView.startAnimation();

        mDatabase.child(username)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        final UsersRateAdapter usersRateAdapter = new UsersRateAdapter();
                                                        RatedUser ratedUser = dataSnapshot.getValue(RatedUser.class);
                                                        final List<RatedMatchesToDB> ratedMatchesList = ratedUser.getRatedMatches();

                                                        for (int i = 0; i < ratedMatchesList.size(); i++) {

                                                            RateMatchService service = ApiFactory.getRateMatchService();
                                                            Call<RateMatchResponse> call = service.match(Integer.parseInt(ratedMatchesList.get(i).getMatchId()));

                                                            final int finalI = i;

                                                            call.enqueue(new Callback<RateMatchResponse>() {
                                                                @Override
                                                                public void onResponse(Response<RateMatchResponse> response) {
                                                                    rateMatchResponse = response.body();
                                                                    matches = rateMatchResponse.getFixture();
                                                                    Rate rate = new Rate();
                                                                    rate.setHomeTeamName(rateMatchResponse.getFixture().getHomeTeamName());
                                                                    rate.setAwayTeamName(rateMatchResponse.getFixture().getAwayTeamName());
                                                                    rate.setPoints(ratedMatchesList.get(finalI).getPoints());
                                                                    rate.setType(ratedMatchesList.get(finalI).getTypeOfRate());
                                                                    usersRateAdapter.addRates(rate);
                                                                }

                                                                @Override
                                                                public void onFailure(Throwable t) {
                                                                }
                                                            });
                                                        }
                                                            progressView.stopAnimation();
                                                            progressView.setVisibility(View.GONE);
                                                            swipeRefreshLayout.setRefreshing(false);
                                                            recyclerView.setAdapter(usersRateAdapter);
                                                    }

                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {
                                                    }
                                                }

                );

    }
    private int getTeamId(String link) {
        Log.d("teamId", link);
        return Integer.parseInt(link.replaceAll("http://api.football-data.org/v1/teams/", ""));
    }

}
