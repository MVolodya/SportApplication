package info.androidhive.firebase.Fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.fabtransitionactivity.SheetLayout;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.samsistemas.calendarview.widget.CalendarView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import info.androidhive.firebase.Activities.MainActivity;
import info.androidhive.firebase.Classes.Managers.DataGetter;
import info.androidhive.firebase.Classes.Models.DataHelper;
import info.androidhive.firebase.Classes.Managers.ProgressDialogManager;
import info.androidhive.firebase.Classes.RecycleViewAdapters.ClickListener;
import info.androidhive.firebase.Classes.RecycleViewAdapters.DividerItemDecoration;
import info.androidhive.firebase.Classes.RecycleViewAdapters.MatchAdapter;
import info.androidhive.firebase.Classes.RecycleViewAdapters.RecyclerTouchListener;
import info.androidhive.firebase.Classes.Retrofit.ApiFactory;
import info.androidhive.firebase.Classes.Retrofit.Match.Fixture;
import info.androidhive.firebase.Classes.Retrofit.Match.MatchResponse;
import info.androidhive.firebase.Classes.Retrofit.Match.MatchService;
import info.androidhive.firebase.R;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class MatchFragment extends Fragment implements Callback<MatchResponse>, View.OnClickListener,
        SheetLayout.OnFabAnimationEndListener,
        CalendarView.OnDateSelectedListener {

    private TextView msg;
    private RecyclerView recyclerView;
    private MatchAdapter mAdapter;
    private ProgressDialog progressDialog;
    private SheetLayout sheetLayout;
    private RecyclerView.LayoutManager mLayoutManager;
    private MainFragment fragment;
    private List<Fixture> matches;
    private CircularProgressView circularProgressView;
    private MatchFragment matchFragment = this;

    private String currentDate = new DataGetter().getCurrentDate();

    public MatchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_match, container, false);

        fragment = (MainFragment) getActivity().getSupportFragmentManager().findFragmentByTag("main");

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_match);
        FloatingActionButton fabCalendar = (FloatingActionButton) view.findViewById(R.id.fabCalendar);
        sheetLayout = (SheetLayout) view.findViewById(R.id.bottom_sheet);
        msg = (TextView) view.findViewById(R.id.textViewMsg);
        ImageView backImageView = (ImageView) view.findViewById(R.id.imageViewBackButton);
        CalendarView calendarView = (CalendarView) view.findViewById(R.id.calendar_view);
        progressDialog = new ProgressDialog(view.getContext());
        circularProgressView = (CircularProgressView) view.findViewById(R.id.progress_view_match);

        msg.setVisibility(View.GONE);

        ProgressDialogManager.showProgressDialog(progressDialog,"Loading");

        mLayoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new ClickListener() {

            @Override
            public void onClick(View view, int position) {

                DataHelper dataHelper = DataHelper.getInstance();
                dataHelper.setMatchId(Integer.parseInt(MatchAdapter.getMatchId(matches.get(position)
                        .getLinks().getSelf().getHref())));
                dataHelper.setHomeTeamId(new DataGetter().getTeamId(matches.get(position)
                        .getLinks().getHomeTeam().getHref()));
                dataHelper.setAwayTeamId(new DataGetter().getTeamId(matches.get(position)
                        .getLinks().getAwayTeam().getHref()));

                Fragment fr = getActivity().getSupportFragmentManager().findFragmentById(R.id.container);

                if (!(fr instanceof RateFragment)) {
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .setCustomAnimations(R.anim.enter_anim, R.anim.exit_anim)
                            .hide(matchFragment)
                            .add(R.id.container, new RateFragment())
                            .addToBackStack(null)
                            .commit();
                }

                ((MainActivity) view.getContext()).hideToolbar();
                ((MainActivity) view.getContext()).lockSwipe();
            }

            @Override
            public void onLongClick(int position) {
            }
        }));

        MatchService service = ApiFactory.getMatchService();
        Call<MatchResponse> call = service.matches();
        call.enqueue(this);


        sheetLayout.setFab(fabCalendar);
        sheetLayout.setFabAnimationEndListener(this);
        fabCalendar.setOnClickListener(this);
        backImageView.setOnClickListener(this);

        calendarView.setFirstDayOfWeek(Calendar.MONDAY);
        calendarView.setIsOverflowDateVisible(true);
        calendarView.setCurrentDay(new Date(System.currentTimeMillis()));
        calendarView.setBackButtonColor(R.color.colorAccent);
        calendarView.setNextButtonColor(R.color.colorAccent);
        calendarView.refreshCalendar(Calendar.getInstance(Locale.getDefault()));
        calendarView.setOnDateSelectedListener(this);

        return view;
    }

    @Override
    public void onResponse(Response<MatchResponse> response) {
        if (response.isSuccess()) {
            ProgressDialogManager.hideProgressDialog(progressDialog);
            MatchResponse matchResponse = response.body();
            matches = new DataGetter().getCorrectMatches(matchResponse.getFixtures(), currentDate);
            mAdapter = new MatchAdapter(matches);

            if(matches.size()==0){
                msg.setVisibility(View.VISIBLE);
            }

            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setAdapter(mAdapter);

            circularProgressView.stopAnimation();
            circularProgressView.setVisibility(View.INVISIBLE);
            //mAdapter.notifyDataSetChanged();
            //mAdapter.swap(matchResponse.getFixtures());
        }
    }

    @Override
    public void onFailure(Throwable t) {
        Log.d("Retrofit", "" + t);
        ProgressDialogManager.hideProgressDialog(progressDialog);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.fabCalendar:
                sheetLayout.expandFab();
                fragment.hideTabs();
                fragment.getViewPager().setPagingEnabled(false);
                break;
            case R.id.imageViewBackButton:
                sheetLayout.contractFab();
                fragment.showTabs();
                fragment.getViewPager().setPagingEnabled(true);
                break;
        }
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        if (!enter) {

        }
        return super.onCreateAnimation(transit, enter, nextAnim);
    }


    @Override
    public void onFabAnimationEnd() {
    }

    @Override
    public void onDateSelected(@NonNull Date date) {

        ProgressDialogManager.showProgressDialog(progressDialog,"Loading");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        currentDate = df.format(date);

        matches = new ArrayList<>();
        mAdapter = new MatchAdapter(matches);
        recyclerView.setAdapter(mAdapter);

        circularProgressView.setVisibility(View.VISIBLE);
        circularProgressView.startAnimation();
        msg.setVisibility(View.GONE);
        MatchService service = ApiFactory.getMatchService();
        Call<MatchResponse> call = service.matches();
        call.enqueue(this);

        sheetLayout.contractFab();
        fragment.showTabs();
        fragment.getViewPager().setPagingEnabled(true);
    }

}
