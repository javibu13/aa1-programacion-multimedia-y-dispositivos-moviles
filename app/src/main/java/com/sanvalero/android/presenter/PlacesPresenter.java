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
    private static final String BASE_URL = "http://ec2-44-218-249-120.compute-1.amazonaws.com:8081/";

    public PlacesPresenter(PlacesCallback callback) {
        this.callback = callback;
    }

    public void fetchPlaces() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        PlaceService service = retrofit.create(PlaceService.class);
        Call<List<Place>> call = service.getPlaces();

        Log.w("PlacesActivity", "Pre-CallEnqueue");

        call.enqueue(new Callback<List<Place>>() {
            @Override
            public void onResponse(Call<List<Place>> call, Response<List<Place>> response) {
                Log.w("PlacesActivity", "Callback went good");
                if (response.isSuccessful()) {
                    List<Place> places = response.body();
                    callback.onPlacesLoaded(places);
                } else {
                    callback.onPlacesLoadError(response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Place>> call, Throwable t) {
                Log.w("PlacesActivity", "Callback went good");
                callback.onPlacesLoadError(t.getMessage());
            }
        });
    }
}
