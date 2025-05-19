package com.sanvalero.android.view;

import android.os.Bundle;

import com.sanvalero.android.R;

public class NewPlaceActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().setTitle(R.string.new_place_activity);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_place_activity);
    }
}