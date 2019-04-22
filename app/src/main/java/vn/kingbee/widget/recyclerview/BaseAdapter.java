package vn.kingbee.widget.recyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;

import java.util.List;

public abstract class BaseAdapter<T, K extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<K> {
    protected static final int TYPE_HEADER = 1;
    protected static final int TYPE_FOOTER = 2;
    protected static final int TYPE_ITEM = 3;
    protected BaseAdapterListener mOnItemClickListener;
    protected List<T> mData;
    protected Context mContext;
    protected LayoutInflater mLayoutInflater;

    public BaseAdapter(Context context) {
        this(context, null);
    }

    public BaseAdapter(Context context, BaseAdapterListener onItemClickListener) {
        init(context);
        this.mOnItemClickListener = onItemClickListener;
    }

    protected void init(Context context) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    public void setOnItemClickListener(BaseAdapterListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    protected void onItemClicked(K holder, int pos) {
        mOnItemClickListener.onItemClicked(holder, pos);
    }

    @Override
    public void onBindViewHolder(final K holder, final int position) {
        holder.itemView.setOnClickListener(v -> onItemClicked(holder, position));
    }

    public T getItem(int pos) {
        return mData.get(pos);
    }

    public void setData(List<T> data) {
        mData = data;
    }

    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public void clear() {
        if (mData != null) {
            mData.clear();
        }
    }

    public void add(T item) {
        if (mData != null) {
            mData.add(item);
        }
    }
}
