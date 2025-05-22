package com.sanvalero.android.callback;

import com.sanvalero.android.model.Place;

public interface PlaceCallback {
    void onPlaceLoaded(Place place);
    void onPlaceLoadError(String errorMessage);
    void onPlaceDeleted();
    void onPlaceDeleteError(String errorMessage);
    void onPlaceUpdated(Place place);
    void onPlaceUpdateError(String errorMessage);
}
