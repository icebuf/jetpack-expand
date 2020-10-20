package com.icebuf.jetpackex.sample.pojo;

/**
 * @author IceTang
 * @version 1.0
 * Data: 2020/9/21
 * E-mail：bflyff@hotmail.com
 */
public class GankTypeEntity {

    /**
     * coverImageUrl : http://gank.io/images/b9f867a055134a8fa45ef8a321616210
     * _id : 5e59ec146d359d60b476e621
     * title : Android
     * type : Android
     * desc : Always deliver more than expected.（Larry Page）
     */
    private String coverImageUrl;
    private String _id;
    private String title;
    private String type;
    private String desc;

    public void setCoverImageUrl(String coverImageUrl) {
        this.coverImageUrl = coverImageUrl;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getCoverImageUrl() {
        return coverImageUrl;
    }

    public String get_id() {
        return _id;
    }

    public String getTitle() {
        return title;
    }

    public String getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }
}
