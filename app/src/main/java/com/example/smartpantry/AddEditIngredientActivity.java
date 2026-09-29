package com.example.smartpantry;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.smartpantry.db.DatabaseHelper;
import com.example.smartpantry.model.PantryItem;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

/**
 * Add/Edit Ingredient screen (Section 2.2 & 3.1). Reused for both creating
 * a new pantry item and editing an existing one, distinguished by whether
 * EXTRA_ITEM_ID was passed in the launching Intent. Performs input
 * validation (Section 3.1) before writing to the database.
 */
public class AddEditIngredientActivity extends AppCompatActivity {

    public static final String EXTRA_ITEM_ID = "extra_item_id";
    private static final long NO_ITEM_ID = -1L;

    private DatabaseHelper dbHelper;
    private long existingItemId = NO_ITEM_ID;

    private TextInputLayout layoutName;
    private TextInputLayout layoutQuantity;
    private TextInputLayout layoutUnit;
    private TextInputEditText inputName;
    private TextInputEditText inputQuantity;
    private TextInputEditText inputUnit;
    private TextInputEditText inputExpiry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_ingredient);

        dbHelper = DatabaseHelper.getInstance(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        layoutName = findViewById(R.id.layout_name);
        layoutQuantity = findViewById(R.id.layout_quantity);
        layoutUnit = findViewById(R.id.layout_unit);
        inputName = findViewById(R.id.input_name);
        inputQuantity = findViewById(R.id.input_quantity);
        inputUnit = findViewById(R.id.input_unit);
        inputExpiry = findViewById(R.id.input_expiry);

        Button saveButton = findViewById(R.id.btn_save);
        Button deleteButton = findViewById(R.id.btn_delete);

        existingItemId = getIntent().getLongExtra(EXTRA_ITEM_ID, NO_ITEM_ID);
        boolean isEditing = existingItemId != NO_ITEM_ID;

        if (isEditing) {
            setTitle(getString(R.string.title_edit_ingredient));
            deleteButton.setVisibility(android.view.View.VISIBLE);
            populateFieldsForEditing(existingItemId);
        } else {
            setTitle(getString(R.string.title_add_ingredient));
        }

        saveButton.setOnClickListener(v -> onSaveClicked());
        deleteButton.setOnClickListener(v -> {
            dbHelper.deletePantryItem(existingItemId);
            finish();
        });
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

    private void populateFieldsForEditing(long itemId) {
        PantryItem item = dbHelper.getPantryItem(itemId);
        if (item == null) return;
        inputName.setText(item.getName());
        inputQuantity.setText(formatQuantity(item.getQuantity()));
        inputUnit.setText(item.getUnit());
        if (item.hasExpiryDate()) {
            inputExpiry.setText(item.getExpiryDate());
        }
    }

    /**
     * Input validation (Section 3.1: "Input validation on any data entry
     * form"). Name and unit must be non-empty; quantity must be a parsable
     * number greater than zero. Errors are shown inline on the relevant field.
     */
    private void onSaveClicked() {
        layoutName.setError(null);
        layoutQuantity.setError(null);
        layoutUnit.setError(null);

        String name = safeText(inputName);
        String quantityText = safeText(inputQuantity);
        String unit = safeText(inputUnit);
        String expiry = safeText(inputExpiry);

        boolean valid = true;

        if (TextUtils.isEmpty(name)) {
            layoutName.setError(getString(R.string.error_name_required));
            valid = false;
        }

        double quantity = 0;
        try {
            quantity = Double.parseDouble(quantityText);
            if (quantity <= 0) {
                layoutQuantity.setError(getString(R.string.error_quantity_invalid));
                valid = false;
            }
        } catch (NumberFormatException e) {
            layoutQuantity.setError(getString(R.string.error_quantity_invalid));
            valid = false;
        }

        if (TextUtils.isEmpty(unit)) {
            layoutUnit.setError(getString(R.string.error_unit_required));
            valid = false;
        }

        if (!valid) {
            return;
        }

        PantryItem item = new PantryItem(existingItemId == NO_ITEM_ID ? 0 : existingItemId,
                name, quantity, unit, TextUtils.isEmpty(expiry) ? null : expiry);

        if (existingItemId == NO_ITEM_ID) {
            dbHelper.insertPantryItem(item);
        } else {
            dbHelper.updatePantryItem(item);
        }
        finish();
    }

    private String safeText(TextInputEditText field) {
        return field.getText() == null ? "" : field.getText().toString().trim();
    }

    private String formatQuantity(double value) {
        if (value == Math.floor(value)) {
            return String.valueOf((long) value);
        }
        return String.valueOf(value);
    }
}
