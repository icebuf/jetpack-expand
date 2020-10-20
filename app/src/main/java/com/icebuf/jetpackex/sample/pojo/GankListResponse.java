package com.icebuf.jetpackex.sample.pojo;

import java.util.List;

/**
 * @author IceTang
 * @version 1.0
 * Data: 2020/9/21
 * E-mail：bflyff@hotmail.com
 */
public class GankListResponse<T> {

    /**
     * data : []
     * status : 100
     */
    private List<T> data;
    private int status;

    public void setData(List<T> data) {
        this.data = data;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<T> getData() {
        return data;
    }

    public int getStatus() {
        return status;
    }
}
