package vn.kingbee.widget.recyclerview;

import android.support.v7.widget.RecyclerView;

public interface BaseAdapterListener { //NOSONAR
    void onItemClicked(RecyclerView.ViewHolder holder, int position);
}
