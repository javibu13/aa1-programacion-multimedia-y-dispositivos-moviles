package com.sanvalero.android.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sanvalero.android.MainActivity;
import com.sanvalero.android.R;
import com.sanvalero.android.adapter.PersonsAdapter;
import com.sanvalero.android.callback.PersonsCallback;
import com.sanvalero.android.model.Person;
import com.sanvalero.android.presenter.PersonsPresenter;

import java.util.List;

public class PersonsActivity extends BaseActivity implements PersonsCallback {
    private RecyclerView recyclerView;
    private PersonsAdapter adapter;
    private PersonsPresenter personsPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().setTitle(R.string.persons_activity);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.persons_activity);

        recyclerView = findViewById(R.id.personsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        personsPresenter = new PersonsPresenter(this);
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
        adapter = new PersonsAdapter(persons);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onPersonsLoadError(String errorMessage) {
        Log.e("PersonsActivity", "Error: " + errorMessage);
        new AlertDialog.Builder(this)
            .setTitle("Failure")
            .setMessage("Error occurred during data retrieve from API.")
            .setNeutralButton("Close", (dialog, which) -> {
                Intent intent = new Intent(PersonsActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            })
            .show();
    }
}
