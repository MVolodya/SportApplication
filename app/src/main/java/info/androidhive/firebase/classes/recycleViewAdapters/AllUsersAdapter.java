package info.androidhive.firebase.classes.recycleViewAdapters;

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
import info.androidhive.firebase.classes.models.RatedUser;
import info.androidhive.firebase.R;

public class AllUsersAdapter extends RecyclerView.Adapter<AllUsersAdapter.ViewHolderUsers> {

    private final List<RatedUser> mUsersList;
    private View view;

    public AllUsersAdapter(List<RatedUser> mUsersList) {
        this.mUsersList = mUsersList;
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
        RatedUser ratedUser = mUsersList.get(position);

        if(ratedUser.getName().equalsIgnoreCase(FirebaseAuth.getInstance().getCurrentUser().getDisplayName())){
            holder.etPosition.setTextColor(Color.parseColor("#ff6861"));
            holder.etName.setTextColor(Color.parseColor("#ff6861"));
        }else {
            holder.etPosition.setTextColor(Color.parseColor("#999999"));
            holder.etName.setTextColor(Color.parseColor("#000000"));
        }

        String positionUser = String.valueOf(position+1);
        holder.etPosition.setText(positionUser);
        holder.etName.setText(ratedUser.getName());
        holder.etPoints.setText(ratedUser.getCurrentPoints());
        Glide.with(view.getContext())
                .load(ratedUser.getPhotoUrl())
                .override(100, 100)
                .into(holder.ivUserPhoto);
    }

    private void getSortedUsers(){
        Collections.sort(mUsersList, new Comparator<RatedUser>() {
            public int compare(RatedUser o1, RatedUser o2) {
                return Double.compare(Double.parseDouble(o2.getCurrentPoints()),
                        Double.parseDouble(o1.getCurrentPoints()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mUsersList.size();
    }

    public class ViewHolderUsers extends RecyclerView.ViewHolder {

        public final TextView etPosition;
        public final TextView etName;
        public final TextView etPoints;
        public final TextView tvHint;
        public final CircleImageView ivUserPhoto;

        public ViewHolderUsers(View v) {
            super(v);
            etPosition = (TextView)v.findViewById(R.id.tv_user_position);
            etName = (TextView)v.findViewById(R.id.tv_user_rate_name);
            etPoints = (TextView)v.findViewById(R.id.tv_user_points);
            ivUserPhoto = (CircleImageView)v.findViewById(R.id.image_view_profile);
            tvHint = (TextView)v.findViewById(R.id.tv_hint_points);
        }
    }
}
