package info.androidhive.firebase.classes.recycleViewAdapters;

import android.graphics.drawable.PictureDrawable;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.GenericRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.StreamEncoder;
import com.bumptech.glide.load.resource.file.FileToStreamDecoder;
import com.caverock.androidsvg.SVG;

import java.io.InputStream;
import java.util.List;

import info.androidhive.firebase.classes.managers.DataGetter;
import info.androidhive.firebase.classes.retrofit.ApiFactory;
import info.androidhive.firebase.classes.retrofit.match.Fixture;
import info.androidhive.firebase.classes.retrofit.team.TeamResponse;
import info.androidhive.firebase.classes.retrofit.team.TeamService;
import info.androidhive.firebase.classes.utils.ConvertDate;
import info.androidhive.firebase.classes.utils.SvgDecoder;
import info.androidhive.firebase.classes.utils.SvgDrawableTranscoder;
import info.androidhive.firebase.classes.utils.SvgSoftwareLayerSetter;
import info.androidhive.firebase.R;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;

public class MatchAdapter extends RecyclerView.Adapter<MatchAdapter.MyViewHolder> {

    private final List<Fixture> mFixturesList;
    private View view;
    private GenericRequestBuilder<Uri, InputStream, SVG, PictureDrawable> mRequestBuilder;

    public MatchAdapter(List<Fixture> mFixturesList) {
        this.mFixturesList = mFixturesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.match_list_row, parent, false);

        mRequestBuilder = Glide.with(view.getContext())
                .using(Glide.buildStreamModelLoader(Uri.class, view.getContext()), InputStream.class)
                .from(Uri.class)
                .as(SVG.class)
                .transcode(new SvgDrawableTranscoder(), PictureDrawable.class)
                .sourceEncoder(new StreamEncoder())
                .cacheDecoder(new FileToStreamDecoder<>(new SvgDecoder()))
                .decoder(new SvgDecoder())
                .animate(android.R.anim.fade_in)
                .listener(new SvgSoftwareLayerSetter<Uri>());
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        if(mFixturesList.size()>0) {
            setHomeTeamImage(holder, position);
            setAwayTeamImage(holder, position);
            holder.homeTeamTv.setText(mFixturesList.get(position).getHomeTeamName());
            holder.awayTeamTv.setText(mFixturesList.get(position).getAwayTeamName());
            holder.timeTv.setText(ConvertDate.getTime(mFixturesList.get(position).getDate()));
            holder.dateTv.setText(ConvertDate.getDate(mFixturesList.get(position).getDate()));
            if (mFixturesList.get(position).getResult().getGoalsHomeTeam() != null
                    && mFixturesList.get(position).getResult().getGoalsAwayTeam() != null) {
                holder.scoreTv.setText(mFixturesList.get(position).getResult().getGoalsHomeTeam().toString()
                        + " - " + mFixturesList.get(position).getResult().getGoalsAwayTeam().toString());
            }
            holder.statusTv.setText(mFixturesList.get(position).getStatus());
        }
    }

    @Override
    public int getItemCount() {
        return mFixturesList.size();
    }

    private void setHomeTeamImage(final MyViewHolder holder, int position) {
        int awayTeamId = new DataGetter().getTeamId(mFixturesList.get(position)
                .getLinks().getHomeTeam().getHref());
        TeamService serviceTeam = ApiFactory.getTeamService();
        Call<TeamResponse> callTeam = serviceTeam.teams(awayTeamId);
        callTeam.enqueue(new Callback<TeamResponse>() {
            @Override
            public void onResponse(Response<TeamResponse> response) {
                if(response.errorBody() == null){
                    TeamResponse teamResponse = response.body();
                    String linkHomeTeamImage = teamResponse.getCrestUrl();
                    if (linkHomeTeamImage != null ) {
                        if (linkHomeTeamImage.contains("svg")) {
                            mRequestBuilder
                                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                    // SVG cannot be serialized so it's not worth to cache it
                                    .override(50, 50)
                                    .load(Uri.parse(teamResponse.getCrestUrl()))
                                    .into(holder.homeTeamIv);
                        } else {
                            Glide.with(view.getContext())
                                    .load(linkHomeTeamImage)
                                    .override(50, 50)
                                    .into(holder.homeTeamIv);
                        }
                    } else {
                        holder.homeTeamIv.setImageDrawable(view.getResources()
                                .getDrawable(R.drawable.soccer_football_icon));
                    }
                } else  holder.homeTeamIv.setImageDrawable(view.getResources()
                        .getDrawable(R.drawable.soccer_football_icon));
            }

            @Override
            public void onFailure(Throwable t) {}
        });
    }

    private void setAwayTeamImage(final MyViewHolder holder, int position) {
        int awayTeamId = new DataGetter().getTeamId(mFixturesList.get(position)
                .getLinks().getAwayTeam().getHref());
        TeamService serviceTeam = ApiFactory.getTeamService();
        Call<TeamResponse> callTeam = serviceTeam.teams(awayTeamId);
        callTeam.enqueue(new Callback<TeamResponse>() {
            @Override
            public void onResponse(Response<TeamResponse> response) {
                if(response.errorBody() == null){
                    TeamResponse teamResponse = response.body();
                    String linkAwayTeamImage = teamResponse.getCrestUrl();
                    if (linkAwayTeamImage != null ) {
                        if (linkAwayTeamImage.contains("svg")) {
                            mRequestBuilder
                                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                    // SVG cannot be serialized so it's not worth to cache it
                                    .load(Uri.parse(teamResponse.getCrestUrl()))
                                    .into(holder.awayTeamIv);
                        } else {
                            Glide.with(view.getContext())
                                    .load(linkAwayTeamImage)
                                    .into(holder.awayTeamIv);
                        }
                    } else {
                        holder.awayTeamIv.setImageDrawable(view.getResources()
                                .getDrawable(R.drawable.soccer_football_icon));
                    }
                } else  holder.awayTeamIv.setImageDrawable(view.getResources()
                        .getDrawable(R.drawable.soccer_football_icon));
            }
            @Override
            public void onFailure(Throwable t) {}
        });
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public final TextView homeTeamTv;
        public final TextView awayTeamTv;
        public final TextView timeTv;
        public final TextView statusTv;
        public final TextView scoreTv;
        public final TextView dateTv;
        public final ImageView homeTeamIv;
        public final ImageView awayTeamIv;


        public MyViewHolder(View v) {
            super(view);
            homeTeamTv = (TextView) v.findViewById(R.id.home_team_name_tv);
            awayTeamTv = (TextView) v.findViewById(R.id.away_team_name_tv);
            timeTv = (TextView) v.findViewById(R.id.match_time_tv);
            dateTv = (TextView) v.findViewById(R.id.match_date_tv);
            statusTv = (TextView) v.findViewById(R.id.match_status_tv);
            scoreTv = (TextView) v.findViewById(R.id.score_tv);
            awayTeamIv = (ImageView) v.findViewById(R.id.second_team_logo_iv);
            homeTeamIv = (ImageView) v.findViewById(R.id.first_team_logo_iv);
        }
    }

}