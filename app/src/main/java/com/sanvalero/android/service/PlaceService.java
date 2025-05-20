package com.sanvalero.android.service;

import com.sanvalero.android.model.Place;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface PlaceService {
    @GET("places")
    Call<List<Place>> getPlaces();

    @POST("places")
    Call<Place> createPlace(@Body Place place);
}
