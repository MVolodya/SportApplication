package info.androidhive.firebase.Classes.RecycleViewClasses;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import info.androidhive.firebase.Classes.Retrofit.League.LeagueModel;
import info.androidhive.firebase.R;

/**
 * Created by andri on 29.07.2016.
 */
public class LeagueAdapter extends RecyclerView.Adapter<LeagueAdapter.MyViewHolder> {

    private List<LeagueModel> leagueList;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView caption, league, year;

        public MyViewHolder(View view) {
            super(view);
            caption = (TextView) view.findViewById(R.id.caption);
            year = (TextView) view.findViewById(R.id.league);
            league = (TextView) view.findViewById(R.id.year);
        }
    }

    public LeagueAdapter(List<LeagueModel> moviesList) {
        this.leagueList = moviesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.football_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        LeagueModel model = leagueList.get(position);
        Log.d("caption",model.getCaption());
        holder.caption.setText(model.getCaption());
        holder.league.setText(model.getLeague());
        holder.year.setText(model.getYear());
    }

    @Override
    public int getItemCount() {
        return leagueList.size();
    }
}