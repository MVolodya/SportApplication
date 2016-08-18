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
import android.widget.ImageView;
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

import info.androidhive.firebase.Activities.MainActivity;
import info.androidhive.firebase.Classes.ConvertDate;
import info.androidhive.firebase.Classes.DataHelper;
import info.androidhive.firebase.Classes.ProgressDialogManager;
import info.androidhive.firebase.Classes.RecycleViewClasses.ClickListener;
import info.androidhive.firebase.Classes.RecycleViewClasses.DividerItemDecoration;
import info.androidhive.firebase.Classes.RecycleViewClasses.MatchAdapter;
import info.androidhive.firebase.Classes.RecycleViewClasses.RecyclerTouchListener;
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
    private ImageView backImageView;
    private List<Fixture> matches;



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
        backImageView = (ImageView) view.findViewById(R.id.imageViewBackButton);
        calendarView = (CalendarView) view.findViewById(R.id.calendar_view);


        dialogManager = new ProgressDialogManager(getActivity(), progressDialog);
        dialogManager.showProgressDialog();

        mLayoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new ClickListener() {

            @Override
            public void onClick(View view, int position) {

                DataHelper dataHelper = DataHelper.getInstance();
                dataHelper.setMatchId(Integer.parseInt(MatchAdapter.getMatchId(matches.get(position)
                        .getLinks().getSelf().getHref())));

                Fragment fr =getActivity().getSupportFragmentManager().findFragmentById(R.id.container);

                if(!(fr instanceof RateFragment)){
                    getActivity().getSupportFragmentManager().beginTransaction()
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

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .add(R.id.container, new MatchRateFragment())
                        .addToBackStack(null)
                        .commit();


            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));


        final DayView dayView = calendarView.findViewByDate(new Date(System.currentTimeMillis()));
        if (null != dayView)
            Toast.makeText(view.getContext(), "Today is: " + dayView.getText().toString() +
                    "/" + calendarView.getCurrentMonth() +
                    "/" + calendarView.getCurrentYear(), Toast.LENGTH_SHORT).show();
        return view;
    }

    @Override
    public void onResponse(Response<MatchResponse> response) {



        if (response.isSuccess()) {
            dialogManager.hideProgressDialog();
            MatchResponse matchResponse = response.body();
            matches = getCorrectMatches(matchResponse.getFixtures());
            mAdapter = new MatchAdapter(matches);

            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setAdapter(mAdapter);

            //mAdapter.notifyDataSetChanged();
            //mAdapter.swap(matchResponse.getFixtures());
        }
    }

    @Override
    public void onFailure(Throwable t) {
        Log.d("Retrofit", "" + t);
        dialogManager.hideProgressDialog();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
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
    public void onFabAnimationEnd() {
    }

    @Override
    public void onDateSelected(@NonNull Date date) {

        dialogManager.showProgressDialog();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        currentDate = df.format(date);

        MatchService service = ApiFactory.getMatchService();
        Call<MatchResponse> call = service.matches();
        call.enqueue(this);

        sheetLayout.contractFab();

        fragment.showTabs();

        fragment.getViewPager().setPagingEnabled(true);

    }

    private String getCurrentDate() {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String date = df.format(Calendar.getInstance().getTime());
        return date;
    }

    private List<Fixture> getCorrectMatches(List<Fixture> list) {

        ArrayList<Fixture> listCorrect = new ArrayList<>();

        for (Fixture f : list) {
            if (currentDate.equals(ConvertDate.getDate(f.getDate()))) {
                listCorrect.add(f);
            }
        }
        return listCorrect;
    }
}
