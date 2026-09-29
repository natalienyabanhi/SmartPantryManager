package com.example.smartpantry.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import com.example.smartpantry.R;

/**
 * Settings screen (Section 2.2: "A settings or profile screen (e.g. toggle
 * for expiring-soon alerts, or units preference)"). Preferences are
 * persisted with SharedPreferences so they survive app restarts, same as
 * the rest of the app's data.
 */
public class SettingsFragment extends Fragment {

    private static final String PREFS_NAME = "smart_pantry_prefs";
    private static final String KEY_EXPIRY_ALERTS = "expiry_alerts_enabled";
    private static final String KEY_UNIT_SYSTEM = "unit_system";
    private static final String UNIT_METRIC = "metric";
    private static final String UNIT_IMPERIAL = "imperial";

    private SharedPreferences prefs;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                              @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        prefs = requireContext().getSharedPreferences(PREFS_NAME, 0);

        SwitchCompat expiryAlertsSwitch = view.findViewById(R.id.switch_expiry_alerts);
        RadioGroup unitGroup = view.findViewById(R.id.radio_group_units);

        expiryAlertsSwitch.setChecked(prefs.getBoolean(KEY_EXPIRY_ALERTS, true));
        boolean isImperial = UNIT_IMPERIAL.equals(prefs.getString(KEY_UNIT_SYSTEM, UNIT_METRIC));
        unitGroup.check(isImperial ? R.id.radio_imperial : R.id.radio_metric);

        expiryAlertsSwitch.setOnCheckedChangeListener((buttonView, isChecked) ->
                prefs.edit().putBoolean(KEY_EXPIRY_ALERTS, isChecked).apply());

        unitGroup.setOnCheckedChangeListener((group, checkedId) -> {
            String unitSystem = checkedId == R.id.radio_imperial ? UNIT_IMPERIAL : UNIT_METRIC;
            prefs.edit().putString(KEY_UNIT_SYSTEM, unitSystem).apply();
        });
    }
}
