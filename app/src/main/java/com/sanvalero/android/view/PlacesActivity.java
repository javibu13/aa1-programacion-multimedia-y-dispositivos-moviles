package com.sanvalero.android.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sanvalero.android.MainActivity;
import com.sanvalero.android.R;
import com.sanvalero.android.adapter.PlacesAdapter;
import com.sanvalero.android.callback.PlacesCallback;
import com.sanvalero.android.model.Place;
import com.sanvalero.android.presenter.PlacesPresenter;

import java.util.List;

public class PlacesActivity extends BaseActivity implements PlacesCallback {
    private RecyclerView recyclerView;
    private PlacesAdapter adapter;
    private PlacesPresenter placesPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().setTitle(R.string.places_activity);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.places_activity);

        recyclerView = findViewById(R.id.placesRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        placesPresenter = new PlacesPresenter(this);
        placesPresenter.fetchPlaces();

        FloatingActionButton addPlaceFloatingActionButton = findViewById(R.id.placesAddPlaceFloatingActionButton);
        addPlaceFloatingActionButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, NewPlaceActivity.class);
            startActivity(intent);
        });
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
        adapter = new PlacesAdapter(this, places);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onPlacesLoadError(String errorMessage) {
        Log.e("PlacesActivity", "Error: " + errorMessage);
        new AlertDialog.Builder(this)
            .setTitle("Failure")
            .setMessage("Error occurred during data retrieve from API.")
            .setNeutralButton("Close", (dialog, which) -> {
                Intent intent = new Intent(PlacesActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            })
            .show();
    }

    @Override
    public void onPlaceClicked(Long idPlaceClicked) {
        Intent intent = new Intent(PlacesActivity.this, PlaceActivity.class);
        intent.putExtra("place_id", idPlaceClicked);
        startActivity(intent);
    }


}
