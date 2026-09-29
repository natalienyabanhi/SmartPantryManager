package com.example.smartpantry.db;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;


class RecipeSeeder {

    private static class SeedIngredient {
        final String name;
        final double quantity;
        final String unit;

        SeedIngredient(String name, double quantity, String unit) {
            this.name = name;
            this.quantity = quantity;
            this.unit = unit;
        }
    }

    static void seed(SQLiteDatabase db) {
        addRecipe(db, "Tomato Pasta",
                "Boil pasta until al dente. In a pan, saute garlic in olive oil, "
                        + "add chopped tomatoes and simmer for 10 minutes. Season with salt "
                        + "and pepper, toss with the drained pasta and serve.",
                new SeedIngredient("pasta", 200, "g"),
                new SeedIngredient("tomato", 3, "unit"),
                new SeedIngredient("garlic", 2, "unit"),
                new SeedIngredient("olive oil", 2, "tbsp"),
                new SeedIngredient("salt", 1, "tsp"));

        addRecipe(db, "Vegetable Stir Fry",
                "Heat oil in a wok. Add chopped vegetables and stir fry on high "
                        + "heat for 5-7 minutes. Add soy sauce and garlic, toss well and serve hot.",
                new SeedIngredient("carrot", 2, "unit"),
                new SeedIngredient("broccoli", 1, "unit"),
                new SeedIngredient("soy sauce", 2, "tbsp"),
                new SeedIngredient("garlic", 1, "unit"),
                new SeedIngredient("oil", 1, "tbsp"));

        addRecipe(db, "Cheese Omelette",
                "Whisk eggs with a splash of milk and salt. Pour into a hot, "
                        + "buttered pan, sprinkle cheese over one half, fold and cook until set.",
                new SeedIngredient("egg", 3, "unit"),
                new SeedIngredient("cheese", 50, "g"),
                new SeedIngredient("milk", 30, "ml"),
                new SeedIngredient("butter", 10, "g"));

        addRecipe(db, "Chicken Rice Bowl",
                "Cook rice according to packet instructions. Pan-fry diced "
                        + "chicken until golden, season, and serve over the rice with a "
                        + "sprinkle of spring onion.",
                new SeedIngredient("rice", 200, "g"),
                new SeedIngredient("chicken breast", 300, "g"),
                new SeedIngredient("spring onion", 1, "unit"),
                new SeedIngredient("oil", 1, "tbsp"));

        addRecipe(db, "Simple Green Salad",
                "Wash and tear the lettuce, slice the cucumber and tomato, "
                        + "and toss together with olive oil and a squeeze of lemon.",
                new SeedIngredient("lettuce", 1, "unit"),
                new SeedIngredient("cucumber", 1, "unit"),
                new SeedIngredient("tomato", 2, "unit"),
                new SeedIngredient("olive oil", 1, "tbsp"));

        addRecipe(db, "Bean Quesadilla",
                "Spread beans and cheese over a tortilla, top with another "
                        + "tortilla and pan-fry both sides until golden and the cheese melts.",
                new SeedIngredient("tortilla", 2, "unit"),
                new SeedIngredient("beans", 150, "g"),
                new SeedIngredient("cheese", 60, "g"));

        addRecipe(db, "Mushroom Risotto",
                "Saute mushrooms and set aside. Toast rice in butter, add "
                        + "stock gradually while stirring until creamy, then fold in the "
                        + "mushrooms and parmesan.",
                new SeedIngredient("rice", 200, "g"),
                new SeedIngredient("mushroom", 200, "g"),
                new SeedIngredient("butter", 20, "g"),
                new SeedIngredient("stock", 750, "ml"),
                new SeedIngredient("parmesan", 40, "g"));

        addRecipe(db, "Banana Pancakes",
                "Mash the banana and mix with flour, egg and milk into a "
                        + "batter. Fry spoonfuls in a buttered pan until golden on both sides.",
                new SeedIngredient("banana", 2, "unit"),
                new SeedIngredient("flour", 150, "g"),
                new SeedIngredient("egg", 1, "unit"),
                new SeedIngredient("milk", 100, "ml"));

        addRecipe(db, "Lentil Soup",
                "Saute onion and carrot, add lentils and stock, simmer for "
                        + "25 minutes until soft, then season and blend lightly if desired.",
                new SeedIngredient("lentils", 200, "g"),
                new SeedIngredient("onion", 1, "unit"),
                new SeedIngredient("carrot", 1, "unit"),
                new SeedIngredient("stock", 1, "l"));

        addRecipe(db, "Tuna Sandwich",
                "Mix tuna with mayonnaise, spread on bread, add lettuce and "
                        + "close the sandwich.",
                new SeedIngredient("bread", 2, "unit"),
                new SeedIngredient("tuna", 1, "unit"),
                new SeedIngredient("mayonnaise", 2, "tbsp"),
                new SeedIngredient("lettuce", 1, "unit"));

        addRecipe(db, "Garlic Butter Potatoes",
                "Boil potatoes until tender, then toss in a pan with melted "
                        + "butter, crushed garlic and chopped parsley.",
                new SeedIngredient("potato", 500, "g"),
                new SeedIngredient("butter", 30, "g"),
                new SeedIngredient("garlic", 2, "unit"));

        addRecipe(db, "Fruit Yoghurt Bowl",
                "Spoon yoghurt into a bowl, top with sliced banana and "
                        + "berries, and drizzle with honey.",
                new SeedIngredient("yoghurt", 200, "g"),
                new SeedIngredient("banana", 1, "unit"),
                new SeedIngredient("berries", 100, "g"),
                new SeedIngredient("honey", 1, "tbsp"));

        addRecipe(db, "Egg Fried Rice",
                "Scramble the eggs and set aside. Stir-fry cold rice with "
                        + "soy sauce and spring onion, then fold the eggs back through.",
                new SeedIngredient("rice", 250, "g"),
                new SeedIngredient("egg", 2, "unit"),
                new SeedIngredient("soy sauce", 1, "tbsp"),
                new SeedIngredient("spring onion", 1, "unit"));

        addRecipe(db, "Margherita Toast",
                "Toast the bread, top with sliced tomato and cheese, and "
                        + "grill until the cheese melts. Finish with fresh basil.",
                new SeedIngredient("bread", 2, "unit"),
                new SeedIngredient("tomato", 1, "unit"),
                new SeedIngredient("cheese", 40, "g"),
                new SeedIngredient("basil", 3, "unit"));

        addRecipe(db, "Beef Tacos",
                "Brown the beef mince with onion and spices, spoon into "
                        + "tortillas and top with cheese and lettuce.",
                new SeedIngredient("beef mince", 300, "g"),
                new SeedIngredient("onion", 1, "unit"),
                new SeedIngredient("tortilla", 4, "unit"),
                new SeedIngredient("cheese", 50, "g"),
                new SeedIngredient("lettuce", 1, "unit"));

        addRecipe(db, "Pumpkin Soup",
                "Saute onion, add cubed pumpkin and stock, simmer until "
                        + "soft, then blend until smooth and season to taste.",
                new SeedIngredient("pumpkin", 500, "g"),
                new SeedIngredient("onion", 1, "unit"),
                new SeedIngredient("stock", 750, "ml"));

        addRecipe(db, "Peanut Butter Toast",
                "Toast the bread and spread generously with peanut butter, "
                        + "topped with sliced banana.",
                new SeedIngredient("bread", 2, "unit"),
                new SeedIngredient("peanut butter", 2, "tbsp"),
                new SeedIngredient("banana", 1, "unit"));

        addRecipe(db, "Chickpea Curry",
                "Saute onion and garlic, add curry powder, chickpeas and "
                        + "chopped tomato, simmer for 15 minutes and serve with rice.",
                new SeedIngredient("chickpeas", 400, "g"),
                new SeedIngredient("onion", 1, "unit"),
                new SeedIngredient("garlic", 2, "unit"),
                new SeedIngredient("tomato", 2, "unit"),
                new SeedIngredient("rice", 200, "g"));
    }

    private static void addRecipe(SQLiteDatabase db, String name, String instructions,
                                   SeedIngredient... ingredients) {
        ContentValues recipeValues = new ContentValues();
        recipeValues.put(DatabaseHelper.COL_RECIPE_NAME, name);
        recipeValues.put(DatabaseHelper.COL_RECIPE_INSTRUCTIONS, instructions);
        long recipeId = db.insert(DatabaseHelper.TABLE_RECIPES, null, recipeValues);

        for (SeedIngredient ingredient : ingredients) {
            ContentValues ingredientValues = new ContentValues();
            ingredientValues.put(DatabaseHelper.COL_RI_RECIPE_ID, recipeId);
            ingredientValues.put(DatabaseHelper.COL_RI_NAME, ingredient.name);
            ingredientValues.put(DatabaseHelper.COL_RI_QUANTITY, ingredient.quantity);
            ingredientValues.put(DatabaseHelper.COL_RI_UNIT, ingredient.unit);
            db.insert(DatabaseHelper.TABLE_RECIPE_INGREDIENTS, null, ingredientValues);
        }
    }
}
