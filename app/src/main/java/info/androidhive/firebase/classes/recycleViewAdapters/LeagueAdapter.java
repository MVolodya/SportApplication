package info.androidhive.firebase.classes.recycleViewAdapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import info.androidhive.firebase.classes.retrofit.league.LeagueModel;
import info.androidhive.firebase.R;

public class LeagueAdapter extends RecyclerView.Adapter<LeagueAdapter.MyViewHolder> {

    private final List<LeagueModel> leagueList;

    public LeagueAdapter(List<LeagueModel> leagueList) {
        this.leagueList = leagueList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.league_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        LeagueModel model = leagueList.get(position);
        if(!model.getCaption().equalsIgnoreCase("DFB-Pokal 2016/17") ||
                !model.getCaption().equalsIgnoreCase("European Championships France 2016")) {
            holder.caption.setText(model.getCaption());
            holder.league.setText(model.getLeague());
            holder.year.setText(model.getYear());
            holder.numberOfTeams.setText(model.getNumberOfTeams());
        }

    }

    @Override
    public int getItemCount() {
        return leagueList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public final TextView caption;
        public final TextView league;
        public final TextView year;
        public final TextView numberOfTeams;

        public MyViewHolder(View view) {
            super(view);
            caption = (TextView) view.findViewById(R.id.caption);
            year = (TextView) view.findViewById(R.id.year);
            league = (TextView) view.findViewById(R.id.league);
            numberOfTeams = (TextView) view.findViewById(R.id.numberOfTeams);
        }
    }
}