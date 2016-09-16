package info.androidhive.firebase.fragments.allUsersFragment.presenter;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import info.androidhive.firebase.classes.models.RatedUser;
import info.androidhive.firebase.fragments.allUsersFragment.view.AllUsersView;

public class AllUsersPresenter {

    private AllUsersView mAllUsersView;

    public void setAllUsersView(AllUsersView allUsersView) {
        this.mAllUsersView = allUsersView;
    }

    public void showUsers(){
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        final List<RatedUser> ratedUserList = new ArrayList<>();
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    ratedUserList.add(d.getValue(RatedUser.class));
                }
                mAllUsersView.onSuccess(ratedUserList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }
}
