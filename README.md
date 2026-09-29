# Smart Pantry Manager

A Java Android application that suggests recipes based **strictly** on the
ingredients the user actually has at home, so no shopping trip is ever
required for a suggested recipe.

## What it does

- Track your pantry: add, edit, and delete ingredients (name, quantity,
  unit, optional expiry date).
- See a live "Suggested Recipes" list generated from a **strict-matching
  rule**: a recipe is only suggested if the pantry contains every single
  ingredient it needs, in at least the required quantity.
- An optional "Almost There" section highlights recipes missing exactly
  one ingredient, kept clearly separate from the strict suggestions.
- View full ingredient lists and preparation steps for any recipe.
- Adjust settings for expiry alerts and preferred unit system.
- All data persists locally between sessions.

## Why SQLite (SQLiteOpenHelper)

This project uses **SQLite via `SQLiteOpenHelper`** (Section 3.2, option 1)
rather than Firebase or PostgreSQL, for three reasons:

1. **Offline-first fit**: a pantry tracker is a genuinely personal,
   single-user tool. There's no need for cloud sync or multi-device
   access, so a local database avoids unnecessary network dependency and
   keeps the app fully usable with no internet connection.
2. **Full control over the matching query**: the strict-matching rule
   needs to compare pantry rows against recipe ingredient rows directly.
   SQLite's relational model (three related tables:
   `pantry_items`, `recipes`, `recipe_ingredients`) maps cleanly onto that
   comparison without needing a backend API layer.
3. **Consistency with the module content**: `SQLiteOpenHelper` and
   `Cursor`-based CRUD were covered directly in the module's persistent
   data chapter, so this choice lets the implementation demonstrate that
   material specifically, rather than a Firebase SDK the module didn't
   teach in depth.

## Data model

- `pantry_items` — id, name, quantity, unit, expiry_date (nullable)
- `recipes` — id, name, instructions
- `recipe_ingredients` — id, recipe_id (FK → recipes), ingredient_name,
  quantity, unit

18 recipes are seeded into `recipes`/`recipe_ingredients` automatically the
first time the app runs (see `RecipeSeeder.java`).

## Core logic: `IngredientMatcher`

`com.example.smartpantry.logic.IngredientMatcher` is the single most
important class in this project. For each recipe it:

1. Normalises ingredient names (naive stemming, e.g. "tomatoes" → "tomato")
   so the match isn't broken by simple singular/plural differences.
2. Converts compatible units onto a common base (grams for mass,
   millilitres for volume) so "1 kg" in the pantry correctly satisfies a
   recipe that calls for "500 g".
3. Only marks a recipe as fully matched if **every** required ingredient
   is present in at least the required quantity — a single missing or
   insufficient ingredient excludes the recipe from the main suggestions.
4. Also reports recipes missing exactly one ingredient, powering the
   bonus "Almost There" list.

Unit tests for this logic are in
`app/src/test/java/com/example/smartpantry/logic/IngredientMatcherTest.java`.

## Project structure

```
app/src/main/java/com/example/smartpantry/
├── MainActivity.java                 // hosts the 3 bottom-nav tabs
├── AddEditIngredientActivity.java    // create/edit/delete a pantry item
├── RecipeDetailActivity.java         // full recipe view + match status
├── ui/
│   ├── PantryListFragment.java
│   ├── SuggestedRecipesFragment.java
│   └── SettingsFragment.java
├── adapter/
│   ├── PantryAdapter.java
│   ├── RecipeAdapter.java
│   └── RecipeListRow.java
├── db/
│   ├── DatabaseHelper.java
│   └── RecipeSeeder.java
├── model/
│   ├── PantryItem.java
│   ├── Recipe.java
│   └── RecipeIngredient.java
└── logic/
    ├── IngredientMatcher.java
    └── RecipeMatchResult.java
```

## Setup / run instructions

1. Clone this repository.
2. Open the project root folder in **Android Studio** (Hedgehog or newer
   recommended).
3. Let Gradle sync (it will download the AndroidX/Material dependencies
   listed in `app/build.gradle`).
4. Run on an emulator or physical device with **API 21+**.
5. On first launch the app automatically creates the local database and
   seeds the recipe catalogue — no setup steps or accounts needed.

## Out of scope (by design, per the assignment brief)

- No Google Maps, mapping SDK, or device location/GPS features.
- No payment processing.
- Not published to the Google Play Store.

## Author

Natalie — Mobile App Development 700, Richfield Graduate Institute of
Technology.
