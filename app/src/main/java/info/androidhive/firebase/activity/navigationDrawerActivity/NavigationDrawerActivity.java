package info.androidhive.firebase.activity.navigationDrawerActivity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

import info.androidhive.firebase.R;
import info.androidhive.firebase.activity.loginActivity.LoginActivity;
import info.androidhive.firebase.activity.mainActivity.MainActivity;
import info.androidhive.firebase.activity.navigationDrawerActivity.presenter.NDPresenter;
import info.androidhive.firebase.activity.navigationDrawerActivity.view.NDView;
import info.androidhive.firebase.classes.managers.LocalDatabaseManager;
import info.androidhive.firebase.classes.managers.SignInManager;
import info.androidhive.firebase.classes.models.User;
import info.androidhive.firebase.fragments.allUsersFragment.AllUsersFragment;
import info.androidhive.firebase.fragments.userRateFragment.UserRateFragment;
import info.androidhive.firebase.fragments.mainFragment.MainFragment;
import info.androidhive.firebase.fragments.settingsFragment.SettingsFragment;

public class NavigationDrawerActivity extends AppCompatActivity implements NDView {

    public Drawer resultDrawer;
    private NDPresenter ndPresenter;
    private AccountHeader mAccountHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);
        ndPresenter = new NDPresenter();
        ndPresenter.setView(this);
    }

    public void initializeNavigationDrawer(Toolbar toolbar, User user, final MainActivity mainActivity) {
        mAccountHeader = ndPresenter.createAccount(user);
        resultDrawer = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withAccountHeader(mAccountHeader)
                .withActionBarDrawerToggleAnimated(true)
                .addDrawerItems(
                        new SecondaryDrawerItem()
                                .withIdentifier(1)
                                .withName(R.string.nd_home)
                                .withIcon(GoogleMaterial.Icon.gmd_home),
                        new SectionDrawerItem()
                                .withName(R.string.nd_sub_rates),
                        new SecondaryDrawerItem()
                                .withIdentifier(3)
                                .withName(R.string.nd_all_users)
                                .withIcon(GoogleMaterial.Icon.gmd_account),
                        new SecondaryDrawerItem()
                                .withIdentifier(4)
                                .withName(R.string.nd_my_rates)
                                .withIcon(GoogleMaterial.Icon.gmd_plus),
                        new SectionDrawerItem()
                                .withIdentifier(5)
                                .withName(R.string.nd_sub_option),
                        new SecondaryDrawerItem()
                                .withIdentifier(7)
                                .withName(R.string.nd_settings)
                                .withIcon(FontAwesome.Icon.faw_cog),
                        new SecondaryDrawerItem()
                                .withIdentifier(8)
                                .withName(R.string.nd_exit)
                                .withIcon(GoogleMaterial.Icon.gmd_fullscreen_exit)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {

                        int itemSelected = (int) drawerItem.getIdentifier();

                        switch (itemSelected) {
                            case 1:
                                replace(new MainFragment());
                                mainActivity.showToolbar();
                                break;
                            case 3:
                                replace(new AllUsersFragment());
                                mainActivity.showToolbar();
                                break;
                            case 4:
                                replace(new UserRateFragment());
                                mainActivity.showToolbar();
                                break;
                            case 7:
                                replace(new SettingsFragment());
                                mainActivity.hideToolbar();
                                break;
                            case 8:
                                SignInManager.signOut();
                                LocalDatabaseManager.delete();
                                LoginActivity.start(NavigationDrawerActivity.this);
                                finish();
                                break;
                        }
                        return false;
                    }
                })
                .build();
    }

    private void replace(Fragment fragment) {
        Fragment fr = getSupportFragmentManager()
                .findFragmentById(R.id.nd_fragment_container);
        FragmentTransaction fragmentManager = getSupportFragmentManager().beginTransaction();
        fragmentManager
                .setCustomAnimations(R.anim.enter_anim, R.anim.exit_anim)
                .replace(R.id.nd_fragment_container, fragment, "main")
                .commit();
    }

    public void updateProfilePhoto(String url) {
        IProfile profile = mAccountHeader.getActiveProfile();
        profile.withIcon(url);
        mAccountHeader.updateProfile(profile);
    }

    public void updateProfileUsername(String username) {
        IProfile profile = mAccountHeader.getActiveProfile();
        profile.withName(username);
        mAccountHeader.updateProfile(profile);
    }

    public void updateProfileEmail(String email) {
        IProfile profile = mAccountHeader.getActiveProfile();
        profile.withEmail(email);
        mAccountHeader.updateProfile(profile);
    }

    @Override
    public Activity getActivity() {
        return this;
    }
}