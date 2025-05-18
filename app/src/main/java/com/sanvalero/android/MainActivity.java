package com.sanvalero.android;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.sanvalero.android.view.PersonsActivity;
import com.sanvalero.android.view.PlacesActivity;


public class MainActivity extends AppCompatActivity {

    private TextView messageTextView;
    private Button personsTextButton;
    private Button placesTextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        messageTextView = findViewById(R.id.messageTextView);
        personsTextButton = findViewById(R.id.personsTextButton);
        placesTextButton = findViewById(R.id.placesTextButton);

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
