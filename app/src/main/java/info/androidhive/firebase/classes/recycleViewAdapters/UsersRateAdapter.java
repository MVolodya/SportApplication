package info.androidhive.firebase.classes.recycleViewAdapters;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import info.androidhive.firebase.classes.models.Rate;
import info.androidhive.firebase.R;

public class UsersRateAdapter extends RecyclerView.Adapter<UsersRateAdapter.ViewHolderUsersRates> {

    private List<Rate> mRatesList;

    public UsersRateAdapter() {}

    public void setList(List<Rate> ratesList){
        this.mRatesList = ratesList;
        notifyDataSetChanged();
    }

    public void clear(){
        mRatesList.clear();
        notifyDataSetChanged();
    }

    public void addDeletedRate(Rate rates){
        mRatesList.add(rates);
        notifyDataSetChanged();
    }

    public void remove(int position) {
        mRatesList.remove(position);
        notifyItemRemoved(position);
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
        String status = (mRatesList.get(position).getStatus());
        Rate rate = mRatesList.get(position);

        if(status.equalsIgnoreCase("win")) color = Color.parseColor("#26ae90");
        else if(status.equalsIgnoreCase("lose")) color = Color.parseColor("#ff6861");
        else if(status.equalsIgnoreCase("unchecked")) color = Color.parseColor("#888888");

        setViewColor(color, holder);

        holder.homeTeamTv.setText(mRatesList.get(position).getHomeTeamName());
        holder.awayTeamTv.setText(mRatesList.get(position).getAwayTeamName());
        holder.typeTv.setText(mRatesList.get(position).getType());
        holder.pointsTv.setText(mRatesList.get(position).getPoints());
    }

    private void setViewColor(int color, ViewHolderUsersRates holder){
        holder.awayTeamTv.setTextColor(color);
        holder.homeTeamTv.setTextColor(color);
        holder.typeTv.setTextColor(color);
        holder.pointsTv.setTextColor(color);
    }

    @Override
    public int getItemCount() {
        return mRatesList.size();
    }

    public class ViewHolderUsersRates extends RecyclerView.ViewHolder {

        public final TextView homeTeamTv;
        public final TextView awayTeamTv;
        public final TextView typeTv;
        public final TextView pointsTv;

        public ViewHolderUsersRates(View v) {
            super(v);
            homeTeamTv = (TextView)v.findViewById(R.id.home_team_tv);
            awayTeamTv = (TextView)v.findViewById(R.id.away_team_tv);
            typeTv = (TextView)v.findViewById(R.id.bet_type_tv);
            pointsTv = (TextView)v.findViewById(R.id.max_win_points);
        }
    }
}
