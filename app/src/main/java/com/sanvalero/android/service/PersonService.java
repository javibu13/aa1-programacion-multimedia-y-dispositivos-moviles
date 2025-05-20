package com.sanvalero.android.service;

import com.sanvalero.android.model.Person;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface PersonService {
    @GET("persons")
    Call<List<Person>> getPersons();

    @POST("persons")
    Call<Person> createPerson(@Body Person person);
}
