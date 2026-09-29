package com.example.smartpantry.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartpantry.R;
import com.example.smartpantry.model.PantryItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Binds the user's pantry contents to a RecyclerView (Section 3.1: "At
 * least one RecyclerView or ListView with a custom Adapter"). Two click
 * targets are exposed via listeners: tapping a row opens it for editing,
 * tapping the trash icon deletes it immediately.
 */
public class PantryAdapter extends RecyclerView.Adapter<PantryAdapter.PantryViewHolder> {

    public interface OnPantryItemClickListener {
        void onItemClick(PantryItem item);
        void onDeleteClick(PantryItem item);
    }

    private List<PantryItem> items = new ArrayList<>();
    private final OnPantryItemClickListener listener;

    public PantryAdapter(OnPantryItemClickListener listener) {
        this.listener = listener;
    }

    public void setItems(List<PantryItem> newItems) {
        this.items = newItems;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PantryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pantry, parent, false);
        return new PantryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PantryViewHolder holder, int position) {
        PantryItem item = items.get(position);
        holder.bind(item, listener);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class PantryViewHolder extends RecyclerView.ViewHolder {
        private final TextView name;
        private final TextView quantity;
        private final TextView expiry;
        private final ImageButton deleteButton;

        PantryViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.text_item_name);
            quantity = itemView.findViewById(R.id.text_item_quantity);
            expiry = itemView.findViewById(R.id.text_item_expiry);
            deleteButton = itemView.findViewById(R.id.btn_delete_item);
        }

        void bind(final PantryItem item, final OnPantryItemClickListener listener) {
            name.setText(item.getName());
            quantity.setText(formatQuantity(item.getQuantity()) + " " + item.getUnit());

            if (item.hasExpiryDate()) {
                expiry.setVisibility(View.VISIBLE);
                expiry.setText("Expires: " + item.getExpiryDate());
            } else {
                expiry.setVisibility(View.GONE);
            }

            itemView.setOnClickListener(v -> {
                if (listener != null) listener.onItemClick(item);
            });
            deleteButton.setOnClickListener(v -> {
                if (listener != null) listener.onDeleteClick(item);
            });
        }

        private String formatQuantity(double value) {
            if (value == Math.floor(value)) {
                return String.valueOf((long) value);
            }
            return String.valueOf(value);
        }
    }
}
