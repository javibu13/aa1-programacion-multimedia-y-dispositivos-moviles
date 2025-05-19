package com.sanvalero.android.callback;

import com.sanvalero.android.model.Person;
import com.sanvalero.android.model.Place;

import java.util.List;

public interface PersonsCallback {
    void onPersonsLoaded(List<Person> persons);
    void onPersonsLoadError(String errorMessage);
}
