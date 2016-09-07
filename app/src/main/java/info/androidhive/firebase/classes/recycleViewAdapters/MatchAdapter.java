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
import info.androidhive.firebase.classes.utils.SvgDecoder;
import info.androidhive.firebase.classes.utils.SvgDrawableTranscoder;
import info.androidhive.firebase.classes.utils.SvgSoftwareLayerSetter;
import info.androidhive.firebase.R;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;

public class MatchAdapter extends RecyclerView.Adapter<MatchAdapter.MyViewHolder> {

    private final List<Fixture> fixturesList;
    private View view;
    private GenericRequestBuilder<Uri, InputStream, SVG, PictureDrawable> requestBuilder;

    public MatchAdapter(List<Fixture> fixturesList) {
        this.fixturesList = fixturesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.match_list_row, parent, false);

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

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        if(fixturesList.size()>0) {
            setHomeTeamImage(holder, position);
            setAwayTeamImage(holder, position);
            holder.tvHomeTeam.setText(fixturesList.get(position).getHomeTeamName());
            holder.tvAwayTeam.setText(fixturesList.get(position).getAwayTeamName());
            holder.tvTime.setText(fixturesList.get(position).getDate());

            if (fixturesList.get(position).getResult().getGoalsHomeTeam() != null
                    && fixturesList.get(position).getResult().getGoalsAwayTeam() != null) {
                holder.tvScore.setText(fixturesList.get(position).getResult().getGoalsHomeTeam().toString()
                        + " - " + fixturesList.get(position).getResult().getGoalsAwayTeam().toString());
            }

            holder.tvStatus.setText(fixturesList.get(position).getStatus());
        }
    }

    @Override
    public int getItemCount() {
        return fixturesList.size();
    }

    public static String getMatchId(String link) {
        return link.substring(41);
    }

    private void setHomeTeamImage(final MyViewHolder holder, int position) {

        int awayTeamId = new DataGetter().getTeamId(fixturesList.get(position)
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
                            requestBuilder
                                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                    // SVG cannot be serialized so it's not worth to cache it
                                    .load(Uri.parse(teamResponse.getCrestUrl()))
                                    .into(holder.circleImageHomeTeam);
                        } else {
                            Glide.with(view.getContext())
                                    .load(linkHomeTeamImage)
                                    .into(holder.circleImageHomeTeam);
                        }
                    } else {
                        holder.circleImageHomeTeam.setImageDrawable(view.getResources()
                                .getDrawable(R.drawable.soccer_football_icon));
                    }
                } else  holder.circleImageHomeTeam.setImageDrawable(view.getResources()
                        .getDrawable(R.drawable.soccer_football_icon));
            }

            @Override
            public void onFailure(Throwable t) {
            }
        });
    }

    private void setAwayTeamImage(final MyViewHolder holder, int position) {

        int awayTeamId = new DataGetter().getTeamId(fixturesList.get(position)
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
                            requestBuilder
                                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                    // SVG cannot be serialized so it's not worth to cache it
                                    .load(Uri.parse(teamResponse.getCrestUrl()))
                                    .into(holder.circleImageAwayTeam);
                        } else {
                            Glide.with(view.getContext())
                                    .load(linkAwayTeamImage)
                                    .into(holder.circleImageAwayTeam);
                        }
                    } else {
                        holder.circleImageAwayTeam.setImageDrawable(view.getResources()
                                .getDrawable(R.drawable.soccer_football_icon));
                    }
                } else  holder.circleImageAwayTeam.setImageDrawable(view.getResources()
                        .getDrawable(R.drawable.soccer_football_icon));
            }

            @Override
            public void onFailure(Throwable t) {
            }
        });
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public final TextView tvHomeTeam;
        public final TextView tvAwayTeam;
        public final TextView tvTime;
        public final TextView tvStatus;
        public final TextView tvScore;
        public final ImageView circleImageHomeTeam;
        public final ImageView circleImageAwayTeam;


        public MyViewHolder(View v) {
            super(view);
            tvHomeTeam = (TextView) v.findViewById(R.id.homeTeam);
            tvAwayTeam = (TextView) v.findViewById(R.id.awayTeam);
            tvTime = (TextView) v.findViewById(R.id.goalsAwayTeam);
            tvStatus = (TextView) v.findViewById(R.id.textViewStatus);
            tvScore = (TextView) v.findViewById(R.id.textViewScore);
            circleImageAwayTeam = (ImageView) v.findViewById(R.id.secondteam);
            circleImageHomeTeam = (ImageView) v.findViewById(R.id.firstteam);
        }
    }

}