package info.androidhive.firebase.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import info.androidhive.firebase.Classes.Rate;
import info.androidhive.firebase.Classes.RatedMatches;
import info.androidhive.firebase.Classes.RatedUser;
import info.androidhive.firebase.Classes.RecycleViewClasses.AllUsersAdapter;
import info.androidhive.firebase.Classes.RecycleViewClasses.DividerItemDecoration;
import info.androidhive.firebase.Classes.RecycleViewClasses.UsersRateAdapter;
import info.androidhive.firebase.Classes.Retrofit.ApiFactory;
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
    private UsersRateAdapter usersRateAdapter;


    public CurrentUserRateFragment() {
        // Required empty public constructor
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
        if (!enter) {
            //leaving fragment
            getFragmentManager().popBackStack();
        }
        return super.onCreateAnimation(transit, enter, nextAnim);
    }


    private void getUsersRates(final String username) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        final List<Rate> currentUserRateList = new ArrayList<>();

        progressView.startAnimation();



        mDatabase.child(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                RatedUser ratedUser = dataSnapshot.getValue(RatedUser.class);
                final List<RatedMatches> ratedMatchesList = ratedUser.getRatedMatches();

                for (int i = 0; i < ratedMatchesList.size(); i++) {

                    RateMatchService service = ApiFactory.getRateMatchService();
                    Call<RateMatchResponse> call = service.match(Integer.parseInt(ratedMatchesList.get(i).getMatchId()));

                    final int finalI = i;

                    call.enqueue(new Callback<RateMatchResponse>() {
                        @Override
                        public void onResponse(Response<RateMatchResponse> response) {
                            rateMatchResponse = response.body();
                            Rate rate = new Rate();
                            rate.setHomeTeamName(rateMatchResponse.getFixture().getHomeTeamName());
                            rate.setAwayTeamName(rateMatchResponse.getFixture().getAwayTeamName());
                            rate.setPoints(ratedMatchesList.get(finalI).getPoints());
                            rate.setType(ratedMatchesList.get(finalI).getTypeOfRate());
                            currentUserRateList.add(rate);
                        }
                    @Override
                    public void onFailure(Throwable t) {
                    }
                });
                    progressView.stopAnimation();
                    progressView.setVisibility(View.GONE);
                    swipeRefreshLayout.setRefreshing(false);

                    usersRateAdapter = new UsersRateAdapter(currentUserRateList);
                    recyclerView.setAdapter(usersRateAdapter);
                    }
                }

                @Override
                public void onCancelled (DatabaseError databaseError){}
            }

            );

        }

    }
