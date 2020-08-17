package com.icebuf.jetpackex.viewmodel;

import android.util.Log;

import androidx.lifecycle.Observer;

/**
 * @author IceTang
 * @version 1.0
 * Data: 2020/7/17
 * E-mail：bflyff@hotmail.com
 */
public abstract class ResultObserver<T> implements Observer<Result<T>> {

    public static final String TAG = ResultObserver.class.getSimpleName();

    @Override
    final public void onChanged(Result<T> result) {
        if (result == null || result.isInvalid()) {
            return;
        }
        switch (result.status()) {
            case LOADING:
                onLoading(result.getData());
                break;
            case SUCCESS:
                if(onSuccess(result.getData())){
                    result.invalid();
                }
                break;
            case ERROR:
                onError(result.getData(), result.getMessage());
                result.invalid();
                break;
        }
    }

    protected void onLoading(T data) {
        Log.e(TAG, "onLoading():: " + data);
    }

    protected abstract boolean onSuccess(T data);

    protected void onError(T data, String message) {
        Log.e(TAG, "onError():: " + data + " " + message);
    }

}
