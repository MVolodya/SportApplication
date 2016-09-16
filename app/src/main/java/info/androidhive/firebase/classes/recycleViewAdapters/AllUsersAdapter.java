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
            holder.positionEt.setTextColor(Color.parseColor("#ff6861"));
            holder.nameEt.setTextColor(Color.parseColor("#ff6861"));
        }else {
            holder.positionEt.setTextColor(Color.parseColor("#999999"));
            holder.nameEt.setTextColor(Color.parseColor("#000000"));
        }

        String positionUser = String.valueOf(position+1);
        holder.positionEt.setText(positionUser);
        holder.nameEt.setText(ratedUser.getName());
        holder.pointsEt.setText(ratedUser.getCurrentPoints());
        Glide.with(view.getContext())
                .load(ratedUser.getPhotoUrl())
                .override(100, 100)
                .into(holder.userPhotoIv);
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

        public final TextView positionEt;
        public final TextView nameEt;
        public final TextView pointsEt;
        public final TextView hintTv;
        public final CircleImageView userPhotoIv;

        public ViewHolderUsers(View v) {
            super(v);
            positionEt = (TextView)v.findViewById(R.id.user_position_tv);
            nameEt = (TextView)v.findViewById(R.id.user_rate_name_tv);
            pointsEt = (TextView)v.findViewById(R.id.user_points_tv);
            userPhotoIv = (CircleImageView)v.findViewById(R.id.profile_iv);
            hintTv = (TextView)v.findViewById(R.id.hint_points_tv);
        }
    }
}
