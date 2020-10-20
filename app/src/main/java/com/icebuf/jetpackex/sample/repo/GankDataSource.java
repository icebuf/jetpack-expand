package com.icebuf.jetpackex.sample.repo;

import com.icebuf.jetpackex.sample.pojo.GankArticleEntity;
import com.icebuf.jetpackex.sample.pojo.GankBannerEntity;
import com.icebuf.jetpackex.sample.pojo.GankDataEntity;
import com.icebuf.jetpackex.sample.pojo.GankListResponse;
import com.icebuf.jetpackex.sample.pojo.GankPageResponse;
import com.icebuf.jetpackex.sample.pojo.GankRandomResponse;
import com.icebuf.jetpackex.sample.pojo.GankResponse;
import com.icebuf.jetpackex.sample.pojo.GankTypeEntity;
import com.icebuf.jetpackex.sample.rest.GankService;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * @author IceTang
 * @version 1.0
 * Data: 2020/9/21
 * E-mail：bflyff@hotmail.com
 *
 * 代理Service
 */
public class GankDataSource extends DataSource implements GankService{

    @Inject
    GankService mGankService;

    @Inject
    public GankDataSource() {

    }

    @Override
    public Observable<GankListResponse<GankBannerEntity>> getBanners() {
        return subAndObs(mGankService.getBanners());
    }

    @Override
    public Observable<GankListResponse<GankTypeEntity>> getType(String category) {
        return subAndObs(mGankService.getType(category));
    }

    @Override
    public Observable<GankPageResponse<GankDataEntity>> getData(String category, String type, int page, int count) {
        return subAndObs(mGankService.getData(category, type, page, count));
    }

    @Override
    public Observable<GankRandomResponse<GankDataEntity>> getRandom(String category, String type, int count) {
        return subAndObs(mGankService.getRandom(category, type, count));
    }

    @Override
    public Observable<GankListResponse<Object>> getHot(String hotType, String category, int count) {
        return subAndObs(mGankService.getHot(hotType, category, count));
    }

    @Override
    public Observable<GankResponse<GankArticleEntity>> getArticleDetails(String postId) {
        return subAndObs(mGankService.getArticleDetails(postId));
    }

    @Override
    public Observable<GankListResponse<Object>> getArticleComments(String postId) {
        return subAndObs(mGankService.getArticleComments(postId));
    }

    @Override
    public Observable<GankListResponse<Object>> find(String content, String category, String type, int page, int count) {
        return subAndObs(mGankService.find(content, category, type, page, count));
    }
}
