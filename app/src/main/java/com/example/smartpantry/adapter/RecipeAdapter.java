package com.example.smartpantry.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartpantry.R;
import com.example.smartpantry.logic.RecipeMatchResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Displays the output of IngredientMatcher as a grouped list: a "You Can
 * Make Right Now" section (strict matches only, per Section 2.3) followed
 * by an optional "Almost There" section (missing exactly one ingredient,
 * the bonus feature from Section 8). Tapping a recipe opens RecipeDetailActivity.
 */
public class RecipeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_HEADER = 0;
    private static final int VIEW_TYPE_RECIPE = 1;

    public interface OnRecipeClickListener {
        void onRecipeClick(RecipeMatchResult result);
    }

    private List<RecipeListRow> rows = new ArrayList<>();
    private final OnRecipeClickListener listener;

    public RecipeAdapter(OnRecipeClickListener listener) {
        this.listener = listener;
    }

    public void setRows(List<RecipeListRow> newRows) {
        this.rows = newRows;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return rows.get(position).type == RecipeListRow.Type.HEADER
                ? VIEW_TYPE_HEADER : VIEW_TYPE_RECIPE;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == VIEW_TYPE_HEADER) {
            View view = inflater.inflate(R.layout.item_section_header, parent, false);
            return new HeaderViewHolder(view);
        } else {
            View view = inflater.inflate(R.layout.item_recipe, parent, false);
            return new RecipeViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        RecipeListRow row = rows.get(position);
        if (holder instanceof HeaderViewHolder) {
            ((HeaderViewHolder) holder).bind(row.headerText);
        } else if (holder instanceof RecipeViewHolder) {
            ((RecipeViewHolder) holder).bind(row.matchResult, listener);
        }
    }

    @Override
    public int getItemCount() {
        return rows.size();
    }

    static class HeaderViewHolder extends RecyclerView.ViewHolder {
        private final TextView headerText;

        HeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            headerText = itemView.findViewById(R.id.text_header);
        }

        void bind(String text) {
            headerText.setText(text);
        }
    }

    static class RecipeViewHolder extends RecyclerView.ViewHolder {
        private final TextView name;
        private final TextView status;

        RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.text_recipe_name);
            status = itemView.findViewById(R.id.text_recipe_status);
        }

        void bind(final RecipeMatchResult result, final OnRecipeClickListener listener) {
            name.setText(result.getRecipe().getName());

            if (result.isFullyMatched()) {
                status.setText("All ingredients available");
            } else if (result.isAlmostThere()) {
                status.setText("Missing: " + result.getMissingIngredientNames().get(0));
            } else {
                status.setText("Missing " + result.getMissingCount() + " ingredients");
            }

            itemView.setOnClickListener(v -> {
                if (listener != null) listener.onRecipeClick(result);
            });
        }
    }
}
