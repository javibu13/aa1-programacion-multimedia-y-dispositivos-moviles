package com.sanvalero.android.service;

import com.sanvalero.android.model.Place;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface PlaceService {
    @GET("places")
    Call<List<Place>> getPlaces();

    @GET("places/{idPlace}")
    Call<Place> getPlaceById(@Path("idPlace") int idPlace);

    @POST("places")
    Call<Place> createPlace(@Body Place place);

    @PUT("places/{idPlace}")
    Call<Place> updatePlaceById(@Path("idPlace") int idPlace, @Body Place place);

    @DELETE("places/{idPlace}")
    Call<Void> deletePlaceById(@Path("idPlace") int idPlace);
}
