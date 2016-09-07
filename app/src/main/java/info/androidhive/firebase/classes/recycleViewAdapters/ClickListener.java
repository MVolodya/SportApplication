package info.androidhive.firebase.classes.recycleViewAdapters;

import android.view.View;

public interface ClickListener {

    void onClick(View view, int position);

    void onLongClick(int position);
}
