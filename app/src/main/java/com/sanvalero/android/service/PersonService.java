package com.sanvalero.android.service;

import com.sanvalero.android.model.Person;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface PersonService {
    @GET("persons")
    Call<List<Person>> getPersons();
}
