package vn.kingbee.widget.dialog.ext.help.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import vn.kingbee.widget.R;
import vn.kingbee.widget.dialog.ext.help.holder.HelpAnswerViewHolder;
import vn.kingbee.widget.dialog.ext.help.holder.HelpCategoryViewHolder;
import vn.kingbee.widget.dialog.ext.help.holder.HelpQuestionViewHolder;
import vn.kingbee.widget.dialog.ext.help.model.HelpFAQ;
import vn.kingbee.widget.recyclerview.base.BaseRecyclerViewAdapter;

public class HelpAdapter extends BaseRecyclerViewAdapter<HelpFAQ> implements HelpViewHolder.HelpViewHolderListener {
    private static final int TYPE_ITEM_CATEGORY = 3;
    private static final int TYPE_ITEM_CHILD = 4;
    private OnExpandItemListener mListener;

    /**
     * Create list adapter.
     *
     * @param context The context which will show list item.
     * @param items   List data.
     */
    public HelpAdapter(Context context, List<HelpFAQ> items, OnExpandItemListener listener) {
        super(context, items);
        mListener = listener;
    }

    @NotNull
    @Override
    public GenericViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM_CATEGORY) {
            View view = getInflater().inflate(R.layout.view_help_item_category, parent, false);
            return new HelpCategoryViewHolder(view, this);
        } else if (viewType == TYPE_ITEM_CHILD) {
            View view = getInflater().inflate(R.layout.view_help_item_content, parent, false);
            return new HelpAnswerViewHolder(view, this);
        }
        return super.onCreateViewHolder(parent, viewType);
    }

    @NotNull
    @Override
    public HelpQuestionViewHolder onCreateContentHolder(@NotNull ViewGroup parent) {
        View view = getInflater().inflate(R.layout.view_help_item, parent, false);
        return new HelpQuestionViewHolder(view, this);
    }

    @NotNull
    @Override
    public GenericViewHolder onCreateFooterHolder(@NotNull ViewGroup parent) {
        throw new UnsupportedOperationException();
    }

    @NotNull
    @Override
    public GenericViewHolder onCreateHeaderHolder(@NotNull ViewGroup parent) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getItemViewType(int position) {
        if (!TextUtils.isEmpty(getItem(position).getCategory())) {
            return TYPE_ITEM_CATEGORY;
        } else if (TextUtils.isEmpty(getItem(position).getQuestion())) {
            return TYPE_ITEM_CHILD;
        }
        return super.getItemViewType(position);
    }

    @Override
    public List<HelpFAQ> getItemList() {
        return getItems();
    }

    @Override
    public void onExpandItem(int position) {
        if (mListener != null) {
            mListener.onExpandItem(position);
        }
    }

    public interface OnExpandItemListener {
        void onExpandItem(int pos);
    }
}
