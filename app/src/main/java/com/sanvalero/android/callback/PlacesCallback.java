package com.sanvalero.android.callback;

import com.sanvalero.android.model.Place;

import java.util.List;

public interface PlacesCallback {
    void onPlacesLoaded(List<Place> places);
    void onPlacesLoadError(String errorMessage);
    void onPlaceClicked(Long idPlaceClicked);
}
