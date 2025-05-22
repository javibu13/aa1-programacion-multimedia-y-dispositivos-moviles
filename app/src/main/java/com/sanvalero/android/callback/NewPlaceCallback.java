package com.sanvalero.android.callback;

import com.sanvalero.android.model.Place;

public interface NewPlaceCallback {
    void onPlaceCreated(Place place);
    void onPlaceCreateError(String errorMessage);
}
