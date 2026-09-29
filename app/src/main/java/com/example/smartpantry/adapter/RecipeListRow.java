package com.example.smartpantry.adapter;

import com.example.smartpantry.logic.RecipeMatchResult;

/**
 * A single row in the Suggested Recipes RecyclerView. The list is built
 * from two row types so that "You Can Make Right Now" and the bonus
 * "Almost There" (Section 8) results can be shown as clearly separated
 * groups, as the brief requires, inside one scrolling list.
 */
public class RecipeListRow {

    public enum Type { HEADER, RECIPE }

    public final Type type;
    public final String headerText;
    public final RecipeMatchResult matchResult;

    public static RecipeListRow header(String text) {
        return new RecipeListRow(Type.HEADER, text, null);
    }

    public static RecipeListRow recipe(RecipeMatchResult result) {
        return new RecipeListRow(Type.RECIPE, null, result);
    }

    private RecipeListRow(Type type, String headerText, RecipeMatchResult matchResult) {
        this.type = type;
        this.headerText = headerText;
        this.matchResult = matchResult;
    }
}
