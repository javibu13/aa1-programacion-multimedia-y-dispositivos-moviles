package com.sanvalero.android;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

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
                messageTextView.setText("Button clicked!");
            }
        });
        placesTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                messageTextView.setText("Button clicked!");
            }
        });
    }
}
