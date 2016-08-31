package info.androidhive.firebase.Fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import info.androidhive.firebase.Activities.MainActivity;
import info.androidhive.firebase.Classes.Models.DataHelper;
import info.androidhive.firebase.Classes.Managers.ProgressDialogManager;
import info.androidhive.firebase.Classes.RecycleViewAdapters.ClickListener;
import info.androidhive.firebase.Classes.RecycleViewAdapters.DividerItemDecoration;
import info.androidhive.firebase.Classes.RecycleViewAdapters.LeagueAdapter;
import info.androidhive.firebase.Classes.RecycleViewAdapters.RecyclerTouchListener;
import info.androidhive.firebase.Classes.Retrofit.ApiFactory;
import info.androidhive.firebase.Classes.Retrofit.League.LeagueModel;
import info.androidhive.firebase.Classes.Retrofit.League.LeagueService;
import info.androidhive.firebase.R;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class LeagueFragment extends Fragment implements Callback<List<LeagueModel>> {

    private List<LeagueModel> leagueList = new ArrayList<>();
    private LeagueAdapter mAdapter;
    private View view;
    private ProgressDialog progressDialog;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView recyclerView;

    private int leagueId;

    public LeagueFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_league, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_league);
        progressDialog = new ProgressDialog(view.getContext());

        mLayoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        ProgressDialogManager.showProgressDialog(progressDialog,"Loading");

        LeagueService service = ApiFactory.getLeagueService();
        Call<List<LeagueModel>> call = service.leagues();
        call.enqueue(this);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new ClickListener() {

            @Override
            public void onClick(View view, int position) {

                DataHelper dataHelper = DataHelper.getInstance();
                leagueId = Integer.parseInt(leagueList.get(position).getId());
                dataHelper.setId(leagueId);
                dataHelper.setLeagueName(leagueList.get(position).getCaption());

                getActivity().getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.enter_anim, R.anim.exit_anim)
                        .add(R.id.container, new LeagueTableFragment())
                        .addToBackStack(null)
                        .commit();

                ((MainActivity)view.getContext()).hideToolbar();
                ((MainActivity)view.getContext()).lockSwipe();
            }

            @Override
            public void onLongClick(View view, int position) {}
        }));

        return view;
    }

    @Override
    public void onResponse(Response<List<LeagueModel>> response) {
        if (response.isSuccess()) {
            ProgressDialogManager.hideProgressDialog(progressDialog);
            leagueList = response.body();
            mAdapter = new LeagueAdapter(leagueList);
            recyclerView.setAdapter(mAdapter);
            //Log.d("Retrofit", football.get(0).getId()+ " " + football.get(0).getCaption());
        }
    }


    @Override
    public void onFailure(Throwable t) {
        ProgressDialogManager.hideProgressDialog(progressDialog);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }
}
