package com.icebuf.jetpackex.sample.pojo;

/**
 * @author IceTang
 * @version 1.0
 * Data: 2020/9/21
 * E-mail：bflyff@hotmail.com
 */
public class GankRandomResponse<T> extends GankListResponse<T> {

    /**
     * data : []
     * counts : 10
     * status : 100
     */
    private int counts;

    public void setCounts(int counts) {
        this.counts = counts;
    }

    public int getCounts() {
        return counts;
    }
}
