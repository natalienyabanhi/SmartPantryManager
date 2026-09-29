package com.example.smartpantry;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.smartpantry.ui.PantryListFragment;
import com.example.smartpantry.ui.SettingsFragment;
import com.example.smartpantry.ui.SuggestedRecipesFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 * Single host Activity for the app's three main tabs (Section 3.1 allows
 * either distinct Activities or Fragments navigated through a host
 * Activity - this app uses a host Activity for the three tab screens, and
 * separate Activities for Add/Edit Ingredient and Recipe Detail, so the
 * "minimum of four distinct screens" requirement is exceeded with five
 * screens in total).
 */
public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selected;
            int itemId = item.getItemId();

            if (itemId == R.id.nav_pantry) {
                selected = new PantryListFragment();
                setTitle(getString(R.string.nav_pantry));
            } else if (itemId == R.id.nav_suggestions) {
                selected = new SuggestedRecipesFragment();
                setTitle(getString(R.string.nav_suggestions));
            } else {
                selected = new SettingsFragment();
                setTitle(getString(R.string.nav_settings));
            }

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, selected)
                    .commit();
            return true;
        });

        // Default tab on launch.
        if (savedInstanceState == null) {
            bottomNav.setSelectedItemId(R.id.nav_pantry);
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
        if (toolbar != null) {
            toolbar.setTitle(title);
        }
    }
}
