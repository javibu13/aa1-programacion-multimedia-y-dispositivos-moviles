package com.sanvalero.android.view;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.sanvalero.android.R;
import com.sanvalero.android.callback.NewPersonCallback;
import com.sanvalero.android.model.Person;
import com.sanvalero.android.presenter.NewPersonPresenter;
import com.sanvalero.android.util.Utils;

import java.util.Calendar;
import java.util.Locale;

public class NewPersonActivity extends BaseActivity implements NewPersonCallback {

    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText birthDateEditText;
    private EditText heightEditText;
    private EditText interestsEditText;
    private CheckBox verifiedCheckBox;
    private Button createButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().setTitle(R.string.new_person_activity);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_person_activity);

        firstNameEditText = findViewById(R.id.firstNameNewPersonEditText);
        lastNameEditText = findViewById(R.id.lastNameNewPersonEditText);
        emailEditText = findViewById(R.id.emailNewPersonEditText);
        passwordEditText = findViewById(R.id.passwordNewPersonEditText);
        birthDateEditText = findViewById(R.id.birthDateNewPersonEditText);
        heightEditText = findViewById(R.id.heightNewPersonEditText);
        interestsEditText = findViewById(R.id.interestsNewPersonEditText);
        verifiedCheckBox = findViewById(R.id.verifiedNewPersonCheckBox);
        createButton = findViewById(R.id.newPersonButton);

        // Show datePicker when birthDate is clicked
        birthDateEditText.setFocusable(false);
        birthDateEditText.setClickable(true);
        birthDateEditText.setOnClickListener(view -> showDatePickerDialog());

        createButton.setOnClickListener(view -> {
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
                        .setMessage(R.string.confirm_create_person_text_dialog)
                        .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                            createPerson(person);
                        })
                        .setNegativeButton(android.R.string.cancel, null)
                        .show();
            } else {
                Toast.makeText(this, getString(R.string.error_required_toast), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void showDatePickerDialog() {
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

        if (passwordEditText.getText().toString().trim().isEmpty()) {
            passwordEditText.setError(getString(R.string.error_required));
            valid = false;
        }

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

    private void createPerson(Person person) {
        new NewPersonPresenter(this, person);
    }

    @Override
    public void onPersonCreated(Person person) {
        Toast.makeText(this, getString(R.string.new_person_toast) + " (ID " + person.getId().toString() + ")", Toast.LENGTH_SHORT).show();
        finish(); // Close the activity and go back to the previous one
    }

    @Override
    public void onPersonCreateError(String errorMessage) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.error_title_dialog)
                .setMessage(R.string.error_api_text_dialog)
                .setNeutralButton(android.R.string.ok, null)
                .show();
    }
}
