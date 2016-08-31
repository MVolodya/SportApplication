package info.androidhive.firebase.Fragments;


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

import java.util.List;

import info.androidhive.firebase.Activities.MainActivity;
import info.androidhive.firebase.Classes.Models.DataHelper;
import info.androidhive.firebase.Classes.Managers.ProgressDialogManager;
import info.androidhive.firebase.Classes.RecycleViewAdapters.LeagueTableAdapter;
import info.androidhive.firebase.Classes.Retrofit.ApiFactory;
import info.androidhive.firebase.Classes.Retrofit.LeagueTable.LeagueTableResponse;
import info.androidhive.firebase.Classes.Retrofit.LeagueTable.LeagueTableService;
import info.androidhive.firebase.Classes.Retrofit.LeagueTable.Standing;
import info.androidhive.firebase.R;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class LeagueTableFragment extends Fragment implements Callback<LeagueTableResponse>, View.OnClickListener{

    private View view;
    private LeagueTableResponse tableResponse;
    private RecyclerView recyclerView;
    private LeagueTableAdapter mAdapter;
    private ProgressDialog progressDialog;
    private RecyclerView.LayoutManager mLayoutManager;


    public LeagueTableFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_league_table_test, container, false);


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

        ProgressDialogManager.showProgressDialog(progressDialog,"Loading");

        DataHelper dataHelper = DataHelper.getInstance();
        int id = dataHelper.getId();

        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) view.findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(dataHelper.getLeagueName());

        LeagueTableService service = ApiFactory.getTableService();
        Call<LeagueTableResponse> call = service.tables(id);
        call.enqueue(this);

        //recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        return view;
    }

    @Override
    public void onResponse(Response<LeagueTableResponse> response) {

        List<Standing> tables;

        if (response.isSuccess()) {
            ProgressDialogManager.hideProgressDialog(progressDialog);
            tableResponse = response.body();
            tables = tableResponse.getStanding();
            mAdapter = new LeagueTableAdapter(tables);

            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setAdapter(mAdapter);

        }

    }

    @Override
    public void onFailure(Throwable t) {
        ProgressDialogManager.hideProgressDialog(progressDialog);
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



}
