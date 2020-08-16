package com.icebuf.jetpackex.sample.rest;

import com.icebuf.jetpackex.sample.pojo.ToutiaoResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ToutiaoApi {

    String BASE_URL = "http://api.tianapi.com/";

    String KEY = "f6cb9088c78f406088025d8f78519067";


    @GET("topnews/index?&key=" + KEY)
    Observable<ToutiaoResponse> getTopNews(@Query("num") int num);
}
