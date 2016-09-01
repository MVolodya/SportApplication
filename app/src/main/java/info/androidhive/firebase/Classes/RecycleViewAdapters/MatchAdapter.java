package info.androidhive.firebase.Classes.RecycleViewAdapters;

import android.graphics.drawable.PictureDrawable;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.GenericRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.StreamEncoder;
import com.bumptech.glide.load.resource.file.FileToStreamDecoder;
import com.caverock.androidsvg.SVG;

import java.io.InputStream;
import java.util.List;

import info.androidhive.firebase.Classes.Retrofit.Match.Fixture;
import info.androidhive.firebase.Classes.Utils.SvgDecoder;
import info.androidhive.firebase.Classes.Utils.SvgDrawableTranscoder;
import info.androidhive.firebase.Classes.Utils.SvgSoftwareLayerSetter;
import info.androidhive.firebase.R;

public class MatchAdapter extends RecyclerView.Adapter<MatchAdapter.MyViewHolder> {

    private List<Fixture> fixturesList;
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
            holder.tvHomeTeam.setText(fixturesList.get(position).getHomeTeamName());
            holder.tvAwayTeam.setText(fixturesList.get(position).getAwayTeamName());
            holder.tvTime.setText(fixturesList.get(position).getDate());

            if (fixturesList.get(position).getResult().getGoalsHomeTeam() != null
                    && fixturesList.get(position).getResult().getGoalsAwayTeam() != null) {
                holder.tvScore.setText(fixturesList.get(position).getResult().getGoalsHomeTeam().toString()
                        + " - " + fixturesList.get(position).getResult().getGoalsAwayTeam().toString());
            }

            holder.tvStatus.setText(fixturesList.get(position).getStatus());
        }else{

        }
    }

    @Override
    public int getItemCount() {

        return fixturesList.size();
    }

    public static String getMatchId(String link) {
        return link.substring(41);
    }

    private int getTeamId(String link) {
        Log.d("teamId", link);
        return Integer.parseInt(link.replaceAll("http://api.football-data.org/v1/teams/", ""));
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView tvHomeTeam;
        public TextView tvAwayTeam;
        public TextView tvTime;
        public TextView tvStatus;
        public TextView tvScore;
        public ImageView circleImageHomeTeam;
        public ImageView circleImageAwayTeam;


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