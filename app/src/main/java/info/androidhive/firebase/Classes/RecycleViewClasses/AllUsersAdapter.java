package info.androidhive.firebase.Classes.RecycleViewClasses;

import android.provider.Telephony;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import info.androidhive.firebase.Classes.RatedUser;
import info.androidhive.firebase.Classes.Retrofit.League.LeagueModel;
import info.androidhive.firebase.R;

/**
 * Created by andrii on 16.08.16.
 */
public class AllUsersAdapter extends RecyclerView.Adapter<AllUsersAdapter.ViewHolderUsers> {

    private List<RatedUser> usersList;

    public AllUsersAdapter(List<RatedUser> usersList) {
        this.usersList = usersList;
        getSortedUsers();
    }

    @Override
    public ViewHolderUsers onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.users_list_row, parent, false);

        return new ViewHolderUsers(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolderUsers holder, int position) {
        RatedUser ratedUser = usersList.get(position);
        String positionUser = String.valueOf(position+1);

        holder.position.setText(positionUser);
        holder.name.setText(ratedUser.getName());
        holder.points.setText(ratedUser.getCurrentPoints());
    }

    private void getSortedUsers(){
        Collections.sort(usersList, new Comparator<RatedUser>() {
            public int compare(RatedUser o1, RatedUser o2) {
                return o2.currentPoints.compareTo(o1.currentPoints);
            }
        });
    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    public class ViewHolderUsers extends RecyclerView.ViewHolder {

        public TextView position;
        public TextView name;
        public TextView points;

        public ViewHolderUsers(View v) {
            super(v);
            position = (TextView)v.findViewById(R.id.textViewUserPosition);
            name = (TextView)v.findViewById(R.id.textViewUserRateName);
            points = (TextView)v.findViewById(R.id.textView7UserPoints);
        }
    }
}
