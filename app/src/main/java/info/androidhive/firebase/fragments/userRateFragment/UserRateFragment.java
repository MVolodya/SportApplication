package info.androidhive.firebase.fragments.userRateFragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Toast;

import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import info.androidhive.firebase.activity.mainActivity.MainActivity;
import info.androidhive.firebase.classes.managers.swipeManager.SwipeManager;
import info.androidhive.firebase.classes.models.DataHelper;
import info.androidhive.firebase.classes.models.Rate;
import info.androidhive.firebase.classes.recycleViewAdapters.ClickListener;
import info.androidhive.firebase.classes.recycleViewAdapters.DividerItemDecoration;
import info.androidhive.firebase.classes.recycleViewAdapters.RecyclerTouchListener;
import info.androidhive.firebase.classes.recycleViewAdapters.UsersRateAdapter;
import info.androidhive.firebase.classes.retrofit.rateMatch.RateMatchResponse;
import info.androidhive.firebase.R;
import info.androidhive.firebase.fragments.bottomSheetHelp.HelpFragment;
import info.androidhive.firebase.fragments.rateFragment.RateFragment;
import info.androidhive.firebase.fragments.userRateFragment.presenter.UserRateFragmentPresenter;
import info.androidhive.firebase.fragments.userRateFragment.view.UserRateView;

public class UserRateFragment extends Fragment implements UserRateView, SwipeRefreshLayout.OnRefreshListener {
    private View view;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private CircularProgressView mProgressView;
    private RateMatchResponse rateMatchResponse;
    private List<Rate> mRatedMatchesList;
    private UsersRateAdapter usersRateAdapter = new UsersRateAdapter();
    private RecyclerView mRecyclerView;
    private UserRateFragment userRateFragment = this;
    private UserRateFragmentPresenter userRateFragmentPresenter;

    public UserRateFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_user_rate, container, false);

        userRateFragmentPresenter = new UserRateFragmentPresenter();
        userRateFragmentPresenter.setUserRateView(this);

        mProgressView = (CircularProgressView) view.findViewById(R.id.user_rate_progress_view);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.user_rate_recycler_view);
        FloatingActionButton floatingActionButton = (FloatingActionButton) view.findViewById(R.id.help_floating_button);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.user_rate_refresh_layout);

        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeColors(Color.parseColor("#1976d2"),Color.parseColor("#628f3e"));
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(view.getContext());

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), mRecyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                DataHelper dataHelper = DataHelper.getInstance();
                dataHelper.setMatchId(Integer.parseInt(mRatedMatchesList
                        .get(position).getId()));
                Fragment fr = getActivity().getSupportFragmentManager().findFragmentById(R.id.nd_fragment_container);
                if (!(fr instanceof RateFragment)) {
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .setCustomAnimations(R.anim.enter_anim, R.anim.exit_anim)
                            .hide(userRateFragment)
                            .add(R.id.nd_fragment_container, new RateFragment())
                            .addToBackStack(null)
                            .commit();
                }
                ((MainActivity) view.getContext()).hideToolbar();
                ((MainActivity) view.getContext()).lockSwipe();
            }

            @Override
            public void onLongClick(int position) {}
        }));

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new HelpFragment().show(getFragmentManager(),
                        null);
            }
        });
        userRateFragmentPresenter.getUserRates(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
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

    @Override
    public void addList(List<Rate> rates) {
        ItemTouchHelper.Callback callback = new SwipeManager(usersRateAdapter,
                view, getContext(), rates);
        ItemTouchHelper helper = new ItemTouchHelper(callback);
        this.mRatedMatchesList = rates;
        mProgressView.stopAnimation();
        mProgressView.setVisibility(View.GONE);
        usersRateAdapter.setList(rates);
        mRecyclerView.setAdapter(usersRateAdapter);
        helper.attachToRecyclerView(mRecyclerView);
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onFail() {
        mSwipeRefreshLayout.setRefreshing(false);
        Toast.makeText(getContext(), R.string.wait_sec, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRateListSize() {
        mProgressView.stopAnimation();
        mProgressView.setVisibility(View.GONE);
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        mSwipeRefreshLayout.setRefreshing(true);
        usersRateAdapter.clear();
        userRateFragmentPresenter.getUserRates(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
    }
}
