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

    private final List<LeagueModel> mLeagueList;

    public LeagueAdapter(List<LeagueModel> mLeagueList) {
        this.mLeagueList = mLeagueList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.league_list_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        LeagueModel model = mLeagueList.get(position);
        if(!model.getCaption().equals("DFB-Pokal 2016/17") ||
                !model.getCaption().equals("European Championships France 2016")) {
            holder.captionTv.setText(model.getCaption());
            holder.leagueTv.setText(model.getLeague());
            holder.yearTv.setText(model.getYear());
            holder.numberOfTeamsTv.setText(model.getNumberOfTeams());
        }
    }

    @Override
    public int getItemCount() {
        return mLeagueList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public final TextView captionTv;
        public final TextView leagueTv;
        public final TextView yearTv;
        public final TextView numberOfTeamsTv;

        public MyViewHolder(View view) {
            super(view);
            captionTv = (TextView) view.findViewById(R.id.caption_tv);
            yearTv = (TextView) view.findViewById(R.id.year_tv);
            leagueTv = (TextView) view.findViewById(R.id.league_tv);
            numberOfTeamsTv = (TextView) view.findViewById(R.id.number_of_teams_tv);
        }
    }
}