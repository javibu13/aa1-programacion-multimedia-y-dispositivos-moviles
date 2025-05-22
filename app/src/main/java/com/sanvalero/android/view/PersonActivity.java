package com.sanvalero.android.view;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.room.Room;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mapbox.geojson.Point;
import com.sanvalero.android.R;
import com.sanvalero.android.callback.NewPersonCallback;
import com.sanvalero.android.callback.PersonCallback;
import com.sanvalero.android.database.AppDatabase;
import com.sanvalero.android.database.PersonFav;
import com.sanvalero.android.model.Person;
import com.sanvalero.android.presenter.NewPersonPresenter;
import com.sanvalero.android.presenter.PersonPresenter;
import com.sanvalero.android.presenter.PlacePresenter;
import com.sanvalero.android.util.Utils;

import java.util.Calendar;
import java.util.Locale;

public class PersonActivity extends BaseActivity implements PersonCallback {

    private PersonPresenter personPresenter;
    private Long id;
    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText birthDateEditText;
    private EditText heightEditText;
    private EditText interestsEditText;
    private CheckBox verifiedCheckBox;
    private Switch allowEditSwitch;
    private FloatingActionButton favoriteFab;
    private FloatingActionButton deleteFab;
    private FloatingActionButton updateFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().setTitle(R.string.person_activity);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_person_activity);

        id = getIntent().getLongExtra("person_id", 0);
        firstNameEditText = findViewById(R.id.firstNameDetailPersonEditText);
        lastNameEditText = findViewById(R.id.lastNameDetailPersonEditText);
        emailEditText = findViewById(R.id.emailDetailPersonEditText);
        passwordEditText = findViewById(R.id.passwordDetailPersonEditText);
        birthDateEditText = findViewById(R.id.birthDateDetailPersonEditText);
        heightEditText = findViewById(R.id.heightDetailPersonEditText);
        interestsEditText = findViewById(R.id.interestsDetailPersonEditText);
        verifiedCheckBox = findViewById(R.id.verifiedDetailPersonCheckBox);
        allowEditSwitch = findViewById(R.id.allowEditPersonSwitch);
        allowEditSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            onEditableSwitchChange(isChecked);
        });
        favoriteFab = findViewById(R.id.favoritePersonFab);
        deleteFab = findViewById(R.id.deletePersonFab);
        updateFab = findViewById(R.id.updatePersonFab);

        // Show datePicker when birthDate is clicked
        birthDateEditText.setFocusable(false);
        birthDateEditText.setClickable(true);
        birthDateEditText.setOnClickListener(view -> showDatePickerDialog());

        onEditableSwitchChange(false);

        personPresenter = new PersonPresenter(this, id);
        personPresenter.fetchPerson();

        updateFab.setOnClickListener(view -> {
            if (!allowEditSwitch.isChecked()) {
                // Allow update only if edit is allowed too
                Toast.makeText(this, getString(R.string.update_not_allowed), Toast.LENGTH_LONG).show();
                return;
            }
            if (validateInputs()) {
                Person person = new Person();
                person.setFirstName(firstNameEditText.getText().toString().trim());
                person.setLastName(lastNameEditText.getText().toString().trim());
                person.setEmail(emailEditText.getText().toString().trim());
                person.setPass(passwordEditText.getText().toString().trim());
                person.setBirthDate(birthDateEditText.getText().toString().trim());
                // Calculate Age using BirthDate
                person.setAge(Utils.calculateAge(person.getBirthDate()));
                try {
                    person.setHeight(Double.parseDouble(heightEditText.getText().toString().trim()));
                } catch (NumberFormatException e) {
                    person.setHeight(0.0);
                }
                person.setInterests(interestsEditText.getText().toString().trim());
                person.setVerified(verifiedCheckBox.isChecked());

                new AlertDialog.Builder(this)
                        .setTitle(R.string.confirmation_title_dialog)
                        .setMessage(R.string.confirm_update_person_text_dialog)
                        .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                            updatePerson(person);
                        })
                        .setNegativeButton(android.R.string.cancel, null)
                        .show();
            } else {
                Toast.makeText(this, getString(R.string.error_required_toast), Toast.LENGTH_LONG).show();
            }
        });

        deleteFab.setOnClickListener(view -> {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.confirmation_title_dialog)
                    .setMessage(R.string.confirm_delete_person_text_dialog)
                    .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                        deletePerson();
                    })
                    .setNegativeButton(android.R.string.cancel, null)
                    .show();
        });

        favoriteFab.setOnClickListener(view -> {
            AppDatabase db = Room.databaseBuilder(this, AppDatabase.class, "PersonFav")
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration().build();
            PersonFav personFav = db.personFavDao().findById(id);
            if (personFav == null) {
                Log.i("DATABASE ROOM", "No personFav found by id");
                // Create register
                db.personFavDao().insert(new PersonFav(id, true));
                Toast.makeText(this, getString(R.string.added_to_favourites), Toast.LENGTH_SHORT).show();
            } else {
                Log.i("DATABASE ROOM", personFav.getId().toString());
                personFav.setFavourite(!personFav.isFavourite());
                db.personFavDao().update(personFav);
                if (personFav.isFavourite()) {
                    Toast.makeText(this, getString(R.string.added_to_favourites), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, getString(R.string.removed_to_favourites), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void onEditableSwitchChange(boolean isChecked) {
        firstNameEditText.setFocusable(isChecked);
        firstNameEditText.setFocusableInTouchMode(isChecked);
        firstNameEditText.setClickable(isChecked);

        lastNameEditText.setFocusable(isChecked);
        lastNameEditText.setFocusableInTouchMode(isChecked);
        lastNameEditText.setClickable(isChecked);

        emailEditText.setFocusable(isChecked);
        emailEditText.setFocusableInTouchMode(isChecked);
        emailEditText.setClickable(isChecked);

        passwordEditText.setFocusable(isChecked);
        passwordEditText.setFocusableInTouchMode(isChecked);
        passwordEditText.setClickable(isChecked);

        birthDateEditText.setFocusable(false);
        birthDateEditText.setFocusableInTouchMode(false);
        birthDateEditText.setClickable(isChecked);

        heightEditText.setFocusable(isChecked);
        heightEditText.setFocusableInTouchMode(isChecked);
        heightEditText.setClickable(isChecked);

        interestsEditText.setFocusable(isChecked);
        interestsEditText.setFocusableInTouchMode(isChecked);
        interestsEditText.setClickable(isChecked);

        verifiedCheckBox.setFocusable(isChecked);
        verifiedCheckBox.setFocusableInTouchMode(isChecked);
        verifiedCheckBox.setClickable(isChecked);
    }

    private void showDatePickerDialog() {
        if (!allowEditSwitch.isChecked()) {
            return;
        }
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (DatePicker view, int selectedYear, int selectedMonth, int selectedDay) -> {
                    String selectedDate = String.format(Locale.getDefault(), "%04d-%02d-%02d",
                            selectedYear, selectedMonth + 1, selectedDay);
                    birthDateEditText.setText(selectedDate);
                    if (!birthDateEditText.getText().toString().trim().isEmpty()) {
                        birthDateEditText.setError(null);
                    }
                },
                year, month, day
        );

        datePickerDialog.show();
    }

    private boolean validateInputs() {
        boolean valid = true;

        if (firstNameEditText.getText().toString().trim().isEmpty()) {
            firstNameEditText.setError(getString(R.string.error_required));
            valid = false;
        }

        if (lastNameEditText.getText().toString().trim().isEmpty()) {
            lastNameEditText.setError(getString(R.string.error_required));
            valid = false;
        }

        if (emailEditText.getText().toString().trim().isEmpty()) {
            emailEditText.setError(getString(R.string.error_required));
            valid = false;
        }

//        if (passwordEditText.getText().toString().trim().isEmpty()) {
//            // Disable password validation to allow update person without changing the password
//            passwordEditText.setError(getString(R.string.error_required));
//            valid = false;
//        }

        if (birthDateEditText.getText().toString().trim().isEmpty()) {
            birthDateEditText.setError(getString(R.string.error_required));
            valid = false;
        }

        if (heightEditText.getText().toString().trim().isEmpty()) {
            heightEditText.setError(getString(R.string.error_required));
            valid = false;
        }

        if (interestsEditText.getText().toString().trim().isEmpty()) {
            interestsEditText.setError(getString(R.string.error_required));
            valid = false;
        }

        return valid;
    }

    private void updatePerson(Person person) {
        personPresenter.updatePerson(person);
    }

    private void deletePerson() {
        personPresenter.deletePerson();
    }

    @Override
    public void onPersonLoaded(Person person) {
        firstNameEditText.setText(person.getFirstName());
        lastNameEditText.setText(person.getLastName());
        emailEditText.setText(person.getEmail());
        birthDateEditText.setText(person.getBirthDate());
        heightEditText.setText(person.getHeight().toString());
        interestsEditText.setText(person.getInterests());
        verifiedCheckBox.setChecked(person.getVerified());
    }

    @Override
    public void onPersonLoadError(String errorMessage) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.error_title_dialog)
                .setMessage(R.string.error_api_text_dialog)
                .setNeutralButton(android.R.string.ok, null)
                .show();
    }

    @Override
    public void onPersonDeleted() {
        Toast.makeText(this, getString(R.string.successfully_delete), Toast.LENGTH_SHORT).show();
        finish(); // Close the activity and go back to the previous one
    }

    @Override
    public void onPersonDeleteError(String errorMessage) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.error_title_dialog)
                .setMessage(R.string.error_api_text_dialog)
                .setNeutralButton(android.R.string.ok, null)
                .show();
    }

    @Override
    public void onPersonUpdated(Person person) {
        Toast.makeText(this, getString(R.string.person_updated), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPersonUpdateError(String errorMessage) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.error_title_dialog)
                .setMessage(R.string.error_api_text_dialog)
                .setNeutralButton(android.R.string.ok, null)
                .show();
    }
}
