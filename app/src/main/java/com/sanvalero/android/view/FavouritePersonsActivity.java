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
import com.sanvalero.android.adapter.PersonsAdapter;
import com.sanvalero.android.callback.PersonsCallback;
import com.sanvalero.android.database.AppDatabase;
import com.sanvalero.android.database.PersonFav;
import com.sanvalero.android.model.Person;
import com.sanvalero.android.presenter.PersonsPresenter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FavouritePersonsActivity extends BaseActivity implements PersonsCallback {
    private RecyclerView recyclerView;
    private PersonsAdapter adapter;
    private PersonsPresenter personsPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().setTitle(R.string.favourite_persons_activity);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.persons_activity);

        recyclerView = findViewById(R.id.personsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        personsPresenter = new PersonsPresenter(this);
        personsPresenter.fetchPersons();

        FloatingActionButton addPersonFloatingActionButton = findViewById(R.id.personsAddPersonFloatingActionButton);
        addPersonFloatingActionButton.setVisibility(View.GONE);
        FloatingActionButton favouritePersonsFloatingActionButton = findViewById(R.id.favouritePersonsFloatingActionButton);
        favouritePersonsFloatingActionButton.setVisibility(View.GONE);

        createDeleteAllButton();
    }

    @Override
    protected void onResume() {
        super.onResume();
        personsPresenter.fetchPersons();
    }

    @Override
    public void onPersonsLoaded(List<Person> persons) {
        if (persons.isEmpty()) {
            Log.w("PersonsActivity", "Callback ok");
            new AlertDialog.Builder(this)
                .setTitle("No Results")
                .setMessage("No persons were found from the API.")
                .setPositiveButton("OK", null)
                .show();
        }
        List<Person> favPersons = filterPersonFav(persons);
        adapter = new PersonsAdapter(this, favPersons);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onPersonsLoadError(String errorMessage) {
        Log.e("PersonsActivity", "Error: " + errorMessage);
        new AlertDialog.Builder(this)
            .setTitle("Failure")
            .setMessage("Error occurred during data retrieve from API.")
            .setNeutralButton("Close", (dialog, which) -> {
                Intent intent = new Intent(FavouritePersonsActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            })
            .show();
    }

    @Override
    public void onPersonClicked(Long idPersonClicked) {
        Intent intent = new Intent(FavouritePersonsActivity.this, PersonActivity.class);
        intent.putExtra("person_id", idPersonClicked);
        startActivity(intent);
    }

    private List<Person> filterPersonFav(List<Person> persons) {
        AppDatabase db = Room.databaseBuilder(this, AppDatabase.class, "PersonFav")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();

        List<PersonFav> favPersons = db.personFavDao().getAll();

        Set<Long> favIds = new HashSet<>();
        for (PersonFav fav : favPersons) {
            if (fav.isFavourite()) {
                favIds.add(fav.getId());
            }
        }

        List<Person> filtered = new ArrayList<>();
        for (Person p : persons) {
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

        ConstraintLayout layout = findViewById(R.id.personsRecyclerView).getParent() instanceof ConstraintLayout
                ? (ConstraintLayout) findViewById(R.id.personsRecyclerView).getParent()
                : null;

        if (layout != null) {
            layout.addView(clearFavoritesFab, params);
        }

        clearFavoritesFab.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.confirmation_title_dialog)
                    .setMessage(R.string.confirm_delete_favourite_persons_text_dialog)
                    .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                        deleteFavouritePersons();
                    })
                    .setNegativeButton(android.R.string.cancel, null)
                    .show();
        });
    }

    private void deleteFavouritePersons() {
        AppDatabase db = Room.databaseBuilder(this, AppDatabase.class, "PersonFav")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();

        db.personFavDao().deleteAll();
        Toast.makeText(this, getString(R.string.empty_favourite_persons), Toast.LENGTH_SHORT).show();
        finish();
    }
}
