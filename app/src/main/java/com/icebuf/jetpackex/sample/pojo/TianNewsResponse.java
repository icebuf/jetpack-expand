package com.icebuf.jetpackex.sample.pojo;

import java.util.List;

/**
 * @author IceTang
 * @version 1.0
 * Data: 2020/8/14
 * E-mail：bflyff@hotmail.com
 */
public class TianNewsResponse<T> {

    private String msg;
    private int code;
    private List<T> newslist;

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setNewslist(List<T> newslist) {
        this.newslist = newslist;
    }

    public String getMsg() {
        return msg;
    }

    public int getCode() {
        return code;
    }

    public List<T> getNewslist() {
        return newslist;
    }
}