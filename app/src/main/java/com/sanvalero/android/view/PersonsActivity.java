package com.sanvalero.android.view;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.sanvalero.android.R;

public class PersonsActivity extends AppCompatActivity {
    private TextView personsListTitleTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.persons_activity);

        personsListTitleTextView = findViewById(R.id.personsListTitleTextView);

    }
}
