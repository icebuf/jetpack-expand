package com.icebuf.jetpackex.sample.pojo;

import androidx.databinding.library.baseAdapters.BR;

import com.icebuf.jetpackex.RecyclerViewItem;
import com.icebuf.jetpackex.sample.R;

import java.util.Objects;

/**
 * @author IceTang
 * @version 1.0
 * Data: 2020/9/21
 * E-mail：bflyff@hotmail.com
 */
@RecyclerViewItem(layoutId = R.layout.item_banner, variableId = BR.item)
public class GankBannerEntity {

    /**
     * image : http://gank.io/images/cfb4028bfead41e8b6e34057364969d1
     * title : 干货集中营新版更新
     * url : https://gank.io/migrate_progress
     */
    private String image;
    private String title;
    private String url;

    public void setImage(String image) {
        this.image = image;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImage() {
        return image;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GankBannerEntity that = (GankBannerEntity) o;
        return image.equals(that.image) &&
                Objects.equals(title, that.title) &&
                url.equals(that.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(image, title, url);
    }
}
