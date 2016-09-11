package info.androidhive.firebase.fragments.leagueTableFragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Toast;

import java.util.List;

import info.androidhive.firebase.activity.mainActivity.MainActivity;
import info.androidhive.firebase.classes.models.DataHelper;
import info.androidhive.firebase.classes.managers.ProgressDialogManager;
import info.androidhive.firebase.classes.recycleViewAdapters.LeagueTableAdapter;
import info.androidhive.firebase.classes.retrofit.ApiFactory;
import info.androidhive.firebase.classes.retrofit.leagueTable.LeagueTableResponse;
import info.androidhive.firebase.classes.retrofit.leagueTable.LeagueTableService;
import info.androidhive.firebase.classes.retrofit.leagueTable.Standing;
import info.androidhive.firebase.R;
import info.androidhive.firebase.fragments.leagueTableFragment.presenter.LeagueTablePresenter;
import info.androidhive.firebase.fragments.leagueTableFragment.view.LeagueTableView;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;

public class LeagueTableFragment extends Fragment implements View.OnClickListener,
        LeagueTableView {

    private View view;
    private RecyclerView recyclerView;
    private ProgressDialog progressDialog;
    private RecyclerView.LayoutManager mLayoutManager;
    private LeagueTablePresenter leagueTablePresenter;

    public LeagueTableFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_league_table_test, container, false);
        leagueTablePresenter = new LeagueTablePresenter();
        leagueTablePresenter.setLeagueTableView(this);

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.anim_toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        if (((AppCompatActivity)getActivity()).getSupportActionBar() != null){
            ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        toolbar.setNavigationOnClickListener(this);

        recyclerView = (RecyclerView) view.findViewById(R.id.scrollableview);
        progressDialog = new ProgressDialog(view.getContext());
        mLayoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setNestedScrollingEnabled(false);

        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) view.findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(DataHelper.getInstance().getLeagueName());

        ProgressDialogManager.showProgressDialog(progressDialog,view.getContext().getString(R.string.loading));

        leagueTablePresenter.showLeagueTeam();

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        return view;
    }

    @Override
    public void onClick(View view) {
        getFragmentManager().popBackStack();
        ((MainActivity)this.view.getContext()).showToolbar();
        ((MainActivity)this.view.getContext()).unlockSwipe();
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        if(!enter){
            //leaving fragment
            getFragmentManager().popBackStack();
            ((MainActivity)this.view.getContext()).showToolbar();
            ((MainActivity)this.view.getContext()).unlockSwipe();
        }
        return super.onCreateAnimation(transit, enter, nextAnim);
    }


    @Override
    public void onSuccess(List<Standing> tables) {
        ProgressDialogManager.hideProgressDialog(progressDialog);
        LeagueTableAdapter mAdapter = new LeagueTableAdapter(tables);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onFail() {
        ProgressDialogManager.hideProgressDialog(progressDialog);
        Toast.makeText(getContext(), getString(R.string.wait_sec), Toast.LENGTH_SHORT).show();          
    }
}
