package vn.kingbee.widget.recyclerview;

import androidx.recyclerview.widget.RecyclerView;

public interface BaseAdapterListener { //NOSONAR
    void onItemClicked(RecyclerView.ViewHolder holder, int position);
}
