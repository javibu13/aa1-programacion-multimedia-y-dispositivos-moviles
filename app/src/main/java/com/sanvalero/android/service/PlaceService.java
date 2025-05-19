package com.sanvalero.android.service;

import com.sanvalero.android.model.Place;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface PlaceService {
    @GET("places")
    Call<List<Place>> getPlaces();
}
