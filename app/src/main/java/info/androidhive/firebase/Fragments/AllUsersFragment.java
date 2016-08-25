package info.androidhive.firebase.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;

import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import info.androidhive.firebase.Activities.MainActivity;
import info.androidhive.firebase.Classes.RatedUser;
import info.androidhive.firebase.Classes.RecycleViewClasses.AllUsersAdapter;
import info.androidhive.firebase.Classes.RecycleViewClasses.DividerItemDecoration;
import info.androidhive.firebase.Classes.RecycleViewClasses.LeagueAdapter;
import info.androidhive.firebase.Classes.User;
import info.androidhive.firebase.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AllUsersFragment extends Fragment {

    private View view;
    private AllUsersAdapter usersAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private CircularProgressView progressView;

    public AllUsersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_all_users, container, false);

        progressView = (CircularProgressView) view.findViewById(R.id.progress_view);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_users);
        mLayoutManager = new LinearLayoutManager(view.getContext());

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        getUsers();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getUsers();
            }
        });

        return view;
    }

    private List<RatedUser> getUsers() {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        final List<RatedUser> ratedUserList = new ArrayList<>();
        progressView.startAnimation();
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    ratedUserList.add(d.getValue(RatedUser.class));
                }
                progressView.stopAnimation();
                progressView.setVisibility(View.GONE);
                swipeRefreshLayout.setRefreshing(false);
                usersAdapter = new AllUsersAdapter(ratedUserList);
                recyclerView.setAdapter(usersAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        return ratedUserList;
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        if (!enter) {
            //leaving fragment
            getFragmentManager().popBackStack();
        }
        return super.onCreateAnimation(transit, enter, nextAnim);
    }



}
