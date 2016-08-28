package info.androidhive.firebase.Classes.RecycleViewClasses;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import info.androidhive.firebase.Classes.Retrofit.Match.Fixture;
import info.androidhive.firebase.R;

public class MatchAdapter extends RecyclerView.Adapter<MatchAdapter.MyViewHolder> {

    private List<Fixture> fixturesList;


    public MatchAdapter(List<Fixture> fixturesList) {
        this.fixturesList = fixturesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.match_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
            holder.tvHomeTeam.setText(fixturesList.get(position).getHomeTeamName());
            holder.tvAwayTeam.setText(fixturesList.get(position).getAwayTeamName());
            holder.tvAwayTeamGoal.setText(fixturesList.get(position).getDate());
    }

    @Override
    public int getItemCount() {

        return fixturesList.size();
    }

   public static String getMatchId (String link){
       return link.substring(41);
   }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView tvHomeTeam;
        public TextView tvAwayTeam;
        public TextView tvAwayTeamGoal;


        public MyViewHolder(View view) {
            super(view);
            tvHomeTeam = (TextView) view.findViewById(R.id.homeTeam);
            tvAwayTeam = (TextView) view.findViewById(R.id.awayTeam);
            tvAwayTeamGoal = (TextView) view.findViewById(R.id.goalsAwayTeam);
        }
    }

}