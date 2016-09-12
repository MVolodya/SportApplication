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
    private TextView homeTeam;
    private TextView awayTeam;
    private TextView wins;
    private TextView draw;
    private TextView lose;
    private TextView round;
    private TextView date;
    private TextView time;
    private TextView result;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ImageView imageHomeTeam;
    private ImageView imageAwayTeam;
    private CustomViewPager customViewPagerRate;
    private TabLayout tabLayout;

    private DataHelper dataHelper;

    private ProgressDialog progressDialog;
    private GenericRequestBuilder<Uri, InputStream, SVG, PictureDrawable> requestBuilder;
    private RateMatchResponse rateMatchResponse;
    private MaterialDialogManager materialDialogManager;
    private RatePresenter ratePresenter;


    public RateFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_rate, container, false);
        ratePresenter = new RatePresenter();
        ratePresenter.setRateView(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.status_bar));

        homeTeam = (TextView) view.findViewById(R.id.textViewTeamHome);
        awayTeam = (TextView) view.findViewById(R.id.textViewTeamAway);
        round = (TextView) view.findViewById(R.id.tv_round);
        date = (TextView) view.findViewById(R.id.textViewDate);
        time = (TextView) view.findViewById(R.id.textViewTime);
        wins = (TextView) view.findViewById(R.id.textViewWin);
        draw = (TextView) view.findViewById(R.id.textViewDraw);
        lose = (TextView) view.findViewById(R.id.textViewLose);
        result = (TextView) view.findViewById(R.id.textView2);
        swipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.refreshRateFragment);
        customViewPagerRate = (CustomViewPager) view.findViewById(R.id.viewpager_rate);
        tabLayout = (TabLayout) view.findViewById(R.id.tabs_match_rate);

        wins.setOnClickListener(this);
        draw.setOnClickListener(this);
        lose.setOnClickListener(this);

        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeColors(Color.parseColor("#1976d2"),Color.parseColor("#628f3e"));

        imageHomeTeam = (ImageView) view.findViewById(R.id.imageHomeTeam);
        imageAwayTeam = (ImageView) view.findViewById(R.id.imageAwayTeam);
        ImageView backArrow = (ImageView) view.findViewById(R.id.imageViewBackArrow);
        progressDialog = new ProgressDialog(view.getContext());

        backArrow.setOnClickListener(this);

        ProgressDialogManager.showProgressDialog(progressDialog, getContext().getString(R.string.loading));

        dataHelper = DataHelper.getInstance();
        materialDialogManager = new MaterialDialogManager(getContext(), view);

        requestBuilder = Glide.with(view.getContext())
                .using(Glide.buildStreamModelLoader(Uri.class, getContext()), InputStream.class)
                .from(Uri.class)
                .as(SVG.class)
                .transcode(new SvgDrawableTranscoder(), PictureDrawable.class)
                .sourceEncoder(new StreamEncoder())
                .cacheDecoder(new FileToStreamDecoder<>(new SvgDecoder()))
                .decoder(new SvgDecoder())
                .animate(android.R.anim.fade_in)
                .listener(new SvgSoftwareLayerSetter<Uri>());

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

        if(rateMatchResponse!= null ) {
            int homeTeamId = new DataGetter().getTeamId(
                    rateMatchResponse.getFixture().getLinks().getHomeTeam().getHref());

            int awayTeamId = new DataGetter().getTeamId(
                    rateMatchResponse.getFixture().getLinks().getAwayTeam().getHref());

            ratePresenter.getHomeTeamPhotoUrl(homeTeamId);
            ratePresenter.getAwayTeamPhotoUrl(awayTeamId);

            homeTeam.setText(rateMatchResponse.getFixture().getHomeTeamName());
            awayTeam.setText(rateMatchResponse.getFixture().getAwayTeamName());
            round.setText(getContext().getString(R.string.round_of) + rateMatchResponse.getFixture().getMatchday().toString());
            date.setText(ConvertDate.getDate(rateMatchResponse.getFixture().getDate()));
            time.setText(ConvertDate.getTime(rateMatchResponse.getFixture().getDate()));

            if (rateMatchResponse.getFixture().getResult().getGoalsHomeTeam() != null
                    && rateMatchResponse.getFixture().getResult().getGoalsAwayTeam() != null) {
                String r = String.valueOf(rateMatchResponse.getFixture().getResult().getGoalsHomeTeam().toString()) + " - " +
                        rateMatchResponse.getFixture().getResult().getGoalsAwayTeam().toString();
                result.setText(r);
            }

            if (!rateMatchResponse.getFixture().getStatus().equalsIgnoreCase("FINISHED")) {
                if (rateMatchResponse.getFixture().getOdds() != null) {
                    wins.setText(rateMatchResponse.getFixture().getOdds().getHomeWin().toString());
                    draw.setText(rateMatchResponse.getFixture().getOdds().getDraw().toString());
                    lose.setText(rateMatchResponse.getFixture().getOdds().getAwayWin().toString());
                } else {
                    wins.setText("2");
                    draw.setText("4");
                    lose.setText("2");
                }
            } else {
                wins.setVisibility(View.GONE);
                draw.setVisibility(View.GONE);
                lose.setVisibility(View.GONE);
            }
            setupViewPager(customViewPagerRate);
            tabLayout.setupWithViewPager(customViewPagerRate);
        }else{
            Toast.makeText(getContext(), getContext().getString(R.string.wait_sec), Toast.LENGTH_SHORT).show();
        }
    }

    private void setupViewPager(CustomViewPager viewPager) {
        RateViewPagerAdapter adapter = new RateViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new HomeTeamFragment(), rateMatchResponse.getFixture().getHomeTeamName());
        adapter.addFragment(new AwayTeamFragment(), rateMatchResponse.getFixture().getAwayTeamName());
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.textViewWin:
                showDialog(Double.parseDouble(wins.getText().toString()), RateManager.WIN_FIRST);
                break;
            case R.id.textViewDraw:
                showDialog(Double.parseDouble(draw.getText().toString()), RateManager.DRAW);
                break;
            case R.id.textViewLose:
                showDialog(Double.parseDouble(lose.getText().toString()), RateManager.WIN_SECOND);
                break;
            case R.id.imageViewBackArrow:
                getFragmentManager().popBackStack();
                ((MainActivity) this.view.getContext()).showToolbar();
                ((MainActivity) this.view.getContext()).unlockSwipe();
                break;
        }
    }

    private void showDialog(double coff, String typeOfRate) {
        AlertDialog alertDialog = materialDialogManager.openDialogBox
                (coff, dataHelper.getMatchId(), typeOfRate);
        alertDialog.show();
    }


    @Override
    public void onSuccess(RateMatchResponse rateMatchResponse) {
        ProgressDialogManager.hideProgressDialog(progressDialog);
        this.rateMatchResponse = rateMatchResponse;
        setView();
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onSuccessHomeImageUrl(String url) {
        if (url != null) {
            if (url.contains("svg")) {
                requestBuilder
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        // SVG cannot be serialized so it's not worth to cache it
                        .load(Uri.parse(url))
                        .override(50, 50)
                        .into(imageHomeTeam);
            } else {
                Glide.with(view.getContext())
                        .load(url)
                        .override(50, 50)
                        .into(imageHomeTeam);
            }
        } else {
            imageAwayTeam.setImageDrawable(getResources().getDrawable(R.drawable.pockemon));
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
                requestBuilder
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        // SVG cannot be serialized so it's not worth to cache it
                        .load(Uri.parse(url))
                        .into(imageAwayTeam);
            } else {
                Glide.with(view.getContext())
                        .load(url)
                        .into(imageAwayTeam);
            }
        } else {
            imageAwayTeam.setImageDrawable(getResources().getDrawable(R.drawable.pockemon));
        }
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onFailAwayImageUrl() {
        swipeRefreshLayout.setRefreshing(false);
        Toast.makeText(getContext(),R.string.wait_sec, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFail() {
        ProgressDialogManager.hideProgressDialog(progressDialog);
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        ProgressDialogManager.showProgressDialog(progressDialog, getContext().getString(R.string.loading));
        ratePresenter.showRateMatch();
    }
}
