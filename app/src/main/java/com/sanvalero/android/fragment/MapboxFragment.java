package com.sanvalero.android.fragment;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.mapbox.maps.MapView;
import com.mapbox.maps.Style;
import com.sanvalero.android.R;

public class MapboxFragment extends Fragment {

    private MapView mapView;

    public MapboxFragment() {
        super(R.layout.fragment_mapbox); // usa un layout con solo el MapView
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mapView = view.findViewById(R.id.mapView);

        // Init map
        mapView.getMapboxMap().loadStyleUri(Style.MAPBOX_STREETS);
    }
}
