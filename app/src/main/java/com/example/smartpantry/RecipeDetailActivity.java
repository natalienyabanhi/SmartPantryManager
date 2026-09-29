package com.example.smartpantry;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.smartpantry.db.DatabaseHelper;
import com.example.smartpantry.logic.IngredientMatcher;
import com.example.smartpantry.logic.RecipeMatchResult;
import com.example.smartpantry.model.PantryItem;
import com.example.smartpantry.model.Recipe;
import com.example.smartpantry.model.RecipeIngredient;

import java.util.List;


public class RecipeDetailActivity extends AppCompatActivity {

    public static final String EXTRA_RECIPE_ID = "extra_recipe_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        long recipeId = getIntent().getLongExtra(EXTRA_RECIPE_ID, -1);
        DatabaseHelper dbHelper = DatabaseHelper.getInstance(this);
        Recipe recipe = dbHelper.getRecipe(recipeId);

        if (recipe == null) {
            finish();
            return;
        }

        List<PantryItem> pantry = dbHelper.getAllPantryItems();
        RecipeMatchResult result = IngredientMatcher.match(recipe, pantry);

        setTitle(recipe.getName());

        TextView nameView = findViewById(R.id.text_detail_name);
        TextView statusView = findViewById(R.id.text_detail_status);
        TextView ingredientsView = findViewById(R.id.text_detail_ingredients);
        TextView methodView = findViewById(R.id.text_detail_method);

        nameView.setText(recipe.getName());

        if (result.isFullyMatched()) {
            statusView.setText("✓ You have everything you need for this recipe");
        } else {
            statusView.setText("Missing: " + String.join(", ", result.getMissingIngredientNames()));
        }

        StringBuilder ingredientsText = new StringBuilder();
        for (RecipeIngredient ingredient : recipe.getIngredients()) {
            ingredientsText.append("• ")
                    .append(formatQuantity(ingredient.getQuantity()))
                    .append(" ")
                    .append(ingredient.getUnit())
                    .append(" ")
                    .append(ingredient.getIngredientName())
                    .append("\n");
        }
        ingredientsView.setText(ingredientsText.toString().trim());
        methodView.setText(recipe.getInstructions());
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private String formatQuantity(double value) {
        if (value == Math.floor(value)) {
            return String.valueOf((long) value);
        }
        return String.valueOf(value);
    }
}
