package com.sanvalero.android.view;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.mapbox.geojson.Point;
import com.sanvalero.android.R;
import com.sanvalero.android.callback.NewPlaceCallback;
import com.sanvalero.android.fragment.MapboxFragment;
import com.sanvalero.android.model.Place;
import com.sanvalero.android.presenter.NewPlacePresenter;
import com.sanvalero.android.util.Utils;

import java.util.Calendar;
import java.util.Locale;

public class NewPlaceActivity extends BaseActivity implements NewPlaceCallback, MapboxFragment.OnAddressSelectedListener {

    private EditText nameEditText;
    private EditText addressEditText;
    private String coordinates;
    private EditText capacityEditText;
    private EditText areaEditText;
    private EditText inaugurationDateEditText;
    private EditText equipmentEditText;
    private CheckBox hasParkingCheckBox;
    private Button createButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().setTitle(R.string.new_place_activity);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_place_activity);

        nameEditText = findViewById(R.id.nameNewPlaceEditText);
        addressEditText = findViewById(R.id.addressNewPlaceEditText);
        coordinates = "";
        capacityEditText = findViewById(R.id.capacityNewPlaceEditText);
        areaEditText = findViewById(R.id.areaNewPlaceEditText);
        inaugurationDateEditText = findViewById(R.id.inaugurationDateNewPlaceEditText);
        equipmentEditText = findViewById(R.id.equipmentNewPlaceEditText);
        hasParkingCheckBox = findViewById(R.id.hasParkingNewPlaceCheckBox);
        createButton = findViewById(R.id.newPlaceButton);

        // Show datePicker when inaugurationDateEditText is clicked
        inaugurationDateEditText.setFocusable(false);
        inaugurationDateEditText.setClickable(true);
        inaugurationDateEditText.setOnClickListener(view -> showDatePickerDialog());

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.mapFragmentNewPlaceContainer, new MapboxFragment())
                .commit();

        createButton.setOnClickListener(view -> {
            if (validateInputs()) {
                Place place = new Place();
                place.setName(nameEditText.getText().toString().trim());
                String customAddress = Utils.generateCustomAddress(addressEditText.getText().toString().trim(), coordinates);
                place.setAddress(customAddress);
                try {
                    place.setCapacity(Integer.parseInt(capacityEditText.getText().toString().trim()));
                } catch (NumberFormatException e) {
                    place.setCapacity(0);
                }
                try {
                    place.setArea(Double.parseDouble(areaEditText.getText().toString().trim()));
                } catch (NumberFormatException e) {
                    place.setArea(0.0);
                }
                place.setInaugurationDate(inaugurationDateEditText.getText().toString().trim());
                place.setHasParking(hasParkingCheckBox.isChecked());
                place.setEquipment(equipmentEditText.getText().toString().trim());

                new AlertDialog.Builder(this)
                        .setTitle(R.string.confirmation_title_dialog)
                        .setMessage(R.string.confirm_create_place_text_dialog)
                        .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                            createPlace(place);
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
                    inaugurationDateEditText.setText(selectedDate);
                    if (!inaugurationDateEditText.getText().toString().trim().isEmpty()) {
                        inaugurationDateEditText.setError(null);
                    }
                },
                year, month, day
        );

        datePickerDialog.show();
    }

    private boolean validateInputs() {
        boolean valid = true;

        if (nameEditText.getText().toString().trim().isEmpty()) {
            nameEditText.setError(getString(R.string.error_required));
            valid = false;
        }

        if (addressEditText.getText().toString().trim().isEmpty()) {
            addressEditText.setError(getString(R.string.error_required));
            valid = false;
        }

        if (coordinates.isEmpty()) {
            coordinates = "-7.094123746230553, 107.66860246750076";
        }

        if (capacityEditText.getText().toString().trim().isEmpty()) {
            capacityEditText.setError(getString(R.string.error_required));
            valid = false;
        }

        if (areaEditText.getText().toString().trim().isEmpty()) {
            areaEditText.setError(getString(R.string.error_required));
            valid = false;
        }

        if (inaugurationDateEditText.getText().toString().trim().isEmpty()) {
            inaugurationDateEditText.setError(getString(R.string.error_required));
            valid = false;
        }

        if (equipmentEditText.getText().toString().trim().isEmpty()) {
            equipmentEditText.setError(getString(R.string.error_required));
            valid = false;
        }

        return valid;
    }

    private void createPlace(Place place) {
        new NewPlacePresenter(this, place);
    }

    @Override
    public void onPlaceCreated(Place place) {
        Toast.makeText(this, getString(R.string.new_place_toast) + " (ID " + place.getId().toString() + ")", Toast.LENGTH_SHORT).show();
        finish(); // Close the activity and go back to the previous one
    }

    @Override
    public void onPlaceCreateError(String errorMessage) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.error_title_dialog)
                .setMessage(R.string.error_api_text_dialog)
                .setNeutralButton(android.R.string.ok, null)
                .show();
    }

    @Override
    public void onAddressSelected(String address, Point point) {
        coordinates = point.coordinates().toString().replace('[', ' ').replace(']', ' ').trim();
        addressEditText.setText(address);
        Log.v("NEW PLACE", coordinates);
        Toast.makeText(this, getString(R.string.new_address_from_map), Toast.LENGTH_SHORT).show();
    }
}
