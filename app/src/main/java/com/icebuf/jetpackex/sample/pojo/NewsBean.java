package com.icebuf.jetpackex.sample.pojo;

import com.icebuf.jetpackex.RecyclerViewItem;
import com.icebuf.jetpackex.sample.R;

import java.util.Objects;

@RecyclerViewItem(layoutId = R.layout.item_news, variable = "news")
public class NewsBean {
    /**
     * ctime : 2020-08-15 21:45:06
     * title : 港媒：林郑月娥删除剑桥名誉院士身份
     * description : 据港媒报道，香港特首林郑月娥今日（15日）通知行政会议秘书处，删除她在行会个人利益登记册上，有关英国剑桥大学沃尔森学院名誉院士身份的项目。
     * picUrl : http://p26-tt.byteimg.com/img/pgc-image/921f3cc83c9f4868a20893f90c34b581~tplv-tt-cs0:1080:914.jpg
     * url : http://toutiao.com/group/6861208325233574411/
     * source : 环球时报
     */

    private String ctime;
    private String title;
    private String description;
    private String picUrl;
    private String url;
    private String source;

    public String getCtime() {
        return ctime;
    }

    public void setCtime(String ctime) {
        this.ctime = ctime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NewsBean newsBean = (NewsBean) o;
        return ctime.equals(newsBean.ctime) &&
                title.equals(newsBean.title) &&
                Objects.equals(description, newsBean.description) &&
                Objects.equals(picUrl, newsBean.picUrl) &&
                Objects.equals(url, newsBean.url) &&
                source.equals(newsBean.source);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ctime, title, description, picUrl, url, source);
    }

    @Override
    public String toString() {
        return "NewsBean{" +
                "ctime='" + ctime + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", picUrl='" + picUrl + '\'' +
                ", url='" + url + '\'' +
                ", source='" + source + '\'' +
                '}';
    }
}