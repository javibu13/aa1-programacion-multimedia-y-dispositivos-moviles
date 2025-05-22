package com.sanvalero.android.service;

import com.sanvalero.android.model.Person;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface PersonService {
    @GET("persons")
    Call<List<Person>> getPersons();

    @GET("persons/{idPerson}")
    Call<Person> getPersonById(@Path("idPerson") Long idPerson);

    @POST("persons")
    Call<Person> createPerson(@Body Person person);

    @PUT("persons/{idPerson}")
    Call<Person> updatePersonById(@Path("idPerson") Long idPerson, @Body Person person);

    @DELETE("persons/{idPerson}")
    Call<Void> deletePersonById(@Path("idPerson") Long idPerson);
}
