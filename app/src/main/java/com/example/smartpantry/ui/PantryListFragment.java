package com.example.smartpantry.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartpantry.AddEditIngredientActivity;
import com.example.smartpantry.R;
import com.example.smartpantry.adapter.PantryAdapter;
import com.example.smartpantry.db.DatabaseHelper;
import com.example.smartpantry.model.PantryItem;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

/**
 * Pantry List screen (Section 2.2): shows all current ingredients bound
 * from the database via a RecyclerView/Adapter, lets the user tap an item
 * to edit it, delete it directly, or add a new one via the FAB.
 */
public class PantryListFragment extends Fragment implements PantryAdapter.OnPantryItemClickListener {

    private DatabaseHelper dbHelper;
    private PantryAdapter adapter;
    private RecyclerView recyclerView;
    private View emptyStateView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                              @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pantry_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dbHelper = DatabaseHelper.getInstance(requireContext());

        recyclerView = view.findViewById(R.id.recycler_pantry);
        emptyStateView = view.findViewById(R.id.text_empty_pantry);
        FloatingActionButton fab = view.findViewById(R.id.fab_add_ingredient);

        adapter = new PantryAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);

        fab.setOnClickListener(v -> startActivity(
                new Intent(requireContext(), AddEditIngredientActivity.class)));
    }

    @Override
    public void onResume() {
        super.onResume();
        // Reload every time the tab becomes visible so edits/deletes/adds
        // made in AddEditIngredientActivity are always reflected.
        refreshPantry();
    }

    private void refreshPantry() {
        List<PantryItem> items = dbHelper.getAllPantryItems();
        adapter.setItems(items);
        boolean isEmpty = items.isEmpty();
        recyclerView.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
        emptyStateView.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onItemClick(PantryItem item) {
        Intent intent = new Intent(requireContext(), AddEditIngredientActivity.class);
        intent.putExtra(AddEditIngredientActivity.EXTRA_ITEM_ID, item.getId());
        startActivity(intent);
    }

    @Override
    public void onDeleteClick(PantryItem item) {
        dbHelper.deletePantryItem(item.getId());
        refreshPantry();
    }
}
