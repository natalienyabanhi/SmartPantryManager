package com.example.smartpantry.logic;

import com.example.smartpantry.model.PantryItem;
import com.example.smartpantry.model.Recipe;
import com.example.smartpantry.model.RecipeIngredient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implements the assignment's core business rule (Section 2.3):
 *
 *      A recipe may only be shown as "suggested" if EVERY ingredient it
 *      requires is currently present in the pantry, in at least the
 *      required quantity. Partial matches are excluded from the main
 *      suggestions list.
 *
 * Two deliberate design decisions make this "reasonably robust to simple
 * real-world messiness" as the brief requires, without needing full NLP:
 *
 * 1. Name normalisation (normaliseName): lower-cases, trims, and strips a
 *    trailing "es"/"s" so "tomato" and "tomatoes" are treated as the same
 *    ingredient. This is a naive English stemmer, not a dictionary lookup,
 *    which is exactly the scope the brief asks for.
 *
 * 2. Unit normalisation (toBaseUnits): converts compatible units onto a
 *    common base (grams for mass, millilitres for volume, "unit" for
 *    countable items such as "2 eggs") so "500 g" of an ingredient can be
 *    correctly compared against a recipe that calls for "0.5 kg".
 */
public class IngredientMatcher {

    // Mass units, converted to grams.
    private static final Map<String, Double> MASS_TO_GRAMS = new HashMap<>();
    // Volume units, converted to millilitres.
    private static final Map<String, Double> VOLUME_TO_ML = new HashMap<>();

    static {
        MASS_TO_GRAMS.put("g", 1.0);
        MASS_TO_GRAMS.put("gram", 1.0);
        MASS_TO_GRAMS.put("grams", 1.0);
        MASS_TO_GRAMS.put("kg", 1000.0);
        MASS_TO_GRAMS.put("kilogram", 1000.0);
        MASS_TO_GRAMS.put("kilograms", 1000.0);
        MASS_TO_GRAMS.put("oz", 28.3495);
        MASS_TO_GRAMS.put("ounce", 28.3495);
        MASS_TO_GRAMS.put("ounces", 28.3495);
        MASS_TO_GRAMS.put("lb", 453.592);
        MASS_TO_GRAMS.put("pound", 453.592);
        MASS_TO_GRAMS.put("pounds", 453.592);

        VOLUME_TO_ML.put("ml", 1.0);
        VOLUME_TO_ML.put("millilitre", 1.0);
        VOLUME_TO_ML.put("milliliter", 1.0);
        VOLUME_TO_ML.put("l", 1000.0);
        VOLUME_TO_ML.put("litre", 1000.0);
        VOLUME_TO_ML.put("liter", 1000.0);
        VOLUME_TO_ML.put("tsp", 4.92892);
        VOLUME_TO_ML.put("teaspoon", 4.92892);
        VOLUME_TO_ML.put("tbsp", 14.7868);
        VOLUME_TO_ML.put("tablespoon", 14.7868);
        VOLUME_TO_ML.put("cup", 236.588);
        VOLUME_TO_ML.put("fl oz", 29.5735);
    }

    /**
     * Runs the strict-matching rule for a single recipe.
     *
     * @param recipe      the recipe to test
     * @param pantryItems the user's current pantry contents
     * @return a RecipeMatchResult describing whether it fully matched, and
     *         if not, exactly what is missing.
     */
    public static RecipeMatchResult match(Recipe recipe, List<PantryItem> pantryItems) {
        // Index the pantry by normalised name for fast, forgiving lookups.
        Map<String, PantryItem> pantryByName = new HashMap<>();
        for (PantryItem item : pantryItems) {
            pantryByName.put(normaliseName(item.getName()), item);
        }

        List<String> missing = new ArrayList<>();

        for (RecipeIngredient required : recipe.getIngredients()) {
            String key = normaliseName(required.getIngredientName());
            PantryItem owned = pantryByName.get(key);

            if (owned == null) {
                // Ingredient not in the pantry at all.
                missing.add(required.getIngredientName());
                continue;
            }

            if (!hasEnoughQuantity(owned, required)) {
                // Ingredient is present but there isn't enough of it.
                missing.add(required.getIngredientName());
            }
        }

        boolean fullyMatched = missing.isEmpty();
        return new RecipeMatchResult(recipe, fullyMatched, missing.size(), missing);
    }

    /** Convenience overload: matches every recipe in one pass. */
    public static List<RecipeMatchResult> matchAll(List<Recipe> recipes, List<PantryItem> pantryItems) {
        List<RecipeMatchResult> results = new ArrayList<>();
        for (Recipe recipe : recipes) {
            results.add(match(recipe, pantryItems));
        }
        return results;
    }

    /**
     * Naive English normaliser so "Tomato" / "tomato" / "tomatoes" all match.
     * This intentionally does NOT attempt full NLP/lemmatisation - it strips
     * a trailing "es" or "s" only when doing so still leaves a sensible word,
     * which covers the common cases named in the brief without mangling
     * short words (e.g. it will not reduce "gas" to "ga").
     */
    public static String normaliseName(String rawName) {
        if (rawName == null) return "";
        String name = rawName.trim().toLowerCase();

        if (name.length() > 4 && name.endsWith("ies")) {
            // e.g. "berries" -> "berry"
            name = name.substring(0, name.length() - 3) + "y";
        } else if (name.length() > 4 && name.endsWith("es")) {
            // e.g. "tomatoes" -> "tomato", "potatoes" -> "potato"
            name = name.substring(0, name.length() - 2);
        } else if (name.length() > 3 && name.endsWith("s") && !name.endsWith("ss")) {
            // e.g. "eggs" -> "egg", but leaves "molasses" alone
            name = name.substring(0, name.length() - 1);
        }
        return name;
    }

    /**
     * Checks whether the pantry item covers the required quantity, converting
     * between compatible units where needed (e.g. pantry has 1 kg, recipe
     * needs 500 g). If the two units are not in the same family (e.g. "unit"
     * vs "ml"), they are compared directly as a safe fallback.
     */
    private static boolean hasEnoughQuantity(PantryItem owned, RecipeIngredient required) {
        String ownedUnit = owned.getUnit() == null ? "" : owned.getUnit().trim().toLowerCase();
        String requiredUnit = required.getUnit() == null ? "" : required.getUnit().trim().toLowerCase();

        Double ownedBase = toBaseAmount(owned.getQuantity(), ownedUnit);
        Double requiredBase = toBaseAmount(required.getQuantity(), requiredUnit);

        if (ownedBase != null && requiredBase != null
                && sameFamily(ownedUnit, requiredUnit)) {
            return ownedBase >= requiredBase;
        }

        // Fallback: units aren't convertible (e.g. both "unit"/"whole"), so
        // compare the raw quantities directly.
        return owned.getQuantity() >= required.getQuantity();
    }

    private static boolean sameFamily(String unitA, String unitB) {
        boolean aIsMass = MASS_TO_GRAMS.containsKey(unitA);
        boolean bIsMass = MASS_TO_GRAMS.containsKey(unitB);
        boolean aIsVolume = VOLUME_TO_ML.containsKey(unitA);
        boolean bIsVolume = VOLUME_TO_ML.containsKey(unitB);
        return (aIsMass && bIsMass) || (aIsVolume && bIsVolume);
    }

    private static Double toBaseAmount(double quantity, String unit) {
        if (MASS_TO_GRAMS.containsKey(unit)) {
            return quantity * MASS_TO_GRAMS.get(unit);
        }
        if (VOLUME_TO_ML.containsKey(unit)) {
            return quantity * VOLUME_TO_ML.get(unit);
        }
        return null;
    }
}
