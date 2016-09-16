package info.androidhive.firebase.fragments.mainFragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import info.androidhive.firebase.classes.utils.CustomViewPager;
import info.androidhive.firebase.fragments.matchFragment.MatchFragment;
import info.androidhive.firebase.fragments.leagueFragment.LeagueFragment;
import info.androidhive.firebase.R;

public class MainFragment extends Fragment {

    private CustomViewPager viewPager;
    private TabLayout tabLayout;
    private View view;

    public MainFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_main, container, false);

        viewPager = (CustomViewPager) view.findViewById(R.id.viewpager);
        tabLayout = (TabLayout) view.findViewById(R.id.main_tab);

        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        return view;
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new LeagueFragment(), view.getContext().getString(R.string.tab_league));
        adapter.addFragment(new MatchFragment(), view.getContext().getString(R.string.tab_match));
        viewPager.setAdapter(adapter);
    }

    public void hideTabs() {
        tabLayout.setVisibility(View.GONE);
    }

    public void showTabs() {
        tabLayout.setVisibility(View.VISIBLE);
    }

    public CustomViewPager getViewPager() {
        return viewPager;
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
