package com.example.paramount.ratappandroid.dao;

/**
 * Created by greg on 12/4/17.
 * Interface with an onFailure method.
 */

public interface FailureCallback<T> {
    /**
     * called upon unsuccessful response from server
     * @param result response from server
     */
    void onFailure(T result);
}