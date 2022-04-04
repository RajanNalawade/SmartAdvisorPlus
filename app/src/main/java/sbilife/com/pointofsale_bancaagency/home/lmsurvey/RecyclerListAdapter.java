

package sbilife.com.pointofsale_bancaagency.home.lmsurvey;

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.view.MotionEventCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import sbilife.com.pointofsale_bancaagency.R;


public class RecyclerListAdapter extends RecyclerView.Adapter<RecyclerListAdapter.ItemViewHolder>
        implements ItemTouchHelperAdapter {

    private final List<String> mItems = new ArrayList<>();

    private final OnStartDragListener mDragStartListener;
    private final String identifier;
    private String others = "";


    public RecyclerListAdapter(Context context, OnStartDragListener dragStartListener, List<String> arrayList, String identifier) {
        mDragStartListener = dragStartListener;
        this.identifier = identifier;

        mItems.addAll(arrayList);
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_lm_strength, parent, false);
        ItemViewHolder itemViewHolder = new ItemViewHolder(view);
        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder holder, int position) {
        holder.tvListItemLMStrength.setText(mItems.get(position));
        holder.tvListRank.setText(position + "");

        String strOther = mItems.get(position);
        if (strOther.equalsIgnoreCase("Others") || strOther.equalsIgnoreCase("अन्य")) {
            holder.edittextOthers.setVisibility(View.VISIBLE);

            holder.edittextOthers.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    others = charSequence.toString();
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
        } else {
            holder.edittextOthers.setVisibility(View.GONE);
        }
        // Start a drag whenever the handle view it touched
        holder.linearlayoutBiggestStrength.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                    mDragStartListener.onStartDrag(holder, identifier);
                }
                return false;
            }
        });
    }

    @Override
    public void onItemDismiss(int position) {
//        mItems.remove(position);
//        notifyItemRemoved(position);
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {

        Collections.swap(mItems, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public List<String> getTotalItemList() {
        List<String> finalList = new ArrayList<>();
        finalList.addAll(mItems);
        if (!TextUtils.isEmpty(others)) {
            finalList.add("othersComment = " + others);
        }
        return finalList;
    }


    public static class ItemViewHolder extends RecyclerView.ViewHolder implements
            ItemTouchHelperViewHolder {

        public final TextView tvListItemLMStrength, tvListRank;
        public final EditText edittextOthers;
        public final LinearLayout linearlayoutBiggestStrength;

        public ItemViewHolder(View itemView) {
            super(itemView);
            tvListItemLMStrength = itemView.findViewById(R.id.tvListItemLMStrength);
            tvListRank = itemView.findViewById(R.id.tvListRank);

            edittextOthers = itemView.findViewById(R.id.edittextOthers);
            linearlayoutBiggestStrength = itemView.findViewById(R.id.linearlayoutBiggestStrength);
        }

        @Override
        public void onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY);
        }

        @Override
        public void onItemClear() {
            itemView.setBackgroundColor(0);
        }
    }
}
