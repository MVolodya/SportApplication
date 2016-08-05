package info.androidhive.firebase.Classes.RecycleViewClasses;

import android.view.View;

/**
 * Created by andri on 03.08.2016.
 */
public interface ClickListener {

    void onClick(View view, int position);

    void onLongClick(View view, int position);
}
