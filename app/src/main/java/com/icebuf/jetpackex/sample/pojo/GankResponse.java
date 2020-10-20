package com.icebuf.jetpackex.sample.pojo;

/**
 * @author IceTang
 * @version 1.0
 * Data: 2020/9/21
 * E-mail：bflyff@hotmail.com
 */
public class GankResponse<T> {

    /**
     * data : []
     * status : 100
     */
    private T data;
    private int status;

    public void setData(T data) {
        this.data = data;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public T getData() {
        return data;
    }

    public int getStatus() {
        return status;
    }
}
