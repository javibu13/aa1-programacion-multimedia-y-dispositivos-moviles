package com.sanvalero.android.view;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sanvalero.android.MainActivity;
import com.sanvalero.android.R;
import com.sanvalero.android.adapter.PlacesAdapter;
import com.sanvalero.android.callback.PlacesCallback;
import com.sanvalero.android.database.AppDatabase;
import com.sanvalero.android.database.PersonFav;
import com.sanvalero.android.database.PlaceFav;
import com.sanvalero.android.model.Person;
import com.sanvalero.android.model.Place;
import com.sanvalero.android.presenter.PlacesPresenter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FavouritePlacesActivity extends BaseActivity implements PlacesCallback {
    private RecyclerView recyclerView;
    private PlacesAdapter adapter;
    private PlacesPresenter placesPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().setTitle(R.string.favourite_places_activity);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.places_activity);

        recyclerView = findViewById(R.id.placesRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        placesPresenter = new PlacesPresenter(this);
        placesPresenter.fetchPlaces();

        FloatingActionButton addPlaceFloatingActionButton = findViewById(R.id.placesAddPlaceFloatingActionButton);
        addPlaceFloatingActionButton.setVisibility(View.GONE);
        FloatingActionButton favouritePlacesFloatingActionButton = findViewById(R.id.favouritePlacesFloatingActionButton);
        favouritePlacesFloatingActionButton.setVisibility(View.GONE);

        createDeleteAllButton();
    }

    @Override
    protected void onResume() {
        super.onResume();
        placesPresenter.fetchPlaces();
    }

    @Override
    public void onPlacesLoaded(List<Place> places) {
        if (places.isEmpty()) {
            Log.w("PlacesActivity", "Callback went good");
            new AlertDialog.Builder(this)
                .setTitle("No Results")
                .setMessage("No places were found from the API.")
                .setPositiveButton("OK", null)
                .show();
        }
        List<Place> favPlaces = filterPlaceFav(places);
        adapter = new PlacesAdapter(this, favPlaces);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onPlacesLoadError(String errorMessage) {
        Log.e("PlacesActivity", "Error: " + errorMessage);
        new AlertDialog.Builder(this)
            .setTitle("Failure")
            .setMessage("Error occurred during data retrieve from API.")
            .setNeutralButton("Close", (dialog, which) -> {
                Intent intent = new Intent(FavouritePlacesActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            })
            .show();
    }

    @Override
    public void onPlaceClicked(Long idPlaceClicked) {
        Intent intent = new Intent(FavouritePlacesActivity.this, PlaceActivity.class);
        intent.putExtra("place_id", idPlaceClicked);
        startActivity(intent);
    }

    private List<Place> filterPlaceFav(List<Place> places) {
        AppDatabase db = Room.databaseBuilder(this, AppDatabase.class, "PlaceFav")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();

        List<PlaceFav> favPlaces = db.placeFavDao().getAll();

        Set<Long> favIds = new HashSet<>();
        for (PlaceFav fav : favPlaces) {
            if (fav.isFavourite()) {
                favIds.add(fav.getId());
            }
        }

        List<Place> filtered = new ArrayList<>();
        for (Place p : places) {
            if (favIds.contains(p.getId())) {
                filtered.add(p);
            }
        }

        return filtered;
    }

    private void createDeleteAllButton() {
        FloatingActionButton clearFavoritesFab = new FloatingActionButton(this);
        clearFavoritesFab.setId(View.generateViewId());
        clearFavoritesFab.setImageResource(android.R.drawable.ic_menu_delete);
        clearFavoritesFab.setContentDescription("Clear all favorites");
        clearFavoritesFab.setBackgroundTintList(ColorStateList.valueOf(Color.RED));

        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
        );
        params.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID;
        params.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID;
        params.setMargins(16, 16, 16, 16);

        ConstraintLayout layout = findViewById(R.id.placesRecyclerView).getParent() instanceof ConstraintLayout
                ? (ConstraintLayout) findViewById(R.id.placesRecyclerView).getParent()
                : null;

        if (layout != null) {
            layout.addView(clearFavoritesFab, params);
        }

        clearFavoritesFab.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.confirmation_title_dialog)
                    .setMessage(R.string.confirm_delete_favourite_places_text_dialog)
                    .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                        deleteFavouritePlaces();
                    })
                    .setNegativeButton(android.R.string.cancel, null)
                    .show();
        });
    }

    private void deleteFavouritePlaces() {
        AppDatabase db = Room.databaseBuilder(this, AppDatabase.class, "PlaceFav")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();

        db.placeFavDao().deleteAll();
        Toast.makeText(this, getString(R.string.empty_favourite_places), Toast.LENGTH_SHORT).show();
        finish();
    }
}
