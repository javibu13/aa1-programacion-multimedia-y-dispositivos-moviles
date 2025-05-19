package com.sanvalero.android.presenter;

import android.util.Log;

import com.sanvalero.android.callback.PersonsCallback;
import com.sanvalero.android.model.Person;
import com.sanvalero.android.network.ApiClient;
import com.sanvalero.android.service.PersonService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PersonsPresenter {

    private PersonsCallback callback;

    public PersonsPresenter(PersonsCallback callback) {
        this.callback = callback;
    }

    public void fetchPersons() {
        Retrofit retrofit = ApiClient.getClient();

        PersonService service = retrofit.create(PersonService.class);
        Call<List<Person>> call = service.getPersons();

        Log.w("PersonsPresenter", "Pre-CallEnqueue");

        call.enqueue(new Callback<List<Person>>() {
            @Override
            public void onResponse(Call<List<Person>> call, Response<List<Person>> response) {
                Log.w("PersonsPresenter", "Callback ok");
                if (response.isSuccessful()) {
                    List<Person> persons = response.body();
                    callback.onPersonsLoaded(persons);
                } else {
                    callback.onPersonsLoadError(response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Person>> call, Throwable t) {
                Log.w("PersonsPresenter", "Callback fail");
                callback.onPersonsLoadError(t.getMessage());
            }
        });
    }
}
