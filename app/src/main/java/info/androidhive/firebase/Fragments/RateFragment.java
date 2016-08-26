package info.androidhive.firebase.Fragments;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.graphics.drawable.PictureDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.GenericRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.StreamEncoder;
import com.bumptech.glide.load.resource.file.FileToStreamDecoder;
import com.caverock.androidsvg.SVG;

import java.io.InputStream;

import info.androidhive.firebase.Activities.MainActivity;
import info.androidhive.firebase.Classes.CustomViewPager;
import info.androidhive.firebase.Classes.DataHelper;
import info.androidhive.firebase.Classes.MaterialDialogManager;
import info.androidhive.firebase.Classes.ProgressDialogManager;
import info.androidhive.firebase.Classes.RateManager;
import info.androidhive.firebase.Classes.RateViewPagerAdapter;
import info.androidhive.firebase.Classes.Retrofit.ApiFactory;
import info.androidhive.firebase.Classes.Retrofit.RateMatch.RateMatchResponse;
import info.androidhive.firebase.Classes.Retrofit.RateMatch.RateMatchService;
import info.androidhive.firebase.Classes.Retrofit.Team.TeamResponse;
import info.androidhive.firebase.Classes.Retrofit.Team.TeamService;
import info.androidhive.firebase.Classes.SvgDecoder;
import info.androidhive.firebase.Classes.SvgDrawableTranscoder;
import info.androidhive.firebase.Classes.SvgSoftwareLayerSetter;
import info.androidhive.firebase.R;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;


public class RateFragment extends Fragment implements Callback<RateMatchResponse>,
        View.OnClickListener {

    private View view;
    private TextView homeTeam;
    private TextView awayTeam;
    private TextView wins;
    private TextView draw;
    private TextView lose;
    private TextView round;
    private TextView status;
    private ImageView imageHomeTeam;
    private ImageView imageAwayTeam;
    private ImageView backArrow;
    private CustomViewPager customViewPagerRate;
    private TabLayout tabLayout;

    private DataHelper dataHelper;

    private ProgressDialog progressDialog;
    private ProgressDialogManager dialogManager;
    private GenericRequestBuilder<Uri, InputStream, SVG, PictureDrawable> requestBuilder;
    private RateMatchResponse rateMatchResponse;
    private MaterialDialogManager materialDialogManager;


    public RateFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_rate, container, false);



        homeTeam = (TextView) view.findViewById(R.id.textViewTeamHome);
        awayTeam = (TextView) view.findViewById(R.id.textViewTeamAway);
        round = (TextView) view.findViewById(R.id.tv_round);
        status = (TextView) view.findViewById(R.id.textViewStatus);
        wins = (TextView) view.findViewById(R.id.textViewWin);
        draw = (TextView) view.findViewById(R.id.textViewDraw);
        lose = (TextView) view.findViewById(R.id.textViewLose);
        customViewPagerRate = (CustomViewPager)view.findViewById(R.id.viewpager_rate);
        tabLayout = (TabLayout)view.findViewById(R.id.tabs_match_rate);

        wins.setOnClickListener(this);
        draw.setOnClickListener(this);
        lose.setOnClickListener(this);

        imageHomeTeam = (ImageView) view.findViewById(R.id.imageHomeTeam);
        imageAwayTeam = (ImageView) view.findViewById(R.id.imageAwayTeam);
        backArrow = (ImageView)view.findViewById(R.id.imageViewBackArrow);

        backArrow.setOnClickListener(this);

        dialogManager = new ProgressDialogManager(getActivity(), progressDialog);
        dialogManager.showProgressDialog();

        dataHelper = DataHelper.getInstance();
        materialDialogManager = new MaterialDialogManager(view.getContext(), view);

        requestBuilder = Glide.with(view.getContext())
                .using(Glide.buildStreamModelLoader(Uri.class, view.getContext()), InputStream.class)
                .from(Uri.class)
                .as(SVG.class)
                .transcode(new SvgDrawableTranscoder(), PictureDrawable.class)
                .sourceEncoder(new StreamEncoder())
                .cacheDecoder(new FileToStreamDecoder<>(new SvgDecoder()))
                .decoder(new SvgDecoder())
                .animate(android.R.anim.fade_in)
                .listener(new SvgSoftwareLayerSetter<Uri>());

        RateMatchService service = ApiFactory.getRateMatchService();
        Call<RateMatchResponse> call = service.match(dataHelper.getMatchId());
        call.enqueue(this);



        return view;
    }

    @Override
    public void onResponse(Response<RateMatchResponse> response) {
        if (response.isSuccess()) {
            dialogManager.hideProgressDialog();
            rateMatchResponse = response.body();
            setView();
        }
    }

    @Override
    public void onFailure(Throwable t) {
        Log.d("Retrofit", "" + t);
        dialogManager.hideProgressDialog();
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        if (!enter) {
            //leaving fragment
            getFragmentManager().popBackStack();
            ((MainActivity) this.view.getContext()).showToolbar();
            ((MainActivity) this.view.getContext()).unlockSwipe();
        }
        return super.onCreateAnimation(transit, enter, nextAnim);
    }

    private void setupViewPager(CustomViewPager viewPager) {
        RateViewPagerAdapter adapter = new RateViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new HomeTeamFragment(), rateMatchResponse.getFixture().getHomeTeamName());
        adapter.addFragment(new AwayTeamFragment(), rateMatchResponse.getFixture().getAwayTeamName());
        viewPager.setAdapter(adapter);
    }


    private void setView() {

        setHomeTeamImage();
        setAwayTeamImage();

        setupViewPager(customViewPagerRate);
        tabLayout.setupWithViewPager(customViewPagerRate);

        homeTeam.setText(rateMatchResponse.getFixture().getHomeTeamName());
        awayTeam.setText(rateMatchResponse.getFixture().getAwayTeamName());
        round.setText("Round of " + rateMatchResponse.getFixture().getMatchday().toString());
        status.setText(rateMatchResponse.getFixture().getStatus());
        if (rateMatchResponse.getFixture().getOdds() != null) {
            wins.setText(rateMatchResponse.getFixture().getOdds().getHomeWin().toString());
            draw.setText(rateMatchResponse.getFixture().getOdds().getDraw().toString());
            lose.setText(rateMatchResponse.getFixture().getOdds().getAwayWin().toString());
        } else {
            wins.setText("2");
            draw.setText("4");
            lose.setText("2");
        }


    }

    private int getTeamId(String link) {
        Log.d("teamId", link);
        return Integer.parseInt(link.replaceAll("http://api.football-data.org/v1/teams/", ""));
    }

    private void setHomeTeamImage() {

        int homeTeamId = getTeamId(
                rateMatchResponse.getFixture().getLinks().getHomeTeam().getHref()
        );

        TeamService serviceTeam = ApiFactory.getTeamService();
        Call<TeamResponse> callTeam = serviceTeam.teams(homeTeamId);
        callTeam.enqueue(new Callback<TeamResponse>() {
            @Override
            public void onResponse(Response<TeamResponse> response) {

                TeamResponse teamResponse = response.body();
                String linkHomeTeamImage = teamResponse.getCrestUrl();

                if (linkHomeTeamImage != null) {
                    if (linkHomeTeamImage.contains("svg")) {
                        requestBuilder
                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                // SVG cannot be serialized so it's not worth to cache it
                                .load(Uri.parse(teamResponse.getCrestUrl()))
                                .into(imageHomeTeam);
                    } else {
                        Glide.with(view.getContext())
                                .load(linkHomeTeamImage)
                                .into(imageHomeTeam);
                    }
                } else {
                    imageHomeTeam.setImageDrawable(getResources().getDrawable(R.drawable.pockemon));
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    private void setAwayTeamImage() {

        int awayTeamId = getTeamId(
                rateMatchResponse.getFixture().getLinks().getAwayTeam().getHref()
        );

        TeamService serviceTeam = ApiFactory.getTeamService();
        Call<TeamResponse> callTeam = serviceTeam.teams(awayTeamId);
        callTeam.enqueue(new Callback<TeamResponse>() {
            @Override
            public void onResponse(Response<TeamResponse> response) {

                TeamResponse teamResponse = response.body();
                String linkAwayTeamImage = teamResponse.getCrestUrl();

                if (linkAwayTeamImage != null) {
                    if (linkAwayTeamImage.contains("svg")) {
                        requestBuilder
                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                // SVG cannot be serialized so it's not worth to cache it
                                .load(Uri.parse(teamResponse.getCrestUrl()))
                                .into(imageAwayTeam);
                    } else {
                        Glide.with(view.getContext())
                                .load(linkAwayTeamImage)
                                .into(imageAwayTeam);
                    }
                } else {
                    imageAwayTeam.setImageDrawable(getResources().getDrawable(R.drawable.pockemon));
                }
            }

            @Override
            public void onFailure(Throwable t) {}
        });
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
}
