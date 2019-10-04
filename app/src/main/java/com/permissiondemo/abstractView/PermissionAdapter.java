package com.permissiondemo.abstractView;

import android.content.res.ColorStateList;
import android.os.Build;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.permissiondemo.R;

import java.util.ArrayList;
import java.util.List;

import static com.permissiondemo.abstractView.ItemHolder.MULTI_SELECTION;

class PermissionAdapter extends RecyclerView.Adapter<ItemHolder> implements OnItemSelectedListener {


    private List<SelectableItem> mValues = new ArrayList<>();
    private boolean isMultiSelectionEnabled = false;
    private OnItemSelectedListener listener;

    void addPermissionData(String[] permissions) {

        List<Item> items = null;
        if (permissions != null) {
            items = new ArrayList<>();
            for (String p : permissions) {
                items.add(new Item(p));
            }
        }

        if (items != null) {
            mValues.clear();
            for (Item item : items) {
                mValues.add(new SelectableItem(item, false));
            }
        }
    }

    void setListener(OnItemSelectedListener listener) {
        this.listener = listener;
    }

    void setMultiSelectionEnabled() {
        this.isMultiSelectionEnabled = true;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.items, parent, false);

        return new ItemHolder(itemView, this);

    }


    @Override
    public void onBindViewHolder(@NonNull ItemHolder viewHolder, final int position) {


        SelectableItem selectableItem = mValues.get(position);
        String name = selectableItem.getName().replace("android.permission.", "");

        viewHolder.textView.setText(name);
        viewHolder.mItem = selectableItem;
        viewHolder.setChecked(viewHolder.mItem.isSelected());

        if (isMultiSelectionEnabled) {
            onMultipleSelectView(viewHolder);
        } else {
            onSingleSelectView(viewHolder);
        }
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }


    List<Item> getMultipleSelectedItems() {

        List<Item> selectedItems = new ArrayList<>();
        for (SelectableItem item : mValues) {
            if (item.isSelected()) {
                selectedItems.add(item);
            }
        }
        return selectedItems;
    }


    @Override
    public int getItemViewType(int position) {
        if (isMultiSelectionEnabled) {
            return MULTI_SELECTION;
        } else {
            return ItemHolder.SINGLE_SELECTION;
        }
    }

    @Override
    public void onItemSelected(SelectableItem item) {
        if (!isMultiSelectionEnabled) {

            for (SelectableItem selectableItem : mValues) {
                if (!selectableItem.equals(item)
                        && selectableItem.isSelected()) {
                    selectableItem.setSelected(false);
                } else if (selectableItem.equals(item)
                        && item.isSelected()) {
                    selectableItem.setSelected(true);
                }
            }
            notifyDataSetChanged();
            listener.onItemSelected(item);
        }

    }


    private void onSingleSelectView(ItemHolder holder) {
        TypedValue value = new TypedValue();
        holder.textView.getContext().getTheme().resolveAttribute(android.R.attr.listChoiceIndicatorSingle, value, true);
        int checkMarkDrawableResId = value.resourceId;
        holder.textView.setCheckMarkDrawable(checkMarkDrawableResId);
        int color =  ContextCompat.getColor(holder.textView.getContext(), R.color.singleBG);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            holder.textView.setCheckMarkTintList(ColorStateList.valueOf(color));
        }
        holder.textView.setTextColor(color);
      }

    private void onMultipleSelectView(ItemHolder holder) {
        TypedValue value = new TypedValue();
        holder.textView.getContext().getTheme().resolveAttribute(android.R.attr.listChoiceIndicatorMultiple, value, true);
        int checkMarkDrawableResId = value.resourceId;
        holder.textView.setCheckMarkDrawable(checkMarkDrawableResId);
        int color =  ContextCompat.getColor(holder.textView.getContext(), R.color.multipleBG);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            holder.textView.setCheckMarkTintList(ColorStateList.valueOf(color));
        }
        holder.textView.setTextColor(color);

    }

}
