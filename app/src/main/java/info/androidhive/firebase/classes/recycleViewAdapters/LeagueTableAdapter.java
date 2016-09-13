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

import info.androidhive.firebase.classes.retrofit.leagueTable.Standing;
import info.androidhive.firebase.classes.utils.SvgDecoder;
import info.androidhive.firebase.classes.utils.SvgSoftwareLayerSetter;
import info.androidhive.firebase.R;
import info.androidhive.firebase.classes.utils.SvgDrawableTranscoder;


public class LeagueTableAdapter extends RecyclerView.Adapter<LeagueTableAdapter.LeagueTableViewHolder> {

    private final List<Standing> standingsList;

    public LeagueTableAdapter(List<Standing> standingsList) {
        this.standingsList = standingsList;
    }

    @Override
    public LeagueTableViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.league_table_row, parent, false);

        return new LeagueTableViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(LeagueTableViewHolder holder, int position) {

        int count = position;

        GenericRequestBuilder<Uri, InputStream, SVG, PictureDrawable> requestBuilder = Glide.with(holder.view.getContext())
                .using(Glide.buildStreamModelLoader(Uri.class, holder.view.getContext()), InputStream.class)
                .from(Uri.class)
                .as(SVG.class)
                .transcode(new SvgDrawableTranscoder(), PictureDrawable.class)
                .sourceEncoder(new StreamEncoder())
                .cacheDecoder(new FileToStreamDecoder<>(new SvgDecoder()))
                .decoder(new SvgDecoder())
                .animate(android.R.anim.fade_in)
                .listener(new SvgSoftwareLayerSetter<Uri>());

        if(standingsList.get(position).getCrestURI() != null) {
            if (standingsList.get(position).getCrestURI().contains("svg")) {
                requestBuilder
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        // SVG cannot be serialized so it's not worth to cache it
                        .override(50, 50)
                        .load(Uri.parse(standingsList.get(position).getCrestURI()))
                        .into(holder.logo);
            } else {
                Glide.with(holder.view.getContext())
                        .load(standingsList.get(position).getCrestURI())
                        .override(50, 50)
                        .into(holder.logo);
            }
        }else holder.logo.setImageResource(R.drawable.soccer_football_icon);

        holder.tvTeamName.setText(standingsList.get(position).getTeam());
        holder.tvPosition.setText(Integer.toString(++count));
        holder.tvPlayedGamesWins.setText(standingsList.get(position).getWins().toString());
        holder.tvPlayedGamesDraws.setText(standingsList.get(position).getDraws().toString());
        holder.tvPlayedGamesLose.setText(standingsList.get(position).getLosses().toString());
        holder.tvGoalsWin.setText(standingsList.get(position).getGoals().toString());
        holder.tvGoalsLose.setText(standingsList.get(position).getGoalsAgainst().toString());
        holder.tvPoints.setText(standingsList.get(position).getPoints().toString());

    }

    @Override
    public int getItemCount() {

        return standingsList.size();
    }


    public class LeagueTableViewHolder extends RecyclerView.ViewHolder {

        public final TextView tvPosition;
        public final TextView tvTeamName;
        public final TextView tvPlayedGamesWins;
        public final TextView tvPlayedGamesDraws;
        public final TextView tvPlayedGamesLose;
        public final TextView tvGoalsWin;
        public final TextView tvGoalsLose;
        public final TextView tvPoints;
        public final ImageView logo;
        public final View view;


        public LeagueTableViewHolder(View view) {
            super(view);
            this.view = view;

            tvTeamName = (TextView)view.findViewById(R.id.textViewTeamRow);
            logo = (ImageView)view.findViewById(R.id.imageView);
            tvPosition = (TextView)view.findViewById(R.id.textViewPositionRow);
            tvPlayedGamesWins = (TextView)view.findViewById(R.id.textViewGamesWinRow);
            tvPlayedGamesDraws = (TextView)view.findViewById(R.id.textViewGamesDrawsRow);
            tvPlayedGamesLose = (TextView)view.findViewById(R.id.textViewGamesLoseRow);
            tvGoalsWin = (TextView)view.findViewById(R.id.textViewGoalWinRow);
            tvGoalsLose = (TextView)view.findViewById(R.id.textViewGoalLoseRow);
            tvPoints = (TextView)view.findViewById(R.id.textViewPointsRow);
        }
    }
}
