package com.sanvalero.android.presenter;

import android.util.Log;

import com.sanvalero.android.callback.PlacesCallback;
import com.sanvalero.android.model.Place;
import com.sanvalero.android.service.PlaceService;

import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PlacesPresenter {

    private PlacesCallback callback;
    private static final String BASE_URL = "https://api.example.com/"; // reemplaza con la URL real

    public PlacesPresenter(PlacesCallback callback) {
        this.callback = callback;
    }

    public void fetchPlacesMock () {
        List<Place> places = new ArrayList<>();
        places.add(new Place("Puerto Venecia1", "Teatro Malibrán1"));
        places.add(new Place("Puerto Venecia2", "Teatro Malibrán2"));
        places.add(new Place("Puerto Venecia3", "Teatro Malibrán3"));
        places.add(new Place("Puerto Venecia4", "Teatro Malibrán4"));
        places.add(new Place("Puerto Venecia5", "Teatro Malibrán5"));
        places.add(new Place("Puerto Venecia6", "Teatro Malibrán6"));
        places.add(new Place("Puerto Venecia7", "Teatro Malibrán7"));
        places.add(new Place("Puerto Venecia8", "Teatro Malibrán8"));
        places.add(new Place("Puerto Venecia9", "Teatro Malibrán9"));
        callback.onPlacesLoaded(places);
    }

    public void fetchPlaces() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        PlaceService service = retrofit.create(PlaceService.class);
        Call<List<Place>> call = service.getPlaces();

        call.enqueue(new Callback<List<Place>>() {
            @Override
            public void onResponse(Call<List<Place>> call, Response<List<Place>> response) {
                if (response.isSuccessful()) {
                    List<Place> places = response.body();
                    callback.onPlacesLoaded(places);
                } else {
                    callback.onPlacesLoadError(response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Place>> call, Throwable t) {
                callback.onPlacesLoadError(t.getMessage());
            }
        });
    }
}
