package info.androidhive.firebase.Classes.RecycleViewClasses;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import info.androidhive.firebase.Classes.Retrofit.Match.FixtResponse;
import info.androidhive.firebase.R;

public class MatchAdapter extends RecyclerView.Adapter<MatchAdapter.MyViewHolder> {

    private FixtResponse fixtResponse;

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

    public MatchAdapter(FixtResponse fixtResponse) {
        this.fixtResponse = fixtResponse;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.match_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

//        Log.d("caption",fixtResponse.getFixtures().get(position).getResult()
//                .getGoalsHomeTeam().toString());
        holder.tvHomeTeam.setText(fixtResponse.getFixtures().get(position).getHomeTeamName());
        holder.tvAwayTeam.setText(fixtResponse.getFixtures().get(position).getAwayTeamName());
        holder.tvHomeTeamGoal.setText(fixtResponse.getFixtures().get(position).getResult()
                .getGoalsHomeTeam().toString());
        holder.tvAwayTeamGoal.setText(fixtResponse.getFixtures().get(position).getResult()
                .getGoalsAwayTeam().toString());
    }

    @Override
    public int getItemCount() {
        return fixtResponse.getFixtures().size();
    }

}