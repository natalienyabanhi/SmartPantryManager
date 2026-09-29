package com.example.smartpantry.logic;

import com.example.smartpantry.model.PantryItem;
import com.example.smartpantry.model.Recipe;
import com.example.smartpantry.model.RecipeIngredient;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for IngredientMatcher - specifically targeting the strict-
 * matching rule described in Section 2.3 of the brief, since this is the
 * piece of logic the marker tests directly. Run with: ./gradlew test
 */
public class IngredientMatcherTest {

    private Recipe buildTomatoPasta() {
        Recipe recipe = new Recipe(1, "Tomato Pasta", "Cook it.");
        recipe.addIngredient(new RecipeIngredient(1, 1, "pasta", 200, "g"));
        recipe.addIngredient(new RecipeIngredient(2, 1, "tomato", 3, "unit"));
        recipe.addIngredient(new RecipeIngredient(3, 1, "garlic", 2, "unit"));
        return recipe;
    }

    @Test
    public void fullyStockedPantry_recipeIsSuggested() {
        Recipe recipe = buildTomatoPasta();
        List<PantryItem> pantry = Arrays.asList(
                new PantryItem(1, "pasta", 500, "g", null),
                new PantryItem(2, "tomato", 5, "unit", null),
                new PantryItem(3, "garlic", 4, "unit", null)
        );

        RecipeMatchResult result = IngredientMatcher.match(recipe, pantry);

        assertTrue("Recipe should be fully matched when all ingredients are present",
                result.isFullyMatched());
        assertEquals(0, result.getMissingCount());
    }

    @Test
    public void missingOneIngredient_recipeIsExcludedButFlaggedAlmostThere() {
        Recipe recipe = buildTomatoPasta();
        List<PantryItem> pantry = Arrays.asList(
                new PantryItem(1, "pasta", 500, "g", null),
                new PantryItem(2, "tomato", 5, "unit", null)
                // garlic is missing entirely
        );

        RecipeMatchResult result = IngredientMatcher.match(recipe, pantry);

        assertFalse("A recipe missing even one ingredient must NOT be a strict match",
                result.isFullyMatched());
        assertEquals(1, result.getMissingCount());
        assertTrue("Missing exactly one ingredient should qualify for Almost There",
                result.isAlmostThere());
    }

    @Test
    public void insufficientQuantity_recipeIsExcluded() {
        Recipe recipe = buildTomatoPasta();
        List<PantryItem> pantry = Arrays.asList(
                new PantryItem(1, "pasta", 500, "g", null),
                new PantryItem(2, "tomato", 1, "unit", null), // needs 3, only has 1
                new PantryItem(3, "garlic", 4, "unit", null)
        );

        RecipeMatchResult result = IngredientMatcher.match(recipe, pantry);

        assertFalse("A pantry item present in insufficient quantity must not count as a match",
                result.isFullyMatched());
    }

    @Test
    public void pluralVsSingularIngredientNames_stillMatch() {
        Recipe recipe = buildTomatoPasta();
        List<PantryItem> pantry = Arrays.asList(
                new PantryItem(1, "pasta", 500, "g", null),
                new PantryItem(2, "Tomatoes", 5, "unit", null), // plural + capitalised
                new PantryItem(3, "garlic", 4, "unit", null)
        );

        RecipeMatchResult result = IngredientMatcher.match(recipe, pantry);

        assertTrue("Naive stemming should treat 'tomato' and 'Tomatoes' as the same ingredient",
                result.isFullyMatched());
    }

    @Test
    public void compatibleUnitsOfDifferentScale_convertCorrectly() {
        Recipe recipe = new Recipe(2, "Flour Test", "n/a");
        recipe.addIngredient(new RecipeIngredient(1, 2, "flour", 500, "g"));

        List<PantryItem> pantry = new ArrayList<>();
        pantry.add(new PantryItem(1, "flour", 1, "kg", null)); // 1kg >= 500g

        RecipeMatchResult result = IngredientMatcher.match(recipe, pantry);

        assertTrue("1kg of flour should satisfy a recipe that needs 500g", result.isFullyMatched());
    }

    @Test
    public void emptyPantry_noRecipesMatch() {
        Recipe recipe = buildTomatoPasta();
        RecipeMatchResult result = IngredientMatcher.match(recipe, new ArrayList<PantryItem>());

        assertFalse(result.isFullyMatched());
        assertEquals(3, result.getMissingCount());
    }
}
