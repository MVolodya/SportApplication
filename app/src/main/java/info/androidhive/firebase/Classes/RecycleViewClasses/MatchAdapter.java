package info.androidhive.firebase.Classes.RecycleViewClasses;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import info.androidhive.firebase.Classes.Retrofit.Match.MatchModel;
import info.androidhive.firebase.R;

public class MatchAdapter extends RecyclerView.Adapter<MatchAdapter.MyViewHolder> {

    private List<MatchModel> matchList;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView tvHomeTeam;
        public TextView tvAwayTeam;
        public TextView tvHomeTeamGoal;
        public TextView tvAwayTeamGoal;


        public MyViewHolder(View view) {
            super(view);
            tvHomeTeam = (TextView) view.findViewById(R.id.homeTeam);
            tvAwayTeam = (TextView) view.findViewById(R.id.awayTeam);
            tvHomeTeamGoal = (TextView) view.findViewById(R.id.goalsHomeTeam);
            tvAwayTeamGoal = (TextView) view.findViewById(R.id.goalsAwayTeam);
        }
    }

    public MatchAdapter(List<MatchModel> matchList) {
        this.matchList = matchList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.match_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        MatchModel model = matchList.get(position);
        //Log.d("caption",model.getCaption());
        holder.tvHomeTeam.setText(model.getHomeTeamName());
        holder.tvAwayTeam.setText(model.getAwayTeamName());
        holder.tvHomeTeamGoal.setText(model.getGoalsHomeTeam());
        holder.tvAwayTeamGoal.setText(model.getGoalsAwayTeam());
    }

    @Override
    public int getItemCount() {
        return matchList.size();
    }
}