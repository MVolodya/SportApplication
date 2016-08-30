package info.androidhive.firebase.Classes.RecycleViewAdapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import info.androidhive.firebase.Classes.Retrofit.Players.Player;
import info.androidhive.firebase.R;

/**
 * Created by andrii on 25.08.16.
 */
public class AwayTeamPlayerAdapter extends RecyclerView.Adapter<AwayTeamPlayerAdapter.ViewHolderPlayer> {

    private List<Player> playerList;


    public AwayTeamPlayerAdapter(List<Player> playerList) {
        this.playerList = playerList;
    }

    @Override
    public ViewHolderPlayer onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.away_team_player_row, parent, false);

        return new ViewHolderPlayer(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolderPlayer holder, int position) {
        if(playerList.get(position).getJerseyNumber()!=null)
            holder.number.setText(playerList.get(position).getJerseyNumber().toString());
        else holder.number.setText("..!.");

        if(playerList.get(position).getNationality()!=null)
            holder.nationality.setText(playerList.get(position).getNationality());
        else holder.number.setText("..!.");

        if(playerList.get(position).getName()!=null)
            holder.name.setText(playerList.get(position).getName());
        else holder.name.setText("..!.");

        if(playerList.get(position).getDateOfBirth()!=null)
            holder.year.setText(playerList.get(position).getDateOfBirth());
        else holder.year.setText("..!.");

        if(playerList.get(position).getPosition()!=null)
            holder.position.setText(playerList.get(position).getPosition());
        else holder.position.setText("..!.");
    }

    @Override
    public int getItemCount() {

        return playerList.size();
    }

    public class ViewHolderPlayer extends RecyclerView.ViewHolder {

        private TextView number;
        private TextView nationality;
        private TextView name;
        private TextView year;
        private TextView position;

        public ViewHolderPlayer (View v) {
            super(v);
            number = (TextView)v.findViewById(R.id.textViewNumber);
            nationality = (TextView)v.findViewById(R.id.textViewNationality);
            name = (TextView)v.findViewById(R.id.textView7Name);
            year = (TextView)v.findViewById(R.id.textViewYear);
            position = (TextView)v.findViewById(R.id.textViewPosition);
        }
    }

}