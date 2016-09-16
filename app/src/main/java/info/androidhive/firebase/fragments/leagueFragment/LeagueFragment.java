package info.androidhive.firebase.fragments.leagueFragment;


import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import info.androidhive.firebase.activity.mainActivity.MainActivity;
import info.androidhive.firebase.classes.managers.ProgressDialogManager;
import info.androidhive.firebase.classes.recycleViewAdapters.ClickListener;
import info.androidhive.firebase.classes.recycleViewAdapters.DividerItemDecoration;
import info.androidhive.firebase.classes.recycleViewAdapters.LeagueAdapter;
import info.androidhive.firebase.classes.recycleViewAdapters.RecyclerTouchListener;
import info.androidhive.firebase.classes.retrofit.league.LeagueModel;
import info.androidhive.firebase.fragments.leagueTableFragment.LeagueTableFragment;
import info.androidhive.firebase.R;
import info.androidhive.firebase.fragments.leagueFragment.presenter.LeagueFragmentPresenter;
import info.androidhive.firebase.fragments.leagueFragment.view.LeagueView;

public class LeagueFragment extends Fragment implements LeagueView, SwipeRefreshLayout.OnRefreshListener {

    private List<LeagueModel> leagueList = new ArrayList<>();
    private ProgressDialog progressDialog;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView textViewMsg;
    private LeagueFragmentPresenter leagueFragmentPresenter;

    public LeagueFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_league, container, false);

        leagueFragmentPresenter = new LeagueFragmentPresenter();
        leagueFragmentPresenter.setLeagueView(this);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_league);
        textViewMsg = (TextView)view.findViewById(R.id.textViewLimitMsg);
        swipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.refreshLeagueFragment);
        progressDialog = new ProgressDialog(view.getContext());

        textViewMsg.setVisibility(View.GONE);

        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeColors(Color.parseColor("#1976d2"),Color.parseColor("#628f3e"));

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        ProgressDialogManager.showProgressDialog(progressDialog,view.getContext().getString(R.string.loading));
        leagueFragmentPresenter.sendRequest();

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new ClickListener() {

            @Override
            public void onClick(View view, int position) {
                String name = leagueList.get(position).getCaption();
                int leagueId = Integer.parseInt(leagueList.get(position).getId());
                leagueFragmentPresenter.setLeagueData(leagueId, name);

                getActivity().getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.enter_anim, R.anim.exit_anim)
                        .add(R.id.nd_fragment_container, new LeagueTableFragment())
                        .addToBackStack(null)
                        .commit();

                ((MainActivity)view.getContext()).hideToolbar();
                ((MainActivity)view.getContext()).lockSwipe();
            }

            @Override
            public void onLongClick(int position) {}
        }));
        return view;
    }

    @Override
    public void onSuccess(List<LeagueModel> leagueList) {
        this.leagueList = leagueList;
        recyclerView.setVisibility(View.VISIBLE);
        textViewMsg.setVisibility(View.GONE);
        swipeRefreshLayout.setRefreshing(false);
        ProgressDialogManager.hideProgressDialog(progressDialog);
        LeagueAdapter mAdapter = new LeagueAdapter(leagueList);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onFail() {
        textViewMsg.setVisibility(View.VISIBLE);
        ProgressDialogManager.hideProgressDialog(progressDialog);
        swipeRefreshLayout.setRefreshing(false);
        recyclerView.setVisibility(View.GONE);
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        leagueFragmentPresenter.sendRequest();
    }
}
