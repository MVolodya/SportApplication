package info.androidhive.firebase.Fragments;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import info.androidhive.firebase.Classes.Utils.ConnectivityReceiver;
import info.androidhive.firebase.Classes.Utils.CustomViewPager;
import info.androidhive.firebase.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment implements View.OnClickListener {

    private View view;
    private CustomViewPager viewPager;
    private TabLayout tabLayout;
    private ImageView imageViewInfo;
    private TextView textViewInfo;
    private Button buttonTryAgain;


    public MainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_main, container, false);

//        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
//        String date = df.format(Calendar.getInstance().getTime());
//        Log.d("data", date);

        viewPager = (CustomViewPager) view.findViewById(R.id.viewpager);
        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        imageViewInfo = (ImageView) view.findViewById(R.id.imageViewBadConnection);
        textViewInfo = (TextView) view.findViewById(R.id.textViewInfo);
        buttonTryAgain = (Button) view.findViewById(R.id.buttonTryAgain);

        viewPager.setVisibility(View.GONE);
        tabLayout.setVisibility(View.GONE);
        imageViewInfo.setVisibility(View.GONE);
        textViewInfo.setVisibility(View.GONE);
        buttonTryAgain.setVisibility(View.GONE);

        buttonTryAgain.setOnClickListener(this);


        if (ConnectivityReceiver.isOnline(view.getContext())) {

            imageViewInfo.setVisibility(View.GONE);
            textViewInfo.setVisibility(View.GONE);
            buttonTryAgain.setVisibility(View.GONE);

            viewPager.setVisibility(View.VISIBLE);
            tabLayout.setVisibility(View.VISIBLE);

            setupViewPager(viewPager);
            tabLayout.setupWithViewPager(viewPager);
        } else {
            imageViewInfo.setVisibility(View.VISIBLE);
            textViewInfo.setVisibility(View.VISIBLE);
            buttonTryAgain.setVisibility(View.VISIBLE);
        }
        // Inflate the layout for this fragment
        return view;
    }


    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new LeagueFragment(), "League");
        adapter.addFragment(new MatchFragment(), "Match");
        viewPager.setAdapter(adapter);
    }

    public void hideTabs() {
        tabLayout.setVisibility(View.GONE);
    }

    public void showTabs() {
        tabLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View view) {
        if (ConnectivityReceiver.isOnline(view.getContext())) {

            imageViewInfo.setVisibility(View.GONE);
            textViewInfo.setVisibility(View.GONE);
            buttonTryAgain.setVisibility(View.GONE);

            viewPager.setVisibility(View.VISIBLE);
            tabLayout.setVisibility(View.VISIBLE);

            setupViewPager(viewPager);
            tabLayout.setupWithViewPager(viewPager);
        } else {
            imageViewInfo.setVisibility(View.VISIBLE);
            textViewInfo.setVisibility(View.VISIBLE);
            buttonTryAgain.setVisibility(View.VISIBLE);
        }
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
