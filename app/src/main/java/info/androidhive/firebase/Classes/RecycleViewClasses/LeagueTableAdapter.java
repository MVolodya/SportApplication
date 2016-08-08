package info.androidhive.firebase.Classes.RecycleViewClasses;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import info.androidhive.firebase.Classes.Retrofit.LeagueTable.Standing;
import info.androidhive.firebase.R;

/**
 * Created by andri on 05.08.2016.
 */
public class LeagueTableAdapter extends RecyclerView.Adapter<LeagueTableAdapter.LeagueTableViewHolder> {

    private List<Standing> standingsList;


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

        Glide.with(holder.view.getContext())
                .load(standingsList.get(position).getCrestURI())
                .into(holder.logo);

        holder.tvTeamName.setText(standingsList.get(position).getTeam());
        holder.tvPosition.setText(Integer.toString(position++));
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

    public void swap(List list) {
        if (standingsList != null) {
            standingsList.clear();
            standingsList.addAll(list);
        } else {
            standingsList = list;
        }
        notifyDataSetChanged();
    }

    public class LeagueTableViewHolder extends RecyclerView.ViewHolder {

        public TextView tvPosition;
        public TextView tvTeamName;
        public TextView tvPlayedGamesWins;
        public TextView tvPlayedGamesDraws;
        public TextView tvPlayedGamesLose;
        public TextView tvGoalsWin;
        public TextView tvGoalsLose;
        public TextView tvPoints;
        public ImageView logo;
        public View view;


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
