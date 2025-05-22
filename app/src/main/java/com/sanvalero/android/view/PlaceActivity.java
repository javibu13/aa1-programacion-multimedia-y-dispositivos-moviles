package com.sanvalero.android.view;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mapbox.geojson.Point;
import com.sanvalero.android.R;
import com.sanvalero.android.callback.PlaceCallback;
import com.sanvalero.android.fragment.MapboxFragment;
import com.sanvalero.android.model.Place;
import com.sanvalero.android.presenter.PlacePresenter;
import com.sanvalero.android.presenter.PlacesPresenter;
import com.sanvalero.android.util.Utils;

import java.util.Calendar;
import java.util.Locale;

public class PlaceActivity extends BaseActivity implements PlaceCallback, MapboxFragment.OnAddressSelectedListener, MapboxFragment.OnMapboxReady {

    private PlacePresenter placePresenter;
    private Long id;
    private EditText nameEditText;
    private EditText addressEditText;
    private String coordinates;
    private EditText capacityEditText;
    private EditText areaEditText;
    private EditText inaugurationDateEditText;
    private EditText equipmentEditText;
    private CheckBox hasParkingCheckBox;
    private Switch allowEditSwitch;
    private FloatingActionButton favoriteFab;
    private FloatingActionButton deleteFab;
    private FloatingActionButton updateFab;
    private MapboxFragment mapboxFragment;
    private boolean mapboxReady;
    private Point initialPoint;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().setTitle(R.string.place_activity);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_place_activity);

        id = getIntent().getLongExtra("place_id", 0);
        nameEditText = findViewById(R.id.namePlaceEditText);
        addressEditText = findViewById(R.id.addressPlaceEditText);
        coordinates = "";
        capacityEditText = findViewById(R.id.capacityPlaceEditText);
        areaEditText = findViewById(R.id.areaPlaceEditText);
        inaugurationDateEditText = findViewById(R.id.inaugurationDatePlaceEditText);
        equipmentEditText = findViewById(R.id.equipmentPlaceEditText);
        hasParkingCheckBox = findViewById(R.id.hasParkingPlaceCheckBox);
        allowEditSwitch = findViewById(R.id.allowEditPlaceSwitch);
        allowEditSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            onEditableSwitchChange(isChecked);
        });
        favoriteFab = findViewById(R.id.favoritePlaceFab);
        deleteFab = findViewById(R.id.deletePlaceFab);
        updateFab = findViewById(R.id.updatePlaceFab);

        // Show datePicker when inaugurationDateEditText is clicked
        inaugurationDateEditText.setFocusable(false);
        inaugurationDateEditText.setClickable(true);
        inaugurationDateEditText.setOnClickListener(view -> showDatePickerDialog());

        mapboxReady = false;
        mapboxFragment = new MapboxFragment(allowEditSwitch);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.mapFragmentPlaceContainer, mapboxFragment)
                .commit();

        onEditableSwitchChange(false);

        placePresenter = new PlacePresenter(this, id);
        placePresenter.fetchPlace();

        updateFab.setOnClickListener(view -> {
            if (!allowEditSwitch.isChecked()) {
                // Allow update only if edit is allowed too
                Toast.makeText(this, getString(R.string.update_not_allowed), Toast.LENGTH_LONG).show();
                return;
            }
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
                        .setMessage(R.string.confirm_update_place_text_dialog)
                        .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                            updatePlace(place);
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
                    .setMessage(R.string.confirm_delete_place_text_dialog)
                    .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                        deletePlace();
                    })
                    .setNegativeButton(android.R.string.cancel, null)
                    .show();
        });
    }

    private void onEditableSwitchChange(boolean isChecked) {
        nameEditText.setFocusable(isChecked);
        nameEditText.setFocusableInTouchMode(isChecked);
        nameEditText.setClickable(isChecked);

        addressEditText.setFocusable(isChecked);
        addressEditText.setFocusableInTouchMode(isChecked);
        addressEditText.setClickable(isChecked);

        capacityEditText.setFocusable(isChecked);
        capacityEditText.setFocusableInTouchMode(isChecked);
        capacityEditText.setClickable(isChecked);

        areaEditText.setFocusable(isChecked);
        areaEditText.setFocusableInTouchMode(isChecked);
        areaEditText.setClickable(isChecked);

        inaugurationDateEditText.setFocusable(false);
        inaugurationDateEditText.setFocusableInTouchMode(false);
        inaugurationDateEditText.setClickable(isChecked);

        equipmentEditText.setFocusable(isChecked);
        equipmentEditText.setFocusableInTouchMode(isChecked);
        equipmentEditText.setClickable(isChecked);

        hasParkingCheckBox.setFocusable(isChecked);
        hasParkingCheckBox.setFocusableInTouchMode(isChecked);
        hasParkingCheckBox.setClickable(isChecked);
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

    private void updatePlace(Place place) {
        placePresenter.updatePlace(place);
    }

    private void deletePlace() {
        placePresenter.deletePlace();
    }

    @Override
    public void onPlaceLoaded(Place place) {
        nameEditText.setText(place.getName());
        String address = Utils.parseCustomAddress(place.getAddress()).second;
        addressEditText.setText(address.isEmpty() ? place.getAddress() : address);
        coordinates = Utils.parseCustomAddress(place.getAddress()).first;
        capacityEditText.setText(place.getCapacity().toString());
        areaEditText.setText(place.getArea().toString());
        inaugurationDateEditText.setText(place.getInaugurationDate());
        equipmentEditText.setText(place.getEquipment());
        hasParkingCheckBox.setChecked(place.getHasParking());
        allowEditSwitch.setChecked(false);
        if (!coordinates.isEmpty()) {
            Double latitude = Utils.parseCoordinates(coordinates).first;
            Double longitude = Utils.parseCoordinates(coordinates).second;
            initialPoint = Point.fromLngLat(latitude, longitude);
            displayInitialPointIfReady();
        }
    }

    @Override
    public void onPlaceLoadError(String errorMessage) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.error_title_dialog)
                .setMessage(R.string.error_api_text_dialog)
                .setNeutralButton(android.R.string.ok, null)
                .show();
    }

    @Override
    public void onPlaceDeleted() {
        Toast.makeText(this, getString(R.string.successfully_delete), Toast.LENGTH_LONG).show();
        finish();
    }

    @Override
    public void onPlaceDeleteError(String errorMessage) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.error_title_dialog)
                .setMessage(R.string.error_api_text_dialog)
                .setNeutralButton(android.R.string.ok, null)
                .show();
    }

    @Override
    public void onPlaceUpdated(Place place) {
        Toast.makeText(this, getString(R.string.place_updated), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPlaceUpdateError(String errorMessage) {
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

    @Override
    public void onMapboxReady() {
        mapboxReady = true;
        displayInitialPointIfReady();
    }

    private void displayInitialPointIfReady() {
        if (mapboxReady && initialPoint != null) {
            mapboxFragment.addMarker(initialPoint);
            mapboxFragment.centerView(initialPoint, 8.0);
        }
    }
}
