package com.example.paramount.ratappandroid.dao;

/**
 * Created by greg on 10/29/17.
 *
 * Interface with an onSuccess method.
 */

public interface SuccessCallback<T> {
    /**
     * called upon successful response from server
     * @param result response from server
     */
    void onSuccess(T result);
}
