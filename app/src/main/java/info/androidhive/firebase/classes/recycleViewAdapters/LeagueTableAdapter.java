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

    private final List<Standing> mStandingsList;
    private View itemView;

    public LeagueTableAdapter(List<Standing> mStandingsList) {
        this.mStandingsList = mStandingsList;
    }

    @Override
    public LeagueTableViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        itemView = LayoutInflater.from(parent.getContext())
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

        if(!mStandingsList.get(position).getCrestURI().equalsIgnoreCase("null")) {
            if (mStandingsList.get(position).getCrestURI().contains("svg")) {
                requestBuilder
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        // SVG cannot be serialized so it's not worth to cache it
                        .override(70, 70)
                        .load(Uri.parse(mStandingsList.get(position).getCrestURI()))
                        .into(holder.logoIv);
            } else {
                Glide.with(holder.view.getContext())
                        .load(mStandingsList.get(position).getCrestURI())
                        .override(70, 70)
                        .into(holder.logoIv);
            }
        }   else holder.logoIv.setImageDrawable(itemView.getResources().getDrawable(
                R.drawable.soccer_football_icon, null
        ));

        holder.teamNameTv.setText(mStandingsList.get(position).getTeam());
        holder.positionTv.setText(Integer.toString(++count));
        holder.playedGamesWinsTv.setText(mStandingsList.get(position).getWins().toString());
        holder.playedGamesDrawsTv.setText(mStandingsList.get(position).getDraws().toString());
        holder.playedGamesLoseTv.setText(mStandingsList.get(position).getLosses().toString());
        holder.goalsWinTv.setText(mStandingsList.get(position).getGoals().toString());
        holder.goalsLoseTv.setText(mStandingsList.get(position).getGoalsAgainst().toString());
        holder.pointsTv.setText(mStandingsList.get(position).getPoints().toString());
    }

    @Override
    public int getItemCount() {

        return mStandingsList.size();
    }

    public class LeagueTableViewHolder extends RecyclerView.ViewHolder {

        public final TextView positionTv;
        public final TextView teamNameTv;
        public final TextView playedGamesWinsTv;
        public final TextView playedGamesDrawsTv;
        public final TextView playedGamesLoseTv;
        public final TextView goalsWinTv;
        public final TextView goalsLoseTv;
        public final TextView pointsTv;
        public final ImageView logoIv;
        public final View view;

        public LeagueTableViewHolder(View view) {
            super(view);
            this.view = view;
            teamNameTv = (TextView)view.findViewById(R.id.team_name_tv);
            logoIv = (ImageView)view.findViewById(R.id.league_team_logo_iv);
            positionTv = (TextView)view.findViewById(R.id.team_position_tv);
            playedGamesWinsTv = (TextView)view.findViewById(R.id.games_win_row_tv);
            playedGamesDrawsTv = (TextView)view.findViewById(R.id.games_draw_row_tv);
            playedGamesLoseTv = (TextView)view.findViewById(R.id.games_lose_row_tv);
            goalsWinTv = (TextView)view.findViewById(R.id.goals_win_row_tv);
            goalsLoseTv = (TextView)view.findViewById(R.id.goals_lose_row_tv);
            pointsTv = (TextView)view.findViewById(R.id.team_points_tv);
        }
    }
}
