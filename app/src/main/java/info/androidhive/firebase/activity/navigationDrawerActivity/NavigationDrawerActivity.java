package info.androidhive.firebase.activity.navigationDrawerActivity;

import android.app.Activity;
import android.content.Intent;
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
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import info.androidhive.firebase.activity.loginActivity.LoginActivity;
import info.androidhive.firebase.activity.mainActivity.MainActivity;
import info.androidhive.firebase.activity.navigationDrawerActivity.presenter.NDPresenter;
import info.androidhive.firebase.activity.navigationDrawerActivity.view.NDView;
import info.androidhive.firebase.classes.managers.LocalDatabaseManager;
import info.androidhive.firebase.classes.managers.SignInManager;
import info.androidhive.firebase.classes.models.DataHelper;
import info.androidhive.firebase.classes.models.User;
import info.androidhive.firebase.fragments.allUsersFragment.AllUsersFragment;
import info.androidhive.firebase.fragments.currentUserRateFragment.CurrentUserRateFragment;
import info.androidhive.firebase.fragments.mainFragment.MainFragment;
import info.androidhive.firebase.fragments.SettingsFragment;
import info.androidhive.firebase.R;

public class NavigationDrawerActivity extends AppCompatActivity implements NDView {

   public Drawer result;
   private NDPresenter ndPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);
        ndPresenter = new NDPresenter();
        ndPresenter.setView(this);
    }

   public void initializeNavigationDrawer(Toolbar toolbar, User user, final MainActivity mainActivity) {
        AccountHeader headerResult = ndPresenter.createAccount(user);
        result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withAccountHeader(headerResult)
                .withActionBarDrawerToggleAnimated(true)
                .addDrawerItems(
                        new PrimaryDrawerItem()
                                .withIdentifier(1)
                                .withName("Home")
                                .withIcon(GoogleMaterial.Icon.gmd_home),
                        new SectionDrawerItem()
                                .withName("Rates"),
                        new SecondaryDrawerItem()
                                .withIdentifier(3)
                                .withName("All users")
                                .withIcon(GoogleMaterial.Icon.gmd_account),
                        new SecondaryDrawerItem()
                                .withIdentifier(4)
                                .withName("My rate")
                                .withIcon(GoogleMaterial.Icon.gmd_plus),
                        new SectionDrawerItem()
                                .withIdentifier(5)
                                .withName("Options"),
                        new SecondaryDrawerItem()
                                .withIdentifier(7)
                                .withName("Settings")
                                .withIcon(FontAwesome.Icon.faw_cog),
                        new SecondaryDrawerItem()
                                .withIdentifier(8)
                                .withName("Exit")
                                .withIcon(GoogleMaterial.Icon.gmd_fullscreen_exit)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {

                        int itemSelected = (int) drawerItem.getIdentifier();
                        Fragment fr = getSupportFragmentManager()
                                .findFragmentById(R.id.container);
                        FragmentTransaction fragmentManager = getSupportFragmentManager().beginTransaction();
                        mainActivity.showToolbar();

                        switch (itemSelected) {
                            case 1:
                                if (!(fr instanceof MainFragment))
                                    fragmentManager
                                            .setCustomAnimations(R.anim.enter_anim, R.anim.exit_anim)
                                            .replace(R.id.container, new MainFragment(), "main")
                                            .commit();
                                break;
                            case 3:
                                if (!(fr instanceof AllUsersFragment))
                                    fragmentManager
                                            .setCustomAnimations(R.anim.enter_anim, R.anim.exit_anim)
                                            .replace(R.id.container, new AllUsersFragment())
                                            .commit();
                                break;
                            case 4:
                                if (!(fr instanceof CurrentUserRateFragment)) {
                                    DataHelper.getInstance().setCount(0);
                                    fragmentManager
                                            .setCustomAnimations(R.anim.enter_anim, R.anim.exit_anim)
                                            .replace(R.id.container, new CurrentUserRateFragment())
                                            .commit();
                                }
                                break;
                            case 7:
                                if (!(fr instanceof SettingsFragment)) {
                                    fragmentManager
                                            .setCustomAnimations(R.anim.enter_anim, R.anim.exit_anim)
                                            .replace(R.id.container, new SettingsFragment())
                                            .commit();
                                    mainActivity.hideToolbar();
                                }
                                break;
                            case 8:
                                SignInManager.signOut();
                                LocalDatabaseManager.delete();
                                startActivity(new Intent(NavigationDrawerActivity.this, LoginActivity.class));
                                finish();
                                break;
                        }
                        return false;
                    }
                })
                .build();
    }

    @Override
    public Activity getActivity() {
        return this;
    }
}