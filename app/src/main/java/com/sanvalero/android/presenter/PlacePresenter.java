package com.sanvalero.android.presenter;

import android.util.Log;

import com.sanvalero.android.callback.PlaceCallback;
import com.sanvalero.android.callback.PlacesCallback;
import com.sanvalero.android.model.Place;
import com.sanvalero.android.network.ApiClient;
import com.sanvalero.android.service.PlaceService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PlacePresenter {

    private PlaceCallback callback;
    private Long id;

    public PlacePresenter(PlaceCallback callback, Long id) {
        this.callback = callback;
        this.id = id;
    }

    public void fetchPlace() {
        Retrofit retrofit = ApiClient.getClient();

        PlaceService service = retrofit.create(PlaceService.class);
        Call<Place> call = service.getPlaceById(id);

        Log.w("PlacePresenter", "Pre-CallEnqueue");

        call.enqueue(new Callback<Place>() {
            @Override
            public void onResponse(Call<Place> call, Response<Place> response) {
                Log.w("PlacePresenter", "Callback ok");
                if (response.isSuccessful()) {
                    Place place = response.body();
                    callback.onPlaceLoaded(place);
                } else {
                    callback.onPlaceLoadError(response.message());
                }
            }

            @Override
            public void onFailure(Call<Place> call, Throwable t) {
                Log.w("PlacePresenter", "Callback fail");
                callback.onPlaceLoadError(t.getMessage());
            }
        });
    }
}
