package com.sanvalero.android;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.sanvalero.android.view.BaseActivity;
import com.sanvalero.android.view.PersonsActivity;
import com.sanvalero.android.view.PlacesActivity;


public class MainActivity extends BaseActivity {

    private Button personsTextButton, placesTextButton;
    private TextView titleMainTextView, subtitleMainTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().setTitle(R.string.main_activity);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        titleMainTextView = findViewById(R.id.titleMainTextView);
        subtitleMainTextView = findViewById(R.id.subtitleMainTextView);

        personsTextButton = findViewById(R.id.personsMainTextButton);
        placesTextButton = findViewById(R.id.placesMainTextButton);

        personsTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, PersonsActivity.class);
                startActivity(intent);
            }
        });
        placesTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, PlacesActivity.class);
                startActivity(intent);
            }
        });
    }
}
