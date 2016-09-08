package info.androidhive.firebase.fragments.matchFragment;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import info.androidhive.firebase.activity.mainActivity.MainActivity;
import info.androidhive.firebase.classes.managers.DataGetter;
import info.androidhive.firebase.classes.managers.ProgressDialogManager;
import info.androidhive.firebase.classes.recycleViewAdapters.ClickListener;
import info.androidhive.firebase.classes.recycleViewAdapters.DividerItemDecoration;
import info.androidhive.firebase.classes.recycleViewAdapters.MatchAdapter;
import info.androidhive.firebase.classes.recycleViewAdapters.RecyclerTouchListener;
import info.androidhive.firebase.classes.retrofit.match.Fixture;
import info.androidhive.firebase.classes.retrofit.match.MatchResponse;
import info.androidhive.firebase.R;
import info.androidhive.firebase.fragments.rateFragment.RateFragment;
import info.androidhive.firebase.fragments.mainFragment.MainFragment;
import info.androidhive.firebase.fragments.matchFragment.presenter.MatchFragmentPresenter;
import info.androidhive.firebase.fragments.matchFragment.view.MatchFragmentView;

public class MatchFragment extends Fragment implements View.OnClickListener,
        SheetLayout.OnFabAnimationEndListener,
        CalendarView.OnDateSelectedListener, MatchFragmentView {

    private TextView msg;
    private ImageView homeTeam;
    private ImageView awayTeam;
    private RecyclerView recyclerView;
    private MatchAdapter mAdapter;
    private ProgressDialog progressDialog;
    private SheetLayout sheetLayout;
    private RecyclerView.LayoutManager mLayoutManager;
    private MainFragment fragment;
    private List<Fixture> matches;
    private CircularProgressView circularProgressView;
    private FloatingActionButton fabCalendar;
    private ImageView backImageView;
    private CalendarView calendarView;

    private MatchFragmentPresenter matchFragmentPresenter;
    private MatchFragment matchFragment = this;
    private String currentDate = new DataGetter().getCurrentDate();

    public MatchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_match, container, false);

        matchFragmentPresenter = new MatchFragmentPresenter();
        matchFragmentPresenter.setMatchFragmentView(this);
        fragment = (MainFragment) getActivity().getSupportFragmentManager().findFragmentByTag("main");

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_match);
        fabCalendar = (FloatingActionButton) view.findViewById(R.id.fabCalendar);
        sheetLayout = (SheetLayout) view.findViewById(R.id.bottom_sheet);
        msg = (TextView) view.findViewById(R.id.textViewMsg);
        backImageView = (ImageView) view.findViewById(R.id.imageViewBackButton);
        calendarView = (CalendarView) view.findViewById(R.id.calendar_view);
        progressDialog = new ProgressDialog(view.getContext());
        circularProgressView = (CircularProgressView) view.findViewById(R.id.progress_view_match);

        mLayoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        msg.setVisibility(View.GONE);

        ProgressDialogManager.showProgressDialog(progressDialog, "Loading");
        matchFragmentPresenter.getMatchList();
        initCalendar();

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new ClickListener() {

            @Override
            public void onClick(View view, int position) {

                matchFragmentPresenter.saveMatchData(matches, position);

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

        return view;
    }

    private void initCalendar() {
        sheetLayout.setFab(fabCalendar);
        sheetLayout.setFabAnimationEndListener(this);
        fabCalendar.setOnClickListener(this);
        backImageView.setOnClickListener(this);

        calendarView.setFirstDayOfWeek(Calendar.MONDAY);
        calendarView.setIsOverflowDateVisible(true);
        calendarView.setCurrentDay(new Date(System.currentTimeMillis()));
        calendarView.setBackButtonColor(R.color.white);
        calendarView.setNextButtonColor(R.color.white);
        calendarView.setCalendarTitleTextColor(Color.parseColor("#ffffff"));
        calendarView.setSelectedDayBackground(Color.parseColor("#26ae90"));
        calendarView.refreshCalendar(Calendar.getInstance(Locale.getDefault()));
        calendarView.setOnDateSelectedListener(this);
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
    public void onFabAnimationEnd() {}

    @Override
    public void onDateSelected(@NonNull Date date) {

        ProgressDialogManager.showProgressDialog(progressDialog, "Loading");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        currentDate = df.format(date);
        matches = new ArrayList<>();
        mAdapter = new MatchAdapter(matches);
        recyclerView.setAdapter(mAdapter);

        circularProgressView.setVisibility(View.VISIBLE);
        circularProgressView.startAnimation();
        msg.setVisibility(View.GONE);
        matchFragmentPresenter.getMatchList();
        sheetLayout.contractFab();
        fragment.showTabs();
        fragment.getViewPager().setPagingEnabled(true);
    }


    @Override
    public void onSuccess(MatchResponse matchResponse) {
        ProgressDialogManager.hideProgressDialog(progressDialog);
        matches = new DataGetter().getCorrectMatches(matchResponse.getFixtures(), currentDate);
        mAdapter = new MatchAdapter(matches);

        if (matches.size() == 0) {
            msg.setVisibility(View.VISIBLE);
        }

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mAdapter);

        circularProgressView.stopAnimation();
        circularProgressView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onFail() {
        ProgressDialogManager.hideProgressDialog(progressDialog);
    }
}
