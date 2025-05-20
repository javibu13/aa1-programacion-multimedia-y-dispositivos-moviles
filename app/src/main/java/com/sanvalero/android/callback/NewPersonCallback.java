package com.sanvalero.android.callback;

import com.sanvalero.android.model.Person;

public interface NewPersonCallback {
    void onPersonCreated(Person person);
    void onPersonCreateError(String errorMessage);
}
