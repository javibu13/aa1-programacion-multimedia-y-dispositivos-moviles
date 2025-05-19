package com.sanvalero.android.view;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

import com.sanvalero.android.MainActivity;
import com.sanvalero.android.R;

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_home) {
            startActivity(new Intent(this, MainActivity.class));
        } else if (id == R.id.menu_persons) {
            startActivity(new Intent(this, PersonsActivity.class));
        } else if (id == R.id.menu_places) {
            startActivity(new Intent(this, PlacesActivity.class));
        } else if (id == R.id.menu_new_person) {
            startActivity(new Intent(this, NewPersonActivity.class));
        } else if (id == R.id.menu_new_place) {
            startActivity(new Intent(this, NewPlaceActivity.class));
        }
        return true;
    }
}
