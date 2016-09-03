package info.androidhive.firebase.Classes.RecycleViewAdapters;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import info.androidhive.firebase.Classes.Models.RatedUser;
import info.androidhive.firebase.R;


public class AllUsersAdapter extends RecyclerView.Adapter<AllUsersAdapter.ViewHolderUsers> {

    private final List<RatedUser> usersList;
    private View view;

    public AllUsersAdapter(List<RatedUser> usersList) {
        this.usersList = usersList;
        getSortedUsers();
    }

    @Override
    public ViewHolderUsers onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.users_list_row, parent, false);

        return new ViewHolderUsers(view);
    }

    @Override
    public void onBindViewHolder(ViewHolderUsers holder, int position) {
        RatedUser ratedUser = usersList.get(position);

        if(ratedUser.getName().equalsIgnoreCase(FirebaseAuth.getInstance().getCurrentUser().getDisplayName())){
            holder.position.setTextColor(Color.parseColor("#ff6861"));
            holder.name.setTextColor(Color.parseColor("#ff6861"));
            holder.points.setTextColor(Color.parseColor("#ff6861"));
            holder.tvHint.setTextColor(Color.parseColor("#ff6861"));
        }

        String positionUser = String.valueOf(position+1);
        holder.position.setText(positionUser);
        holder.name.setText(ratedUser.getName());
        holder.points.setText(ratedUser.getCurrentPoints());
        Glide.with(view.getContext())
                .load(ratedUser.getPhotoUrl())
                .into(holder.userPhoto);
    }

    private void getSortedUsers(){
        Collections.sort(usersList, new Comparator<RatedUser>() {
            public int compare(RatedUser o1, RatedUser o2) {
                return Double.compare(Double.parseDouble(o2.getCurrentPoints()),
                        Double.parseDouble(o1.getCurrentPoints()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    public class ViewHolderUsers extends RecyclerView.ViewHolder {

        public final TextView position;
        public final TextView name;
        public final TextView points;
        public final TextView tvHint;
        public final CircleImageView userPhoto;

        public ViewHolderUsers(View v) {
            super(v);
            position = (TextView)v.findViewById(R.id.textViewUserPosition);
            name = (TextView)v.findViewById(R.id.textViewUserRateName);
            points = (TextView)v.findViewById(R.id.textView7UserPoints);
            userPhoto = (CircleImageView)v.findViewById(R.id.profile_image);
            tvHint = (TextView)v.findViewById(R.id.textView3);
        }
    }
}
