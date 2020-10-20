package com.icebuf.jetpackex.sample.pojo;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.icebuf.jetpackex.sample.repo.converter.GankConverter;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author IceTang
 * @version 1.0
 * Data: 2020/9/21
 * E-mail：bflyff@hotmail.com
 */
@Entity
@TypeConverters(GankConverter.class)
public class GankArticleEntity {

    /**
     * images : ["http://gank.io/images/624ade89f93f421b8d4e8fafd86b1d8d"]
     * publishedAt : 2020-03-25 08:00:00
     * author : 鸢媛
     * index : 35
     * likeCount : 0
     * stars : 1
     * originalAuthor :
     * title : 第35期
     * type : Girl
     * content : 这世界总有人在笨拙地爱着你，想把全部的温柔都给你。
     * url : http://gank.io/images/624ade89f93f421b8d4e8fafd86b1d8d
     * tags : []
     * createdAt : 2020-03-25 08:00:00
     * isOriginal : true
     * license :
     * likeCounts : 1
     * markdown :
     * _id : 5e777432b8ea09cade05263f
     * category : Girl
     * views : 1310
     * desc : 这世界总有人在笨拙地爱着你，想把全部的温柔都给你。
     * likes : ["DBRef('users', ObjectId('5b6ce9c89d21226f4e09c779'))"]
     * status : 1
     * updatedAt : 2020-03-25 08:00:00
     */
    private List<String> images;
    private String publishedAt;
    private String author;
    private int index;
    private int likeCount;
    private int stars;
    private String originalAuthor;
    private String title;
    private String type;
    private String content;
    private String url;
    private List<String> tags;
    private String createdAt;
    private boolean isOriginal;
    private String license;
    private int likeCounts;
    private String markdown;
    @NonNull
    @PrimaryKey
    private String _id = "";
    private String category;
    private int views;
    private String desc;
    private List<String> likes;
    private int status;
    private String updatedAt;

    public void setImages(List<String> images) {
        this.images = images;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    public void setOriginalAuthor(String originalAuthor) {
        this.originalAuthor = originalAuthor;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setIsOriginal(boolean isOriginal) {
        this.isOriginal = isOriginal;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public void setLikeCounts(int likeCounts) {
        this.likeCounts = likeCounts;
    }

    public void setMarkdown(String markdown) {
        this.markdown = markdown;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setLikes(List<String> likes) {
        this.likes = likes;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<String> getImages() {
        return images;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public String getAuthor() {
        return author;
    }

    public int getIndex() {
        return index;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public int getStars() {
        return stars;
    }

    public String getOriginalAuthor() {
        return originalAuthor;
    }

    public String getTitle() {
        return title;
    }

    public String getType() {
        return type;
    }

    public String getContent() {
        return content;
    }

    public String getUrl() {
        return url;
    }

    public List<String> getTags() {
        return tags;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public boolean isIsOriginal() {
        return isOriginal;
    }

    public String getLicense() {
        return license;
    }

    public int getLikeCounts() {
        return likeCounts;
    }

    public String getMarkdown() {
        return markdown;
    }

    @NotNull
    public String get_id() {
        return _id;
    }

    public String getCategory() {
        return category;
    }

    public int getViews() {
        return views;
    }

    public String getDesc() {
        return desc;
    }

    public List<String> getLikes() {
        return likes;
    }

    public int getStatus() {
        return status;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }
}
