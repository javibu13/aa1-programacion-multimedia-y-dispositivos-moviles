package com.sanvalero.android.presenter;

import com.sanvalero.android.callback.NewPlaceCallback;
import com.sanvalero.android.model.Place;
import com.sanvalero.android.network.ApiClient;
import com.sanvalero.android.service.PlaceService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class NewPlacePresenter {

    private NewPlaceCallback callback;

    public NewPlacePresenter(NewPlaceCallback callback) {
        this.callback = callback;
    }

    public NewPlacePresenter(NewPlaceCallback callback, Place place) {
        this.callback = callback;
        this.createPlace(place);
    }

    public void createPlace(Place place) {
        Retrofit retrofit = ApiClient.getClient();
        PlaceService service = retrofit.create(PlaceService.class);

        Call<Place> call = service.createPlace(place);

        call.enqueue(new Callback<Place>() {
            @Override
            public void onResponse(Call<Place> call, Response<Place> response) {
                if (response.isSuccessful()) {
                    Place place = response.body();
                    callback.onPlaceCreated(place);
                } else {
                    callback.onPlaceCreateError("Error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Place> call, Throwable t) {
                callback.onPlaceCreateError("Network error: " + t.getMessage());
            }
        });
    }
}
