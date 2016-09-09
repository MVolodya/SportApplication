package info.androidhive.firebase.activity.navigationDrawerActivity.presenter;

import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

import info.androidhive.firebase.activity.navigationDrawerActivity.view.NDView;
import info.androidhive.firebase.classes.models.User;
import info.androidhive.firebase.R;

public class NDPresenter {

    private NDView ndView;

    public void setView(NDView view){
        ndView = view;
    }

    public AccountHeader createAccount(User user) {

        IProfile profile = new ProfileDrawerItem();

        if (user.getName() != null) profile.withName(user.getName());
        else profile.withName("Anonymous");

        if (user.getEmail() != null) profile.withEmail(user.getEmail());
        else profile.withEmail("Anonymous@Anonymous.com");

        if (user.getPhotoURL() != null) profile.withIcon(user.getPhotoURL());
        else profile.withIcon(R.drawable.prof);

        AccountHeader accountHeader =
                new AccountHeaderBuilder()
                        .withActivity(ndView.getActivity())
                        .withHeaderBackground(R.drawable.w)
                        .addProfiles(profile)
                        .build();

        return accountHeader;
    }
}
