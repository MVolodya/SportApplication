package info.androidhive.firebase.Fragments;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
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
import com.tuesda.walker.circlerefresh.CircleRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import info.androidhive.firebase.Activities.MainActivity;
import info.androidhive.firebase.Classes.Managers.SwipeManager;
import info.androidhive.firebase.Classes.Models.DataHelper;
import info.androidhive.firebase.Classes.Models.Rate;
import info.androidhive.firebase.Classes.Models.RatedMatchesToDB;
import info.androidhive.firebase.Classes.Models.RatedUser;
import info.androidhive.firebase.Classes.RecycleViewAdapters.ClickListener;
import info.androidhive.firebase.Classes.RecycleViewAdapters.DividerItemDecoration;
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
    private CircularProgressView progressView;
    private RateMatchResponse rateMatchResponse;
    private List<RatedMatchesToDB> ratedMatchesList;
    private UsersRateAdapter usersRateAdapter;
    private SwipeManager swipeManager;


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
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_user_rate);

        swipeManager = new SwipeManager(getContext());
        mLayoutManager = new LinearLayoutManager(view.getContext());

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {

                DataHelper dataHelper = DataHelper.getInstance();
                dataHelper.setMatchId(Integer.parseInt(ratedMatchesList
                        .get(position).getMatchId()));

                Fragment fr = getActivity().getSupportFragmentManager().findFragmentById(R.id.container);

                if (!(fr instanceof RateFragment)) {
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .setCustomAnimations(R.anim.enter_anim, R.anim.exit_anim)
                            .add(R.id.container, new RateFragment())
                            .addToBackStack(null)
                            .commit();
                }

                ((MainActivity) view.getContext()).hideToolbar();
                ((MainActivity) view.getContext()).lockSwipe();
            }

            @Override
            public void onLongClick(View view, int position) {
            }
        }));

        getUsersRates(FirebaseAuth.getInstance().getCurrentUser().getDisplayName(), recyclerView);
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


    private void getUsersRates(final String username,final RecyclerView recyclerView) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

        progressView.startAnimation();

        mDatabase.child(username)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                                        RatedUser ratedUser = dataSnapshot.getValue(RatedUser.class);
                                                        ratedMatchesList = ratedUser.getRatedMatches();
                                                        usersRateAdapter = new UsersRateAdapter();
                                                        if (ratedMatchesList != null) {
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
                                                                        rate.setStatus(ratedMatchesList.get(finalI).getStatus());
                                                                        usersRateAdapter.addRates(rate);
                                                                    }
                                                                    @Override
                                                                    public void onFailure(Throwable t) {
                                                                    }
                                                                });
                                                            }
                                                        }
                                                        progressView.stopAnimation();
                                                        progressView.setVisibility(View.GONE);
                                                        recyclerView.setAdapter(usersRateAdapter);
                                                        swipeManager.initSwipe(usersRateAdapter, recyclerView, view);
                                                    }

                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {
                                                    }
                                                }

                );

    }

}
