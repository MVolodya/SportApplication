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
            holder.tvNumber.setText(mPlayerList.get(position).getJerseyNumber().toString());
        else holder.tvNumber.setText("..!.");

        if (mPlayerList.get(position).getNationality() != null)
            holder.tvNationality.setText(mPlayerList.get(position).getNationality());
        else holder.tvNumber.setText("..!.");

        if (mPlayerList.get(position).getName() != null)
            holder.tvName.setText(mPlayerList.get(position).getName());
        else holder.tvName.setText("..!.");

        if (mPlayerList.get(position).getDateOfBirth() != null)
            holder.tvYear.setText(mPlayerList.get(position).getDateOfBirth());
        else holder.tvYear.setText("..!.");

        if (mPlayerList.get(position).getPosition() != null)
            holder.tvPosition.setText(mPlayerList.get(position).getPosition());
        else holder.tvPosition.setText("..!.");
    }

    @Override
    public int getItemCount() {
        return mPlayerList.size();
    }

    public class ViewHolderPlayer extends RecyclerView.ViewHolder {

        private final TextView tvNumber;
        private final TextView tvNationality;
        private final TextView tvName;
        private final TextView tvYear;
        private final TextView tvPosition;

        public ViewHolderPlayer(View v) {
            super(v);
            tvNumber = (TextView) v.findViewById(R.id.player_number_tv);
            tvNationality = (TextView) v.findViewById(R.id.player_nationality_tv);
            tvName = (TextView) v.findViewById(R.id.tv_player_name);
            tvYear = (TextView) v.findViewById(R.id.player_year_tv);
            tvPosition = (TextView) v.findViewById(R.id.player_position_tv);
        }
    }

}