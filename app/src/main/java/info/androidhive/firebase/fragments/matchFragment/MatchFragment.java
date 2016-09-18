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
import android.widget.Button;
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

    private TextView msgTv;
    private Button backBtn;
    private ImageView homeTeamTv;
    private ImageView awayTeamTv;
    private ImageView msgTvImg;
    private RecyclerView mRecyclerView;
    private MatchAdapter mAdapter;
    private ProgressDialog progressDialog;
    private SheetLayout sheetLayout;
    private RecyclerView.LayoutManager mLayoutManager;
    private MainFragment fragment;
    private List<Fixture> matches;
    private CircularProgressView circularProgressView;
    private FloatingActionButton calendarFab;
    private CalendarView calendarView;
    private View view;

    private MatchFragmentPresenter matchFragmentPresenter;
    private MatchFragment matchFragment = this;
    private String mCurrentDate = new DataGetter().getCurrentDate();

    public MatchFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_match, container, false);

        matchFragmentPresenter = new MatchFragmentPresenter();
        matchFragmentPresenter.setMatchFragmentView(this);
        fragment = (MainFragment) getActivity().getSupportFragmentManager().findFragmentByTag("main");

        mRecyclerView = (RecyclerView) view.findViewById(R.id.match_recycler_view);
        calendarFab = (FloatingActionButton) view.findViewById(R.id.calendar_fab);
        sheetLayout = (SheetLayout) view.findViewById(R.id.bottom_sheet);
        msgTv = (TextView) view.findViewById(R.id.msg_tv);
        msgTvImg = (ImageView) view.findViewById(R.id.msg_tv_img);
        backBtn = (Button) view.findViewById(R.id.back_btn);
        calendarView = (CalendarView) view.findViewById(R.id.calendar_view);
        progressDialog = new ProgressDialog(view.getContext());
        circularProgressView = (CircularProgressView) view.findViewById(R.id.match_progress_view);

        mLayoutManager = new LinearLayoutManager(view.getContext());
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        msgTv.setVisibility(View.GONE);
        msgTvImg.setVisibility(View.GONE);
        backBtn.setOnClickListener(this);

        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), mRecyclerView, new ClickListener() {

            @Override
            public void onClick(View view, int position) {
                matchFragmentPresenter.saveMatchData(matches, position);
                Fragment fr = getActivity().getSupportFragmentManager().findFragmentById(R.id.nd_fragment_container);
                if (!(fr instanceof RateFragment)) {
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .setCustomAnimations(R.anim.enter_anim, R.anim.exit_anim)
                            .hide(matchFragment)
                            .add(R.id.nd_fragment_container, new RateFragment())
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

        ProgressDialogManager.showProgressDialog(progressDialog, view.getContext().getString(R.string.loading));
        matchFragmentPresenter.getMatchList();
        initCalendar();

        return view;
    }

    private void initCalendar() {
        sheetLayout.setFab(calendarFab);
        sheetLayout.setFabAnimationEndListener(this);
        calendarFab.setOnClickListener(this);

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
            case R.id.calendar_fab:
                backBtn.setVisibility(View.VISIBLE);
                sheetLayout.expandFab();
                fragment.hideTabs();
                fragment.getViewPager().setPagingEnabled(false);
                break;
            case R.id.back_btn:
                backBtn.setVisibility(View.GONE);
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

        ProgressDialogManager.showProgressDialog(progressDialog,view.getContext().getString(R.string.loading));
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        mCurrentDate = df.format(date);
        matches = new ArrayList<>();
        mAdapter = new MatchAdapter(matches);
        mRecyclerView.setAdapter(mAdapter);

        circularProgressView.setVisibility(View.VISIBLE);
        circularProgressView.startAnimation();
        msgTv.setVisibility(View.GONE);
        msgTvImg.setVisibility(View.GONE);
        matchFragmentPresenter.getMatchList();
        sheetLayout.contractFab();
        fragment.showTabs();
        fragment.getViewPager().setPagingEnabled(true);
    }


    @Override
    public void onSuccess(MatchResponse matchResponse) {
        ProgressDialogManager.hideProgressDialog(progressDialog);
        matches = new DataGetter().getCorrectMatches(matchResponse.getFixtures(), mCurrentDate);
        mAdapter = new MatchAdapter(matches);

        if (matches.size() == 0) {
            msgTv.setText(getContext().getString(R.string.no_matches_today));
            msgTv.setVisibility(View.VISIBLE);
            msgTvImg.setVisibility(View.VISIBLE);
        }

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        circularProgressView.stopAnimation();
        circularProgressView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onFail() {
        ProgressDialogManager.hideProgressDialog(progressDialog);
        circularProgressView.stopAnimation();
        circularProgressView.setVisibility(View.INVISIBLE);
        msgTv.setText(getContext().getString(R.string.wait_sec));
        msgTv.setVisibility(View.VISIBLE);

    }
}
