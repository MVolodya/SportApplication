package info.androidhive.firebase.classes.recycleViewAdapters;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import info.androidhive.firebase.classes.managers.RateManager;
import info.androidhive.firebase.classes.models.DataHelper;
import info.androidhive.firebase.classes.models.Rate;
import info.androidhive.firebase.R;


public class UsersRateAdapter extends RecyclerView.Adapter<UsersRateAdapter.ViewHolderUsersRates> {

    private List<Rate> ratesList;

    public UsersRateAdapter() {
        this.ratesList = new ArrayList<>();
    }

    public void setList(List<Rate> ratesList){
        this.ratesList = ratesList;
        notifyDataSetChanged();
    }

    public void clear(){
        ratesList.clear();
        notifyDataSetChanged();
    }

    public void addDeletedRate(Rate rates){
        ratesList.add(DataHelper.getInstance().getDeletedPosition(),rates);
        notifyDataSetChanged();
    }

    public void remove(int position) {
        DataHelper.getInstance().setRate(ratesList.get(position));
        ratesList.remove(position);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolderUsersRates onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_rate_list_row, parent, false);

        return new ViewHolderUsersRates(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolderUsersRates holder, int position) {
        int color = Color.parseColor("#2196f3");
        String status = (ratesList.get(position).getStatus());

        if(status.equalsIgnoreCase("win")) color = Color.parseColor("#26ae90");
        else if(status.equalsIgnoreCase("lose")) color = Color.parseColor("#ff6861");
        else if(status.equalsIgnoreCase("unchecked")) color = Color.parseColor("#888888");

        setViewColor(color, holder);

        holder.homeTeam.setText(ratesList.get(position).getHomeTeamName());
        holder.awayTeam.setText(ratesList.get(position).getAwayTeamName());
        holder.type.setText(ratesList.get(position).getType());
        holder.points.setText(ratesList.get(position).getPoints());
    }

    private void setViewColor(int color, ViewHolderUsersRates holder){
        holder.awayTeam.setTextColor(color);
        holder.homeTeam.setTextColor(color);
        holder.type.setTextColor(color);
        holder.points.setTextColor(color);
    }

    @Override
    public int getItemCount() {
        return ratesList.size();
    }


    public class ViewHolderUsersRates extends RecyclerView.ViewHolder {

        public final TextView homeTeam;
        public final TextView awayTeam;
        public final TextView type;
        public final TextView points;

        public ViewHolderUsersRates(View v) {
            super(v);
            homeTeam = (TextView)v.findViewById(R.id.rated_home_team);
            awayTeam = (TextView)v.findViewById(R.id.rated_away_team);
            type = (TextView)v.findViewById(R.id.ratedType);
            points = (TextView)v.findViewById(R.id.ratedPoints);
        }
    }
}
