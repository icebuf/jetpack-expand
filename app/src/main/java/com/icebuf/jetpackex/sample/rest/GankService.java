package com.icebuf.jetpackex.sample.rest;

import com.icebuf.jetpackex.sample.pojo.GankArticleEntity;
import com.icebuf.jetpackex.sample.pojo.GankBannerEntity;
import com.icebuf.jetpackex.sample.pojo.GankDataEntity;
import com.icebuf.jetpackex.sample.pojo.GankListResponse;
import com.icebuf.jetpackex.sample.pojo.GankPageResponse;
import com.icebuf.jetpackex.sample.pojo.GankRandomResponse;
import com.icebuf.jetpackex.sample.pojo.GankResponse;
import com.icebuf.jetpackex.sample.pojo.GankTypeEntity;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * @author IceTang
 * @version 1.0
 * Data: 2020/9/21
 * E-mail：bflyff@hotmail.com
 */
public interface GankService {

    String BASE_URL = "https://gank.io/api/v2/";

    enum CategoryType {
        GanHuo,
        Article,
        Girl;
    }

    enum HotType {
        Views("views"),
        Likes("likes"),
        Comments("comments"),
        ;

        String value;

        HotType(String val) {
            value = val;
        }
    }

    /**
     * 首页banner轮播
     * @return 返回首页banner轮播的数据
     */
    @GET("banners")
    Observable<GankListResponse<GankBannerEntity>> getBanners();

    /**
     * 获取所有分类具体子分类[types]数据
     * @param category Article | GanHuo | Girl
     * @return 子分类数据
     */
    @GET("categories/{category}")
    Observable<GankListResponse<GankTypeEntity>> getType(@Path("category") String category);

    /**
     * 分类数据
     * @param category All(所有分类) | Article | GanHuo | Girl
     * @param type All(全部类型) | Android | iOS | Flutter | Girl ...，即分类API返回的类型数据
     * @param page >=1
     * @param count [10, 50]
     * @return 分类数据
     */
    @GET("data/category/{category}/type/{type}/page/{page}/count/{count}")
    Observable<GankPageResponse<GankDataEntity>> getData(@Path("category") String category,
                                                         @Path("type") String type,
                                                         @Path("page") int page,
                                                         @Path("count") int count);

    /**
     * 随机获取
     * @param category All(所有分类) | Article | GanHuo | Girl
     * @param type All(全部类型) | Android | iOS | Flutter | Girl ...，即分类API返回的类型数据
     * @param count [10, 50]
     * @return 数据列表
     */
    @GET("random/category/{category}/type/{type}/count/{count}")
    Observable<GankRandomResponse<GankDataEntity>> getRandom(@Path("category") String category,
                                                     @Path("type") String type,
                                                     @Path("count") int count);

    /***
     * 本周最热
     * @param hotType views（浏览数） | likes（点赞数） | comments（评论数）
     * @param category  Article | GanHuo | Girl
     * @param count [1, 20]
     * @return 本周最热
     */
    @GET("hot/{hot_type}/category/{category}/count/{count}")
    Observable<GankListResponse<Object>> getHot(@Path("hot_type") String hotType,
                                                @Path("category") String category,
                                                @Path("count") int count);

    /**
     * 文章详情
     * @param postId 可接受参数 文章id[分类数据API返回的_id字段]
     * @return 文章详情
     */
    @GET("post/{post_id}")
    Observable<GankResponse<GankArticleEntity>> getArticleDetails(@Path("post_id") String postId);

    /**
     * 文章评论获取
     * @param postId 可接受参数 文章Id
     * @return 文章评论
     */
    @GET("post/comments/{post_id}")
    Observable<GankListResponse<Object>> getArticleComments(@Path("post_id") String postId);

    /**
     * 搜索
     * @param content 要搜索的内容
     * @param category All[所有分类] | Article | GanHuo
     * @param type Android | iOS | Flutter ...，即分类API返回的类型数据
     * @param page [10, 50]
     * @param count >=1
     * @return 搜索结果
     */
    @GET("search/{search}/category/{category}/type/{type}/page/{page}/count/{count}")
    Observable<GankListResponse<Object>> find(@Path("search") String content,
                                              @Path("category") String category,
                                              @Path("type") String type,
                                              @Path("page") int page,
                                              @Path("count") int count);
}
