package com.sanvalero.android.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.OptIn;
import androidx.fragment.app.Fragment;

import com.mapbox.geojson.Point;
import com.mapbox.maps.CameraOptions;
import com.mapbox.maps.MapView;
import com.mapbox.maps.Style;
import com.mapbox.maps.MapboxExperimental;
import com.mapbox.maps.plugin.annotation.AnnotationConfig;
import com.mapbox.maps.plugin.annotation.AnnotationPlugin;
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationManager;
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationManagerKt;
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions;
import com.mapbox.search.ApiType;
import com.mapbox.search.ResponseInfo;
import com.mapbox.search.ReverseGeoOptions;
import com.mapbox.search.SearchCallback;
import com.mapbox.search.SearchEngine;
import com.mapbox.search.SearchEngineSettings;
import com.mapbox.search.common.AsyncOperationTask;
import com.mapbox.search.result.SearchAddress;
import com.mapbox.search.result.SearchResult;
import com.sanvalero.android.R;

import com.mapbox.maps.plugin.gestures.GesturesPlugin;
import com.mapbox.maps.plugin.Plugin;

import java.util.List;

public class MapboxFragment extends Fragment {

    public interface OnAddressSelectedListener {
        void onAddressSelected(String address, Point point);
    }

    private MapView mapView;
    private SearchEngine searchEngine;
    private AsyncOperationTask searchRequestTask;
    private OnAddressSelectedListener addressListener;
    private static Point staticPoint;
    private PointAnnotationManager pointAnnotationManager;

    private final SearchCallback searchCallback = new SearchCallback() {
        @Override
        public void onResults(@NonNull List<SearchResult> results, @NonNull ResponseInfo responseInfo) {
            if (results.isEmpty()) {
                Log.i("MAPBOX_REVERSE", "No reverse geocoding results");
                if (addressListener != null) {
                    addressListener.onAddressSelected(getString(R.string.address_not_found_in_map), staticPoint);
                }
            } else {
                SearchResult result = results.get(0);
                String address = result.getAddress().formattedAddress();
                Point point = result.getCoordinate();
                Log.i("MAPBOX_REVERSE", "Address: " + address + " (" + point + ")");
                if (addressListener != null) {
                    addressListener.onAddressSelected(address, point);
                }
            }
        }

        @Override
        public void onError(@NonNull Exception e) {
            Log.e("MAPBOX_REVERSE", "Reverse geocoding error", e);
        }
    };

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_mapbox, container, false);
        mapView = root.findViewById(R.id.mapView);

        // Create search engine
        final SearchEngine searchEngine = SearchEngine.createSearchEngineWithBuiltInDataProviders(
                ApiType.SEARCH_BOX,
                new SearchEngineSettings()
        );

        mapView.getMapboxMap().loadStyleUri(Style.MAPBOX_STREETS, style -> {
            // Initialize the PointAnnotationManager
            AnnotationPlugin annotationPlugin = mapView.getPlugin(Plugin.MAPBOX_ANNOTATION_PLUGIN_ID);
            if (annotationPlugin != null) {
                AnnotationConfig annotationConfig = new AnnotationConfig();
                pointAnnotationManager = PointAnnotationManagerKt.createPointAnnotationManager(annotationPlugin, annotationConfig);
                pointAnnotationManager.addClickListener(pointAnnotation -> {
                    Point point = pointAnnotation.getPoint();

                    mapView.getMapboxMap().setCamera(
                            new CameraOptions.Builder()
                                    .center(point)
                                    .zoom(16.0) // Pretty close
                                    .build()
                    );
                    return true;
                });
            }
        });

        // Config click gesture on map
        GesturesPlugin gesturesPlugin = mapView.getPlugin(Plugin.MAPBOX_GESTURES_PLUGIN_ID);
        if (gesturesPlugin != null) {
            gesturesPlugin.addOnMapClickListener(point -> {
                Log.d("MAP_CLICK", "Lat: " + point.latitude() + ", Lon: " + point.longitude());

                // Delete previous markers
                pointAnnotationManager.deleteAll();
                // Add the new marker
                addMarker(point);

                staticPoint = point; // Store to use if no address is found with reverse-geocoding
                ReverseGeoOptions options = new ReverseGeoOptions.Builder(Point.fromLngLat(point.longitude(), point.latitude()))
                        .limit(1)
                        .build();

                // Reverse-geocoding
                searchRequestTask = searchEngine.search(options, searchCallback);
                return true;
            });
        } else {
            Log.e("MapFragment", "GesturesPlugin is null");
        }

        return root;
    }

    private void addMarker(Point point) {
        if (pointAnnotationManager == null) {
            return;
        }
        PointAnnotationOptions pointAnnotationOptions = new PointAnnotationOptions()
                .withPoint(Point.fromLngLat(point.longitude(), point.latitude()))
                .withIconImage(BitmapFactory.decodeResource(getResources(), R.drawable.red_marker));
        pointAnnotationManager.create(pointAnnotationOptions);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnAddressSelectedListener) {
            addressListener = (OnAddressSelectedListener) context;
        } else {
            throw new ClassCastException(context.toString() + " must implement OnAddressSelectedListener");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (searchRequestTask != null) {
            searchRequestTask.cancel();
        }
    }
}