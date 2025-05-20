package com.sanvalero.android.presenter;

import com.sanvalero.android.callback.NewPersonCallback;
import com.sanvalero.android.model.Person;
import com.sanvalero.android.network.ApiClient;
import com.sanvalero.android.service.PersonService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class NewPersonPresenter {

    private NewPersonCallback callback;

    public NewPersonPresenter(NewPersonCallback callback) {
        this.callback = callback;
    }

    public NewPersonPresenter(NewPersonCallback callback, Person person) {
        this.callback = callback;
        this.createPerson(person);
    }

    public void createPerson(Person person) {
        Retrofit retrofit = ApiClient.getClient();
        PersonService service = retrofit.create(PersonService.class);

        Call<Person> call = service.createPerson(person);

        call.enqueue(new Callback<Person>() {
            @Override
            public void onResponse(Call<Person> call, Response<Person> response) {
                if (response.isSuccessful()) {
                    Person person = response.body();
                    callback.onPersonCreated(person);
                } else {
                    callback.onPersonCreateError("Error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Person> call, Throwable t) {
                callback.onPersonCreateError("Network error: " + t.getMessage());
            }
        });
    }
}
