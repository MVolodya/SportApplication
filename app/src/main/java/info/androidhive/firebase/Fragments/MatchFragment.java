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
import android.widget.Toast;

import com.github.fabtransitionactivity.SheetLayout;
import com.samsistemas.calendarview.widget.CalendarView;
import com.samsistemas.calendarview.widget.DayView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import info.androidhive.firebase.Classes.ConvertDate;
import info.androidhive.firebase.Classes.ProgressDialogManager;
import info.androidhive.firebase.Classes.RecycleViewClasses.DividerItemDecoration;
import info.androidhive.firebase.Classes.RecycleViewClasses.MatchAdapter;
import info.androidhive.firebase.Classes.Retrofit.ApiFactory;
import info.androidhive.firebase.Classes.Retrofit.Match.FixtResponse;
import info.androidhive.firebase.Classes.Retrofit.Match.Fixtures;
import info.androidhive.firebase.Classes.Retrofit.Match.MatchService;
import info.androidhive.firebase.R;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class MatchFragment extends Fragment implements Callback<FixtResponse>, View.OnClickListener,
        SheetLayout.OnFabAnimationEndListener,
        CalendarView.OnDateSelectedListener {

    private static final int REQUEST_CODE = 1;

    private FixtResponse fixtResponse;
    private RecyclerView recyclerView;
    private MatchAdapter mAdapter;
    private View view;
    private ProgressDialog progressDialog;
    private ProgressDialogManager dialogManager;
    private FloatingActionButton fabCalendar;
    private SheetLayout sheetLayout;
    private CalendarView calendarView;
    private RecyclerView.LayoutManager mLayoutManager;
    private MainFragment fragment;



    private String currentDate = getCurrentDate();

    public MatchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_match, container, false);

        fragment = (MainFragment)getActivity().getSupportFragmentManager().findFragmentByTag("main");

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_match);
        fabCalendar = (FloatingActionButton) view.findViewById(R.id.fabCalendar);
        sheetLayout = (SheetLayout) view.findViewById(R.id.bottom_sheet);
        calendarView = (CalendarView) view.findViewById(R.id.calendar_view);


        dialogManager = new ProgressDialogManager(getActivity(), progressDialog);
        dialogManager.showProgressDialog();

        mLayoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        MatchService service = ApiFactory.getMatchService();
        Call<FixtResponse> call = service.matches();
        call.enqueue(this);

        sheetLayout.setFab(fabCalendar);
        sheetLayout.setFabAnimationEndListener(this);
        fabCalendar.setOnClickListener(this);

        calendarView.setFirstDayOfWeek(Calendar.MONDAY);
        calendarView.setIsOverflowDateVisible(true);
        calendarView.setCurrentDay(new Date(System.currentTimeMillis()));
        calendarView.setBackButtonColor(R.color.colorAccent);
        calendarView.setNextButtonColor(R.color.colorAccent);
        calendarView.refreshCalendar(Calendar.getInstance(Locale.getDefault()));
        calendarView.setOnDateSelectedListener(this);

        final DayView dayView = calendarView.findViewByDate(new Date(System.currentTimeMillis()));
        if (null != dayView)
            Toast.makeText(view.getContext(), "Today is: " + dayView.getText().toString() +
                    "/" + calendarView.getCurrentMonth() +
                    "/" + calendarView.getCurrentYear(), Toast.LENGTH_SHORT).show();

        return view;
    }

    @Override
    public void onResponse(Response<FixtResponse> response) {

        List<Fixtures> matches;

        if (response.isSuccess()) {
            dialogManager.hideProgressDialog();
            fixtResponse = response.body();
            matches = getCorrectMatches(fixtResponse.getFixtures());
            mAdapter = new MatchAdapter(matches);

            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setAdapter(mAdapter);

            //mAdapter.notifyDataSetChanged();
            //mAdapter.swap(fixtResponse.getFixtures());
        }
    }

    @Override
    public void onFailure(Throwable t) {
        Log.d("Retrofit", "" + t);
        dialogManager.hideProgressDialog();
    }

    @Override
    public void onClick(View view) {
        fragment.hideTabs();

        sheetLayout.expandFab();
//        tabLayout.setVisibility(View.GONE);
    }

    @Override
    public void onFabAnimationEnd() {
    }

    @Override
    public void onDateSelected(@NonNull Date date) {

        dialogManager.showProgressDialog();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        currentDate = df.format(date);

        MatchService service = ApiFactory.getMatchService();
        Call<FixtResponse> call = service.matches();
        call.enqueue(this);

        fragment.showTabs();

        sheetLayout.contractFab();
    }

    private String getCurrentDate() {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String date = df.format(Calendar.getInstance().getTime());
        return date;
    }

    private List<Fixtures> getCorrectMatches(List<Fixtures> list) {

        ArrayList<Fixtures> listCorrect = new ArrayList<>();

        for (Fixtures f : list) {
            if (currentDate.equals(ConvertDate.getDate(f.getDate()))) {
                listCorrect.add(f);
            }
        }
        return listCorrect;
    }
}
