package com.icebuf.jetpackex.viewmodel;

import androidx.lifecycle.LiveData;

/**
 * @author IceTang
 * @version 1.0
 * Data: 2020/7/17
 * E-mail：bflyff@hotmail.com
 */
public class Result<T> {

    private final Status status;

    private final T data;

    private final String message;

    private boolean invalid;

    private Result(Status status, T data, String message) {
        this.status = status;
        this.data = data;
        this.message = message;
    }

    public Status status() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }

    public boolean isInvalid() {
        return invalid;
    }

    public void invalid() {
        this.invalid = true;
    }

    public enum Status {
        LOADING,
        SUCCESS,
        ERROR,
    }

    public static <T> Result<T> success(T data) {
        return new Result<>(Result.Status.SUCCESS, data,null);
    }

    public static <T> Result<T> loading() {
        return new Result<>(Result.Status.LOADING, null, null);
    }

    public static <T> Result<T> loading(T data) {
        return new Result<>(Result.Status.LOADING, data, null);
    }

    public static <T> Result<T> error(String message) {
        return new Result<>(Status.ERROR, null, message);
    }

    public static <T> Result<T> error(T data, String message) {
        return new Result<>(Status.ERROR, data, message);
    }

    public static <T> boolean isLoading(LiveData<Result<T>> liveData) {
        Result<T> result = liveData.getValue();
        return result != null && result.status() == Result.Status.LOADING;
    }
}
