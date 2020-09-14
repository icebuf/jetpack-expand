package com.icebuf.jetpackex.sample.rest;

import com.icebuf.jetpackex.sample.pojo.TianHotVideoEntity;
import com.icebuf.jetpackex.sample.pojo.TianNewsEntity;
import com.icebuf.jetpackex.sample.pojo.TianNewsResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * @author IceTang
 * @version 1.0
 * Data: 2020/9/11
 * E-mail：bflyff@hotmail.com
 */
public interface TianNewsApi extends TianApi{

    @GET("topnews/index?&key=" + KEY)
    Observable<TianNewsResponse<TianNewsEntity>> getTopNews(@Query("num") int num);

    @GET("txapi/dyvideohot/index?&key=" + KEY)
    Observable<TianNewsResponse<TianHotVideoEntity>> getHotVideos();
}
