package info.androidhive.firebase.fragments;


import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.tuesda.walker.circlerefresh.CircleRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import info.androidhive.firebase.classes.models.RatedUser;
import info.androidhive.firebase.classes.recycleViewAdapters.AllUsersAdapter;
import info.androidhive.firebase.classes.recycleViewAdapters.DividerItemDecoration;
import info.androidhive.firebase.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AllUsersFragment extends Fragment {

    private AllUsersAdapter usersAdapter;
    private RecyclerView recyclerView;
    private CircleRefreshLayout refreshLayout;
    private CircularProgressView progressView;

    public AllUsersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_all_users, container, false);

        progressView = (CircularProgressView) view.findViewById(R.id.progress_view);
        refreshLayout = (CircleRefreshLayout) view.findViewById(R.id.refresh_layout_users);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_users);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(view.getContext());

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        refreshLayout.setOnRefreshListener(
                new CircleRefreshLayout.OnCircleRefreshListener() {
                    @Override
                    public void refreshing() {
                        getUsers();
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {
                                refreshLayout.finishRefreshing();
                            }
                        }, 2 * 1000);
                    }

                    @Override
                    public void completeRefresh() {
                    }
                });

        getUsers();

        return view;
    }

    private void getUsers() {
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
                usersAdapter = new AllUsersAdapter(ratedUserList);
                recyclerView.setAdapter(usersAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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
