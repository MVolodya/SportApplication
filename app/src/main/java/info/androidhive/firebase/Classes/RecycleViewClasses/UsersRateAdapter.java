package info.androidhive.firebase.Classes.RecycleViewClasses;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import info.androidhive.firebase.Classes.Models.Rate;
import info.androidhive.firebase.R;

/**
 * Created by andrii on 23.08.16.
 */
public class UsersRateAdapter extends RecyclerView.Adapter<UsersRateAdapter.ViewHolderUsersRates> {

    private List<Rate> ratesList;

    public UsersRateAdapter(List<Rate> ratesList) {
        this.ratesList = ratesList;
    }

    @Override
    public ViewHolderUsersRates onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_rate_list_row, parent, false);

        return new ViewHolderUsersRates(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolderUsersRates holder, int position) {
        holder.homeTeam.setText(ratesList.get(position).getHomeTeamName());
        holder.awayTeam.setText(ratesList.get(position).getAwayTeamName());
        holder.type.setText(ratesList.get(position).getType());
        holder.points.setText(ratesList.get(position).getPoints());
    }


    @Override
    public int getItemCount() {
        return ratesList.size();
    }

    public class ViewHolderUsersRates extends RecyclerView.ViewHolder {

        public TextView homeTeam;
        public TextView awayTeam;
        public TextView type;
        public TextView points;

        public ViewHolderUsersRates(View v) {
            super(v);
            homeTeam = (TextView)v.findViewById(R.id.rated_home_team);
            awayTeam = (TextView)v.findViewById(R.id.rated_away_team);
            type = (TextView)v.findViewById(R.id.ratedType);
            points = (TextView)v.findViewById(R.id.ratedPoints);
        }
    }
}
