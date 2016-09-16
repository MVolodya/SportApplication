package info.androidhive.firebase.fragments.rateFragment;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.PictureDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.GenericRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.StreamEncoder;
import com.bumptech.glide.load.resource.file.FileToStreamDecoder;
import com.caverock.androidsvg.SVG;

import java.io.InputStream;

import info.androidhive.firebase.activity.mainActivity.MainActivity;
import info.androidhive.firebase.classes.managers.DataGetter;
import info.androidhive.firebase.classes.models.RatedUser;
import info.androidhive.firebase.classes.utils.ConvertDate;
import info.androidhive.firebase.classes.utils.CustomViewPager;
import info.androidhive.firebase.classes.models.DataHelper;
import info.androidhive.firebase.classes.managers.MaterialDialogManager;
import info.androidhive.firebase.classes.managers.ProgressDialogManager;
import info.androidhive.firebase.classes.managers.RateManager;
import info.androidhive.firebase.classes.recycleViewAdapters.RateViewPagerAdapter;
import info.androidhive.firebase.classes.retrofit.rateMatch.RateMatchResponse;
import info.androidhive.firebase.classes.utils.SvgDecoder;
import info.androidhive.firebase.classes.utils.SvgDrawableTranscoder;
import info.androidhive.firebase.classes.utils.SvgSoftwareLayerSetter;
import info.androidhive.firebase.R;
import info.androidhive.firebase.fragments.awayTeamFragment.AwayTeamFragment;
import info.androidhive.firebase.fragments.homeTeamFragment.HomeTeamFragment;
import info.androidhive.firebase.fragments.rateFragment.presenter.RatePresenter;
import info.androidhive.firebase.fragments.rateFragment.view.RateView;


public class RateFragment extends Fragment implements View.OnClickListener, RateView, SwipeRefreshLayout.OnRefreshListener {

    private View view;
    private TextView homeTeamTv;
    private TextView awayTeamTv;
    private TextView winsTv;
    private TextView drawTv;
    private TextView loseTv;
    private TextView roundTv;
    private TextView dateTv;
    private TextView timeTv;
    private TextView resultTv;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ImageView homeTeamIv;
    private ImageView awayTeamIv;
    private CustomViewPager customViewPagerRate;
    private TabLayout tabLayout;

    private RatedUser mUser;
    private DataHelper mDataHelper;

    private ProgressDialog progressDialog;
    private GenericRequestBuilder<Uri, InputStream, SVG, PictureDrawable> mRequestBuilder;
    private RateMatchResponse mRateMatchResponse;
    private MaterialDialogManager mMaterialDialogManager;
    private RatePresenter ratePresenter;

    public RateFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_rate, container, false);
        ratePresenter = new RatePresenter();
        ratePresenter.setRateView(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.status_bar));

        homeTeamTv = (TextView) view.findViewById(R.id.home_team_name_tv);
        awayTeamTv = (TextView) view.findViewById(R.id.away_team_name_tv);
        roundTv = (TextView) view.findViewById(R.id.round_tv);
        dateTv = (TextView) view.findViewById(R.id.match_date_tv);
        timeTv = (TextView) view.findViewById(R.id.time_tv);
        winsTv = (TextView) view.findViewById(R.id.coff_w1_tv);
        drawTv = (TextView) view.findViewById(R.id.coff_d_tv);
        loseTv = (TextView) view.findViewById(R.id.coff_w2_tv);
        resultTv = (TextView) view.findViewById(R.id.score_tv);
        swipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.rate_fragment_refresh);
        customViewPagerRate = (CustomViewPager) view.findViewById(R.id.rate_viewpager);
        tabLayout = (TabLayout) view.findViewById(R.id.match_rate_tabs);

        winsTv.setOnClickListener(this);
        drawTv.setOnClickListener(this);
        loseTv.setOnClickListener(this);

        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeColors(Color.parseColor("#1976d2"),Color.parseColor("#628f3e"));

        homeTeamIv = (ImageView) view.findViewById(R.id.home_team_iv);
        awayTeamIv = (ImageView) view.findViewById(R.id.away_team_iv);
        ImageView backArrow = (ImageView) view.findViewById(R.id.back_arrow_iv);
        progressDialog = new ProgressDialog(view.getContext());

        backArrow.setOnClickListener(this);

        ProgressDialogManager.showProgressDialog(progressDialog, getContext().getString(R.string.loading));

        mDataHelper = DataHelper.getInstance();
        mMaterialDialogManager = new MaterialDialogManager(getContext(), view);

        mRequestBuilder = Glide.with(view.getContext())
                .using(Glide.buildStreamModelLoader(Uri.class, getContext()), InputStream.class)
                .from(Uri.class)
                .as(SVG.class)
                .transcode(new SvgDrawableTranscoder(), PictureDrawable.class)
                .sourceEncoder(new StreamEncoder())
                .cacheDecoder(new FileToStreamDecoder<>(new SvgDecoder()))
                .decoder(new SvgDecoder())
                .animate(android.R.anim.fade_in)
                .listener(new SvgSoftwareLayerSetter<Uri>());

        ratePresenter.getUserData();
        ratePresenter.showRateMatch();

        return view;
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        if (!enter) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
            ((MainActivity) this.view.getContext()).showToolbar();
            ((MainActivity) this.view.getContext()).unlockSwipe();
        }
        return super.onCreateAnimation(transit, enter, nextAnim);
    }
    
    private void setView() {

        if(mRateMatchResponse != null ) {
            int homeTeamId = new DataGetter().getTeamId(
                    mRateMatchResponse.getFixture().getLinks().getHomeTeam().getHref());

            int awayTeamId = new DataGetter().getTeamId(
                    mRateMatchResponse.getFixture().getLinks().getAwayTeam().getHref());

            ratePresenter.getHomeTeamPhotoUrl(homeTeamId);
            ratePresenter.getAwayTeamPhotoUrl(awayTeamId);

            homeTeamTv.setText(mRateMatchResponse.getFixture().getHomeTeamName());
            awayTeamTv.setText(mRateMatchResponse.getFixture().getAwayTeamName());
            roundTv.setText(getContext().getString(R.string.round_of) + mRateMatchResponse.getFixture().getMatchday().toString());
            dateTv.setText(ConvertDate.getDate(mRateMatchResponse.getFixture().getDate()));
            timeTv.setText(ConvertDate.getTime(mRateMatchResponse.getFixture().getDate()));

            if (mRateMatchResponse.getFixture().getResult().getGoalsHomeTeam() != null
                    && mRateMatchResponse.getFixture().getResult().getGoalsAwayTeam() != null) {
                String r = String.valueOf(mRateMatchResponse.getFixture().getResult().getGoalsHomeTeam().toString()) + " - " +
                        mRateMatchResponse.getFixture().getResult().getGoalsAwayTeam().toString();
                resultTv.setText(r);
            }

            if (!mRateMatchResponse.getFixture().getStatus().equalsIgnoreCase("FINISHED")) {
                if (mRateMatchResponse.getFixture().getOdds() != null) {
                    winsTv.setText(mRateMatchResponse.getFixture().getOdds().getHomeWin().toString());
                    drawTv.setText(mRateMatchResponse.getFixture().getOdds().getDraw().toString());
                    loseTv.setText(mRateMatchResponse.getFixture().getOdds().getAwayWin().toString());
                } else {
                    winsTv.setText("2");
                    drawTv.setText("4");
                    loseTv.setText("2");
                }
            } else {
                winsTv.setVisibility(View.GONE);
                drawTv.setVisibility(View.GONE);
                loseTv.setVisibility(View.GONE);
            }
            setupViewPager(customViewPagerRate);
            tabLayout.setupWithViewPager(customViewPagerRate);
        }else{
            Toast.makeText(getContext(), getContext().getString(R.string.wait_sec), Toast.LENGTH_SHORT).show();
        }
    }

    private void setupViewPager(CustomViewPager viewPager) {
        RateViewPagerAdapter adapter = new RateViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new HomeTeamFragment(), mRateMatchResponse.getFixture().getHomeTeamName());
        adapter.addFragment(new AwayTeamFragment(), mRateMatchResponse.getFixture().getAwayTeamName());
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        ratePresenter.getUserData();
        switch (v.getId()) {
            case R.id.coff_w1_tv:
                showDialog(Double.parseDouble(winsTv.getText().toString()), RateManager.WIN_FIRST);
                break;
            case R.id.coff_d_tv:
                showDialog(Double.parseDouble(drawTv.getText().toString()), RateManager.DRAW);
                break;
            case R.id.coff_w2_tv:
                showDialog(Double.parseDouble(loseTv.getText().toString()), RateManager.WIN_SECOND);
                break;
            case R.id.back_arrow_iv:
                getFragmentManager().popBackStack();
                ((MainActivity) this.view.getContext()).showToolbar();
                ((MainActivity) this.view.getContext()).unlockSwipe();
                break;
        }
    }

    private void showDialog(double coff, String typeOfRate) {
        AlertDialog alertDialog = mMaterialDialogManager.openRateDialogBox
                (coff, mDataHelper.getMatchId(), typeOfRate, mUser);
        alertDialog.show();
    }


    @Override
    public void onSuccess(RateMatchResponse rateMatchResponse) {
        ProgressDialogManager.hideProgressDialog(progressDialog);
        this.mRateMatchResponse = rateMatchResponse;
        setView();
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onSuccessHomeImageUrl(String url) {
        if (url != null) {
            if (url.contains("svg")) {
                mRequestBuilder
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        // SVG cannot be serialized so it's not worth to cache it
                        .load(Uri.parse(url))
                        .override(70, 70)
                        .into(homeTeamIv);
            } else {
                Glide.with(view.getContext())
                        .load(url)
                        .override(70, 70)
                        .into(homeTeamIv);
            }
        } else {
            awayTeamIv.setImageDrawable(getResources().getDrawable(R.drawable.pockemon));
        }
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onFailHomeImageUrl() {
        swipeRefreshLayout.setRefreshing(false);
        Toast.makeText(getContext(), R.string.wait_sec, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSuccessAwayImageUrl(String url) {
        if (url != null) {
            if (url.contains("svg")) {
                mRequestBuilder
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        // SVG cannot be serialized so it's not worth to cache it
                        .load(Uri.parse(url))
                        .into(awayTeamIv);
            } else {
                Glide.with(view.getContext())
                        .load(url)
                        .into(awayTeamIv);
            }
        } else {
            awayTeamIv.setImageDrawable(getResources().getDrawable(R.drawable.pockemon));
        }
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onFailAwayImageUrl() {
        swipeRefreshLayout.setRefreshing(false);
        Toast.makeText(getContext(),R.string.wait_sec, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setUser(RatedUser user) {
        this.mUser = user;
    }


    @Override
    public void onFail() {
        ProgressDialogManager.hideProgressDialog(progressDialog);
        Toast.makeText(getContext(), R.string.wait_sec, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        ProgressDialogManager.showProgressDialog(progressDialog, getContext().getString(R.string.loading));
        ratePresenter.showRateMatch();
    }
}
