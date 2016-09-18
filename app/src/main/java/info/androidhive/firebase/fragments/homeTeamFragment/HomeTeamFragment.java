package info.androidhive.firebase.fragments.homeTeamFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import info.androidhive.firebase.R;
import info.androidhive.firebase.classes.recycleViewAdapters.DividerItemDecoration;
import info.androidhive.firebase.classes.recycleViewAdapters.HomeTeamPlayerAdapter;
import info.androidhive.firebase.classes.retrofit.players.PlayersResponse;
import info.androidhive.firebase.fragments.homeTeamFragment.presenter.HomeTeamPresenter;
import info.androidhive.firebase.fragments.homeTeamFragment.view.HomeTeamView;

public class HomeTeamFragment extends Fragment implements HomeTeamView{

    private RecyclerView mRecyclerView;
    private TextView msgTv;
    private ImageView msgTvImg;
    private RecyclerView.LayoutManager mLayoutManager;

    public HomeTeamFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_team, container, false);
        HomeTeamPresenter homeTeamPresenter = new HomeTeamPresenter();
        homeTeamPresenter.setHomeTeamView(this);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.home_team_players_recycler_view);
        msgTv = (TextView)view.findViewById(R.id.home_msg_tv);
        msgTvImg = (ImageView)view.findViewById(R.id.msg_tv_img_bugs);
        mLayoutManager = new LinearLayoutManager(view.getContext());

        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        msgTv.setVisibility(View.GONE);
        msgTvImg.setVisibility(View.GONE);

        homeTeamPresenter.showHomeTeam();
        return view;
    }

    @Override
    public void onSuccess(PlayersResponse playersResponse) {
        if (playersResponse.getPlayers().size() == 0) {
            msgTv.setVisibility(View.VISIBLE);
            msgTvImg.setVisibility(View.VISIBLE);
        } else {
            mRecyclerView.setLayoutManager(mLayoutManager);
            HomeTeamPlayerAdapter homeTeamPlayerAdapter = new HomeTeamPlayerAdapter(playersResponse.getPlayers());
            mRecyclerView.setAdapter(homeTeamPlayerAdapter);
        }
    }

    @Override
    public void onFail() {
        Toast.makeText(getContext(), R.string.wait_sec, Toast.LENGTH_LONG).show();
    }
}
