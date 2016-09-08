package info.androidhive.firebase.fragments.currentUserRateFragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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

import java.util.List;

import info.androidhive.firebase.activity.mainActivity.MainActivity;
import info.androidhive.firebase.classes.managers.SwipeManager;
import info.androidhive.firebase.classes.models.DataHelper;
import info.androidhive.firebase.classes.models.Rate;
import info.androidhive.firebase.classes.models.RatedMatchesToDB;
import info.androidhive.firebase.classes.models.RatedUser;
import info.androidhive.firebase.classes.recycleViewAdapters.ClickListener;
import info.androidhive.firebase.classes.recycleViewAdapters.DividerItemDecoration;
import info.androidhive.firebase.classes.recycleViewAdapters.RecyclerTouchListener;
import info.androidhive.firebase.classes.recycleViewAdapters.UsersRateAdapter;
import info.androidhive.firebase.classes.retrofit.ApiFactory;
import info.androidhive.firebase.classes.retrofit.rateMatch.RateMatchResponse;
import info.androidhive.firebase.classes.retrofit.rateMatch.RateMatchService;
import info.androidhive.firebase.R;
import info.androidhive.firebase.fragments.RateFragment;
import info.androidhive.firebase.fragments.currentUserRateFragment.presenter.UserRateFragmentPresenter;
import info.androidhive.firebase.fragments.currentUserRateFragment.view.UserRateView;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;

public class CurrentUserRateFragment extends Fragment implements UserRateView {
    private View view;
    private CircularProgressView progressView;
    private RateMatchResponse rateMatchResponse;
    private List<RatedMatchesToDB> ratedMatchesList;
    private UsersRateAdapter usersRateAdapter = new UsersRateAdapter();
    private SwipeManager swipeManager;
    private RecyclerView recyclerView;
    private CurrentUserRateFragment currentUserRateFragment = this;
    private UserRateFragmentPresenter userRateFragmentPresenter;

    public CurrentUserRateFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_cuurent_user_rate, container, false);

        userRateFragmentPresenter = new UserRateFragmentPresenter();
        userRateFragmentPresenter.setUserRateView(this);

        progressView = (CircularProgressView) view.findViewById(R.id.progress_view_user_rate);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_user_rate);

        swipeManager = new SwipeManager(getContext());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(view.getContext());

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity()));
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
                            .hide(currentUserRateFragment)
                            .add(R.id.container, new RateFragment())
                            .addToBackStack(null)
                            .commit();
                }

                ((MainActivity) view.getContext()).hideToolbar();
                ((MainActivity) view.getContext()).lockSwipe();
            }

            @Override
            public void onLongClick(int position) {}
        }));

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
    public void setRatedMathList(List<RatedMatchesToDB> ratedMatchesList) {
        this.ratedMatchesList = ratedMatchesList;
    }

    @Override
    public void addItemToList(Rate rate) {
        progressView.stopAnimation();
        progressView.setVisibility(View.GONE);
        usersRateAdapter.addRates(rate);
        recyclerView.setAdapter(usersRateAdapter);
        swipeManager.initSwipe(usersRateAdapter, recyclerView, view);
    }

    @Override
    public void onFail() {}
}
