package info.androidhive.firebase.Fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import info.androidhive.firebase.Classes.IdHelper;
import info.androidhive.firebase.Classes.ProgressDialogManager;
import info.androidhive.firebase.Classes.RecycleViewClasses.LeagueTableAdapter;
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
public class LeagueTableFragment extends Fragment implements Callback<LeagueTableResponse> {

    private View view;
    private LeagueTableResponse tableResponse;
    private RecyclerView recyclerView;
    private LeagueTableAdapter mAdapter;
    private ProgressDialog progressDialog;
    private ProgressDialogManager dialogManager;
    private RecyclerView.LayoutManager mLayoutManager;


    public LeagueTableFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_league_table, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_league_table);
        mLayoutManager = new LinearLayoutManager(view.getContext());

        dialogManager = new ProgressDialogManager(getActivity(), progressDialog);
        dialogManager.showProgressDialog();

        IdHelper idHelper = IdHelper.getInstance();
        int id = idHelper.getId();

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
            dialogManager.hideProgressDialog();
            tableResponse = response.body();
            tables = tableResponse.getStanding();
            mAdapter = new LeagueTableAdapter(tables);

            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setAdapter(mAdapter);

        }

    }

    @Override
    public void onFailure(Throwable t) {dialogManager.hideProgressDialog();}
}
