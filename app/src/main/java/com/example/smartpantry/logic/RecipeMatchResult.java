package com.example.smartpantry.logic;

import com.example.smartpantry.model.Recipe;

import java.util.ArrayList;
import java.util.List;

/**
 * The outcome of running the strict-matching rule (see IngredientMatcher)
 * for one recipe against the user's current pantry.
 *
 * fullyMatched  -> true only if EVERY ingredient the recipe needs is present
 *                  in at least the required quantity (Section 2.3 of the brief).
 * missingCount  -> how many distinct ingredients are missing/short. Used to
 *                  build the optional "Almost There" list (missingCount == 1).
 * missingIngredientNames -> human-readable list of what's missing, shown on
 *                  the recipe detail screen so the user knows what to buy.
 */
public class RecipeMatchResult {

    private final Recipe recipe;
    private final boolean fullyMatched;
    private final int missingCount;
    private final List<String> missingIngredientNames;

    public RecipeMatchResult(Recipe recipe, boolean fullyMatched, int missingCount,
                              List<String> missingIngredientNames) {
        this.recipe = recipe;
        this.fullyMatched = fullyMatched;
        this.missingCount = missingCount;
        this.missingIngredientNames = missingIngredientNames != null
                ? missingIngredientNames : new ArrayList<String>();
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public boolean isFullyMatched() {
        return fullyMatched;
    }

    public int getMissingCount() {
        return missingCount;
    }

    public List<String> getMissingIngredientNames() {
        return missingIngredientNames;
    }

    /** True when the recipe qualifies for the bonus "Almost There" list. */
    public boolean isAlmostThere() {
        return !fullyMatched && missingCount == 1;
    }
}
