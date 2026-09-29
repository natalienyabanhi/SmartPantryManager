package com.example.smartpantry;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.smartpantry.ui.PantryListFragment;
import com.example.smartpantry.ui.SettingsFragment;
import com.example.smartpantry.ui.SuggestedRecipesFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

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

        // Open the Pantry screen when the app starts
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