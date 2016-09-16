package info.androidhive.firebase.classes.recycleViewAdapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import info.androidhive.firebase.classes.retrofit.players.Player;
import info.androidhive.firebase.R;

public class AwayTeamPlayerAdapter extends RecyclerView.Adapter<AwayTeamPlayerAdapter.ViewHolderPlayer> {

    private final List<Player> mPlayerList;

    public AwayTeamPlayerAdapter(List<Player> mPlayerList) {
        this.mPlayerList = mPlayerList;
    }

    @Override
    public ViewHolderPlayer onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.away_team_player_row, parent, false);
        return new ViewHolderPlayer(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolderPlayer holder, int position) {
        if (mPlayerList.get(position).getJerseyNumber() != null)
            holder.numberTv.setText(mPlayerList.get(position).getJerseyNumber().toString());
        else holder.numberTv.setText("..!.");

        if (mPlayerList.get(position).getNationality() != null)
            holder.nationalityTv.setText(mPlayerList.get(position).getNationality());
        else holder.numberTv.setText("..!.");

        if (mPlayerList.get(position).getName() != null)
            holder.nameTv.setText(mPlayerList.get(position).getName());
        else holder.nameTv.setText("..!.");

        if (mPlayerList.get(position).getDateOfBirth() != null)
            holder.yearTv.setText(mPlayerList.get(position).getDateOfBirth());
        else holder.yearTv.setText("..!.");

        if (mPlayerList.get(position).getPosition() != null)
            holder.positionTv.setText(mPlayerList.get(position).getPosition());
        else holder.positionTv.setText("..!.");
    }

    @Override
    public int getItemCount() {
        return mPlayerList.size();
    }

    public class ViewHolderPlayer extends RecyclerView.ViewHolder {

        private final TextView numberTv;
        private final TextView nationalityTv;
        private final TextView nameTv;
        private final TextView yearTv;
        private final TextView positionTv;

        public ViewHolderPlayer(View v) {
            super(v);
            numberTv = (TextView) v.findViewById(R.id.player_number_tv);
            nationalityTv = (TextView) v.findViewById(R.id.player_nationality_tv);
            nameTv = (TextView) v.findViewById(R.id.player_name_tv);
            yearTv = (TextView) v.findViewById(R.id.player_year_tv);
            positionTv = (TextView) v.findViewById(R.id.player_position_tv);
        }
    }

}