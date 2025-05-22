package com.sanvalero.android.callback;

import com.sanvalero.android.model.Person;

public interface PersonCallback {
    void onPersonLoaded(Person person);
    void onPersonLoadError(String errorMessage);
    void onPersonDeleted();
    void onPersonDeleteError(String errorMessage);
    void onPersonUpdated(Person person);
    void onPersonUpdateError(String errorMessage);
}
