package com.example.paramount.ratappandroid.dao;

/**
 * Created by greg on 10/29/17.
 *
 * Interface with an onSuccess method.
 */

public interface Callback<T> {
    void onSuccess(T result);
}
