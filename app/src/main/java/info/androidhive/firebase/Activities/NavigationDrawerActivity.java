package info.androidhive.firebase.Activities;

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
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import info.androidhive.firebase.Classes.Managers.LocalDatabaseManager;
import info.androidhive.firebase.Classes.Managers.SignInManager;
import info.androidhive.firebase.Classes.Models.User;
import info.androidhive.firebase.Fragments.AllUsersFragment;
import info.androidhive.firebase.Fragments.CurrentUserRateFragment;
import info.androidhive.firebase.Fragments.MainFragment;
import info.androidhive.firebase.R;

public class NavigationDrawerActivity extends AppCompatActivity {

    Drawer result;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);
}

    void initializeNavigationDrawer(Toolbar toolbar, User user) throws IOException, ExecutionException, InterruptedException {
        AccountHeader headerResult = createAccount(user);
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

                        Fragment fr =getSupportFragmentManager()
                                .findFragmentById(R.id.container);

                        FragmentTransaction fragmentManager = getSupportFragmentManager().beginTransaction();

                        if (drawerItem != null) {
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
                                    if (!(fr instanceof CurrentUserRateFragment))
                                    fragmentManager
                                            .setCustomAnimations(R.anim.enter_anim, R.anim.exit_anim)
                                            .replace(R.id.container, CurrentUserRateFragment.newInstance())
                                            .commit();
                                    break;
                                case 7:
                                    startActivity(new Intent(NavigationDrawerActivity.this, SettingsActivity.class));
                                    break;
                                case 8:
                                    SignInManager.signOut();
                                    LocalDatabaseManager.delete();
                                    startActivity(new Intent(NavigationDrawerActivity.this, LoginActivity.class));
                                    finish();
                                    break;
                            }
                        }
                        return false;
                    }
                })
                .build();

    }

    private AccountHeader createAccount(User user) {

        IProfile profile = new ProfileDrawerItem();

        if (user.getName() != null) profile.withName(user.getName());
        else profile.withName("Anonymous");

        if (user.getEmail()!=null) profile.withEmail(user.getEmail());
        else profile.withEmail("Anonymous@Anonymous.com");

        if (user.getPhotoURL()!= null) profile.withIcon(user.getPhotoURL());
        else profile.withIcon(R.drawable.prof);


        return new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.w)
                .addProfiles(profile)
                .build();
    }
}