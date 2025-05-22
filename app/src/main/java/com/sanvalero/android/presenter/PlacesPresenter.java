package com.sanvalero.android.presenter;

import android.util.Log;

import com.sanvalero.android.callback.PlacesCallback;
import com.sanvalero.android.model.Place;
import com.sanvalero.android.network.ApiClient;
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

    public PlacesPresenter(PlacesCallback callback) {
        this.callback = callback;
    }

    public void fetchPlaces() {
        Retrofit retrofit = ApiClient.getClient();

        PlaceService service = retrofit.create(PlaceService.class);
        Call<List<Place>> call = service.getPlaces();

        Log.w("PlacesPresenter", "Pre-CallEnqueue");

        call.enqueue(new Callback<List<Place>>() {
            @Override
            public void onResponse(Call<List<Place>> call, Response<List<Place>> response) {
                Log.w("PlacesPresenter", "Callback ok");
                if (response.isSuccessful()) {
                    List<Place> places = response.body();
                    callback.onPlacesLoaded(places);
                } else {
                    callback.onPlacesLoadError(response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Place>> call, Throwable t) {
                Log.w("PlacesPresenter", "Callback fail");
                callback.onPlacesLoadError(t.getMessage());
            }
        });
    }
}
