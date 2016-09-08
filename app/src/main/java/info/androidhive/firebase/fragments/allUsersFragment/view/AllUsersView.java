package info.androidhive.firebase.fragments.allUsersFragment.view;

import java.util.List;

import info.androidhive.firebase.classes.models.RatedUser;

public interface AllUsersView {
    void onSuccess(List<RatedUser> ratedUserList);
    void onFail();
}
