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
import android.widget.ImageView;
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

    private List<LeagueModel> mLeagueList = new ArrayList<>();
    private ProgressDialog progressDialog;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private TextView msgTv;
    private ImageView msgTvImg;
    private LeagueFragmentPresenter leagueFragmentPresenter;

    public LeagueFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_league, container, false);

        leagueFragmentPresenter = new LeagueFragmentPresenter();
        leagueFragmentPresenter.setLeagueView(this);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.league_recycler_view);
        msgTv = (TextView)view.findViewById(R.id.limit_msg_tv);
        msgTvImg = (ImageView) view.findViewById(R.id.msg_tv_img_wait);
        mSwipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.refreshLeagueFragment);
        progressDialog = new ProgressDialog(view.getContext());

        msgTv.setVisibility(View.GONE);
        msgTvImg.setVisibility(View.GONE);

        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeColors(Color.parseColor("#1976d2"),Color.parseColor("#628f3e"));

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(view.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), mRecyclerView, new ClickListener() {

            @Override
            public void onClick(View view, int position) {
                String name = mLeagueList.get(position).getCaption();
                int leagueId = Integer.parseInt(mLeagueList.get(position).getId());
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

        ProgressDialogManager.showProgressDialog(progressDialog,view.getContext().getString(R.string.loading));
        leagueFragmentPresenter.sendRequest();

        return view;
    }

    @Override
    public void onSuccess(List<LeagueModel> leagueList) {
        this.mLeagueList = leagueList;
        mRecyclerView.setVisibility(View.VISIBLE);
        msgTv.setVisibility(View.GONE);
        msgTvImg.setVisibility(View.GONE);
        mSwipeRefreshLayout.setRefreshing(false);
        ProgressDialogManager.hideProgressDialog(progressDialog);
        LeagueAdapter mAdapter = new LeagueAdapter(leagueList);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onFail() {
        msgTv.setVisibility(View.VISIBLE);
        msgTvImg.setVisibility(View.VISIBLE);
        ProgressDialogManager.hideProgressDialog(progressDialog);
        mSwipeRefreshLayout.setRefreshing(false);
        mRecyclerView.setVisibility(View.GONE);
    }

    @Override
    public void onRefresh() {
        mSwipeRefreshLayout.setRefreshing(true);
        leagueFragmentPresenter.sendRequest();
    }
}
