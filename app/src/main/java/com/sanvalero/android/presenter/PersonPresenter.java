package com.sanvalero.android.presenter;

import android.util.Log;

import com.sanvalero.android.callback.PersonCallback;
import com.sanvalero.android.model.Person;
import com.sanvalero.android.network.ApiClient;
import com.sanvalero.android.service.PersonService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PersonPresenter {

    private PersonCallback callback;
    private Long id;

    public PersonPresenter(PersonCallback callback, Long id) {
        this.callback = callback;
        this.id = id;
    }

    public void fetchPerson() {
        Retrofit retrofit = ApiClient.getClient();

        PersonService service = retrofit.create(PersonService.class);
        Call<Person> call = service.getPersonById(id);

        Log.w("PersonPresenter", "Pre-CallEnqueue");

        call.enqueue(new Callback<Person>() {
            @Override
            public void onResponse(Call<Person> call, Response<Person> response) {
                Log.w("PersonPresenter", "Callback ok");
                if (response.isSuccessful()) {
                    Person person = response.body();
                    callback.onPersonLoaded(person);
                } else {
                    callback.onPersonLoadError(response.message());
                }
            }

            @Override
            public void onFailure(Call<Person> call, Throwable t) {
                Log.w("PersonPresenter", "Callback fail");
                callback.onPersonLoadError(t.getMessage());
            }
        });
    }

    public void updatePerson(Person person) {
        Retrofit retrofit = ApiClient.getClient();

        PersonService service = retrofit.create(PersonService.class);
        Call<Person> call = service.updatePersonById(id, person);

        Log.w("PersonPresenter", "Pre-CallEnqueue");

        call.enqueue(new Callback<Person>() {
            @Override
            public void onResponse(Call<Person> call, Response<Person> response) {
                Log.w("PersonPresenter", "Callback ok");
                if (response.isSuccessful()) {
                    Person person = response.body();
                    callback.onPersonUpdated(person);
                } else {
                    callback.onPersonUpdateError(response.message());
                }
            }

            @Override
            public void onFailure(Call<Person> call, Throwable t) {
                Log.w("PersonPresenter", "Callback fail");
                callback.onPersonUpdateError(t.getMessage());
            }
        });
    }

    public void deletePerson() {
        Retrofit retrofit = ApiClient.getClient();

        PersonService service = retrofit.create(PersonService.class);
        Call<Void> call = service.deletePersonById(id);

        Log.w("PersonPresenter", "Pre-CallEnqueue");

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.w("PersonPresenter", "Callback ok");
                if (response.isSuccessful()) {
                    callback.onPersonDeleted();
                } else {
                    callback.onPersonDeleteError(response.message());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.w("PersonPresenter", "Callback fail");
                callback.onPersonDeleteError(t.getMessage());
            }
        });
    }
}
