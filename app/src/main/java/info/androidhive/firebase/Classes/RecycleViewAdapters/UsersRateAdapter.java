package info.androidhive.firebase.Classes.RecycleViewAdapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import info.androidhive.firebase.Classes.Models.DataHelper;
import info.androidhive.firebase.Classes.Models.Rate;
import info.androidhive.firebase.R;


public class UsersRateAdapter extends RecyclerView.Adapter<UsersRateAdapter.ViewHolderUsersRates> {

    private final List<Rate> ratesList;

    public UsersRateAdapter() {
        this.ratesList = new ArrayList<>();
    }

    public void addRates(Rate rates) {
        ratesList.add(rates);
        notifyDataSetChanged();
        //notifyItemInserted(ratesList.size());
    }

    public void addDeletedRate(Rate rates){
        ratesList.add(DataHelper.getInstance().getDeletedPosition(),rates);
        notifyDataSetChanged();
       // notifyItemInserted(ratesList.size());
    }

    public void remove(int position) {
        DataHelper.getInstance().setRate(ratesList.get(position));
        ratesList.remove(position);
        notifyDataSetChanged();
//        notifyItemRemoved(position);
//        notifyItemRangeChanged(position, ratesList.size());
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
        holder.checkStatus.setText(ratesList.get(position).getStatus());
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
        public final TextView checkStatus;

        public ViewHolderUsersRates(View v) {
            super(v);
            homeTeam = (TextView)v.findViewById(R.id.rated_home_team);
            awayTeam = (TextView)v.findViewById(R.id.rated_away_team);
            type = (TextView)v.findViewById(R.id.ratedType);
            points = (TextView)v.findViewById(R.id.ratedPoints);
            checkStatus = (TextView)v.findViewById(R.id.textViewStatusRate);
        }
    }
}
