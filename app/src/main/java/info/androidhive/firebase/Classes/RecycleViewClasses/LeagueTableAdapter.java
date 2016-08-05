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

        holder.tvTeamName.setText(standingsList.get(position).getTeam());
        holder.tvPoints.setText(standingsList.get(position).getPoints().toString());
        holder.tvGoals.setText(standingsList.get(position).getGoals().toString());
        holder.tvGoalsAgainst.setText(standingsList.get(position).getGoalsAgainst().toString());
        holder.tvGoalDifference.setText(standingsList.get(position).getGoalDifference().toString());

        Glide.with(holder.view.getContext())
                .load(standingsList.get(position).getCrestURI())
                .into(holder.logo);
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

        public TextView tvRank;
        public TextView tvTeamName;
        public TextView tvPlayedGames;
        public TextView tvPoints;
        public TextView tvGoals;
        public TextView tvGoalsAgainst;
        public TextView tvGoalDifference;
        public ImageView logo;
        public View view;


        public LeagueTableViewHolder(View view) {
            super(view);
            this.view = view;
            //tvRank = (TextView)view.findViewById(R.id.textViewRankRow);
            tvTeamName = (TextView)view.findViewById(R.id.textViewTeamRow);
            //tvPlayedGames = (TextView)view.findViewById(R.id.textViewPlayedGamesRow);
            tvPoints = (TextView)view.findViewById(R.id.textViewPointsRow);
            tvGoals = (TextView)view.findViewById(R.id.textViewGoalsRow);
            tvGoalsAgainst = (TextView)view.findViewById(R.id.textViewGoalAgainstRow);
            tvGoalDifference = (TextView)view.findViewById(R.id.textViewGoalDifferenceRow);
            logo = (ImageView)view.findViewById(R.id.imageView);
        }
    }
}
