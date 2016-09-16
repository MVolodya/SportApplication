package info.androidhive.firebase.classes.managers.swipeManager;

import android.content.Context;
import android.graphics.Paint;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import info.androidhive.firebase.R;
import info.androidhive.firebase.classes.managers.RateManager;
import info.androidhive.firebase.classes.models.Rate;
import info.androidhive.firebase.classes.models.RatedMatchesToDB;
import info.androidhive.firebase.classes.recycleViewAdapters.UsersRateAdapter;

public class SwipeManager extends ItemTouchHelper.SimpleCallback implements InitDeletedRate {

    private Paint p = new Paint();
    private Context context;
    private RateManager rateManager;
    private List<Rate> rates;
    private View view;
    private UsersRateAdapter usersRateAdapter;
    private RatedMatchesToDB matchesToDB;

    public SwipeManager(UsersRateAdapter usersRateAdapter, View view, Context context,
                        List<Rate> rates) {
        super(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        this.usersRateAdapter = usersRateAdapter;
        this.view = view;
        this.context = context;
        this.rates = rates;
        rateManager = new RateManager(context);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction) {
        final CoordinatorLayout coordinatorLayout = (CoordinatorLayout) view.findViewById(R.id.fabLayout);
        final Rate rate = rates.get(viewHolder.getAdapterPosition());
        usersRateAdapter.remove(viewHolder.getAdapterPosition());
        rateManager.deleteRate(FirebaseAuth.getInstance().getCurrentUser().getDisplayName(),
                rate, SwipeManager.this);
        Snackbar snackbar = Snackbar
                .make(coordinatorLayout, R.string.rate_deleted, Snackbar.LENGTH_LONG)
                .setAction(R.string.undo, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        usersRateAdapter.addDeletedRate(rate);
                        rateManager.setDeletedRate(FirebaseAuth.getInstance().getCurrentUser().getDisplayName(),
                                matchesToDB);
                        Snackbar snackbarMsg = Snackbar.make(coordinatorLayout, R.string.rate_restored, Snackbar.LENGTH_SHORT);
                        snackbarMsg.show();
                    }
                });
        snackbar.show();
    }

    @Override
    public void initRate(RatedMatchesToDB ratedMatchesToDB) {
        this.matchesToDB = ratedMatchesToDB;
    }
}
