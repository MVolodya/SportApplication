package info.androidhive.firebase.Fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import info.androidhive.firebase.Classes.ProgressDialogManager;
import info.androidhive.firebase.Classes.RecycleViewClasses.MatchAdapter;
import info.androidhive.firebase.Classes.Retrofit.ApiFactory;
import info.androidhive.firebase.Classes.Retrofit.Match.MatchModel;
import info.androidhive.firebase.Classes.Retrofit.Match.MatchService;
import info.androidhive.firebase.R;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class MatchFragment extends Fragment implements Callback<List<MatchModel>> {

    private List<MatchModel> matchList = new ArrayList<>();
    private RecyclerView recyclerView;
    private MatchAdapter mAdapter;
    private View view;
    private ProgressDialog progressDialog;
    private ProgressDialogManager dialogManager;


    public MatchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_match, container, false);

        dialogManager = new ProgressDialogManager(getActivity(),progressDialog);
        dialogManager.showProgressDialog();

        MatchService service = ApiFactory.getMatchService();
        Call<List<MatchModel>> call = service.matches();
        call.enqueue(this);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_match);

        return view;
    }

    @Override
    public void onResponse(Response<List<MatchModel>> response) {
        if (response.isSuccess()) {
            dialogManager.hideProgressDialog();
            matchList = response.body();
            mAdapter = new MatchAdapter(matchList);

            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(view.getContext());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setAdapter(mAdapter);
        }
    }

    @Override
    public void onFailure(Throwable t) {
        Log.d("Retrofit",""+t);
        dialogManager.hideProgressDialog();}
}
