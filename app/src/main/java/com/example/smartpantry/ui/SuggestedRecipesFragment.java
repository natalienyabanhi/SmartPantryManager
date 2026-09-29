package com.example.smartpantry.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartpantry.R;
import com.example.smartpantry.RecipeDetailActivity;
import com.example.smartpantry.adapter.RecipeAdapter;
import com.example.smartpantry.adapter.RecipeListRow;
import com.example.smartpantry.db.DatabaseHelper;
import com.example.smartpantry.logic.IngredientMatcher;
import com.example.smartpantry.logic.RecipeMatchResult;
import com.example.smartpantry.model.PantryItem;
import com.example.smartpantry.model.Recipe;

import java.util.ArrayList;
import java.util.List;

/**
 * Suggested Recipes screen (Section 2.2 & 2.3): runs the strict-matching
 * rule from IngredientMatcher against the current pantry every time this
 * tab is shown, so adding/removing an ingredient on the Pantry tab is
 * immediately reflected here - this is exactly what the video demo
 * (Section 5.1.2) needs to prove.
 */
public class SuggestedRecipesFragment extends Fragment implements RecipeAdapter.OnRecipeClickListener {

    private DatabaseHelper dbHelper;
    private RecipeAdapter adapter;
    private RecyclerView recyclerView;
    private View emptyStateView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                              @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_suggested_recipes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dbHelper = DatabaseHelper.getInstance(requireContext());

        recyclerView = view.findViewById(R.id.recycler_recipes);
        emptyStateView = view.findViewById(R.id.text_empty_suggestions);

        adapter = new RecipeAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshSuggestions();
    }

    private void refreshSuggestions() {
        List<PantryItem> pantry = dbHelper.getAllPantryItems();
        List<Recipe> allRecipes = dbHelper.getAllRecipes();
        List<RecipeMatchResult> results = IngredientMatcher.matchAll(allRecipes, pantry);

        List<RecipeMatchResult> fullMatches = new ArrayList<>();
        List<RecipeMatchResult> almostThere = new ArrayList<>();

        for (RecipeMatchResult result : results) {
            if (result.isFullyMatched()) {
                fullMatches.add(result);
            } else if (result.isAlmostThere()) {
                // Bonus feature (Section 8): recipes missing exactly one
                // ingredient, kept clearly separate from the strict list.
                almostThere.add(result);
            }
        }

        List<RecipeListRow> rows = new ArrayList<>();
        if (!fullMatches.isEmpty()) {
            rows.add(RecipeListRow.header(getString(R.string.suggested_header)));
            for (RecipeMatchResult result : fullMatches) {
                rows.add(RecipeListRow.recipe(result));
            }
        }
        if (!almostThere.isEmpty()) {
            rows.add(RecipeListRow.header(getString(R.string.almost_there_header)));
            for (RecipeMatchResult result : almostThere) {
                rows.add(RecipeListRow.recipe(result));
            }
        }

        adapter.setRows(rows);

        boolean nothingToShow = fullMatches.isEmpty() && almostThere.isEmpty();
        recyclerView.setVisibility(nothingToShow ? View.GONE : View.VISIBLE);
        emptyStateView.setVisibility(nothingToShow ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onRecipeClick(RecipeMatchResult result) {
        Intent intent = new Intent(requireContext(), RecipeDetailActivity.class);
        intent.putExtra(RecipeDetailActivity.EXTRA_RECIPE_ID, result.getRecipe().getId());
        startActivity(intent);
    }
}
