package info.androidhive.firebase.Classes;

import android.content.Context;
import android.content.Intent;
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

import info.androidhive.firebase.Activities.LoginActivity;
import info.androidhive.firebase.Activities.MainActivity;
import info.androidhive.firebase.Activities.SettingsActivity;
import info.androidhive.firebase.R;

/**
 * Created by andri on 22.07.2016.
 */
public class NavigationDrawerManager {

    private Context context;
    private User user;

    public NavigationDrawerManager(Context context, User user){
        this.context = context;
        this.user = user;
    }



    public Drawer initializeNavigationDrawer(Toolbar toolbar) throws IOException, ExecutionException, InterruptedException {

        AccountHeader headerResult = createAccount();


       Drawer result = new DrawerBuilder()
                .withActivity((MainActivity)context)
                .withToolbar(toolbar)
                .withAccountHeader(headerResult)
                .withActionBarDrawerToggleAnimated(true)
                .addDrawerItems(
                        new PrimaryDrawerItem()
                                .withIdentifier(1)
                                .withName("My office")
                                .withIcon(GoogleMaterial.Icon.gmd_balance),
                        new PrimaryDrawerItem()
                                .withIdentifier(2)
                                .withName("News")
                                .withIcon(GoogleMaterial.Icon.gmd_balance),
                        new PrimaryDrawerItem()
                                .withIdentifier(3)
                                .withName("Feedback")
                                .withIcon(GoogleMaterial.Icon.gmd_balance),
                        new SectionDrawerItem()
                                .withIdentifier(4)
                                .withName("Options"),
                        new SecondaryDrawerItem()
                                .withIdentifier(5)
                                .withName("FAQ")
                                .withIcon(FontAwesome.Icon.faw_cog),
                        new SecondaryDrawerItem()
                                .withIdentifier(6)
                                .withName("Settings")
                                .withIcon(FontAwesome.Icon.faw_cog),
                        new SecondaryDrawerItem()
                                .withIdentifier(7)
                                .withName("Exit")
                                .withIcon(FontAwesome.Icon.faw_cog)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {

                        //((MainActivity)context).showToolbar();

                        int itemSelected = (int) drawerItem.getIdentifier();

                        if (drawerItem != null) {
                            switch (itemSelected) {
                                case 6: {
                                    //((MainActivity)context).hideToolbar();

                                        context.startActivity(new Intent(context, SettingsActivity.class));
                                    break;
                                }
                                case 7:
                                    SignInManager.signOut();
                                    LocalDatabaseManager.delete();
                                    context.startActivity(new Intent(context, LoginActivity.class));
                                    ((MainActivity) context).finish();
                                    break;
                            }
                        }

                        return false;
                    }
                })
                .build();

        return  result;
    }


    private AccountHeader createAccount() throws IOException, ExecutionException, InterruptedException {

        IProfile profile = new ProfileDrawerItem();

        if (user.getName() != null) profile.withName(user.getName());
        else profile.withName("Anonymous");

        if (user.getEmail()!=null) profile.withEmail(user.getEmail());
        else profile.withEmail("Anonymous@Anonymous.com");

        if (user.getPhotoURL()!=null) profile.withIcon(user.getPhotoURL());
        else profile.withIcon(R.drawable.prof);

        return new AccountHeaderBuilder()
                .withActivity((MainActivity)context)
                .withHeaderBackground(R.drawable.back)
                .addProfiles(profile)
                .build();
    }
}
