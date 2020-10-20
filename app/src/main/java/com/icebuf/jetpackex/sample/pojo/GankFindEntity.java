package com.icebuf.jetpackex.sample.pojo;

import java.util.List;

/**
 * @author IceTang
 * @version 1.0
 * Data: 2020/9/21
 * E-mail：bflyff@hotmail.com
 */
public class GankFindEntity {

    /**
     * images : ["http://gank.io/images/2a72ffd3a23e4d6b9075af5b0cd84fd5"]
     * publishedAt : 2018-12-28 00:00:00
     * author : 李金山
     * likeCount : 0
     * stars : 3
     * title : android-particles
     * type : Android
     * url : https://github.com/ibrahimsn98/android-particles
     * createdAt : 2018-12-28 07:28:21
     * likeCounts : 0
     * markdown : 适用于Android的粒子动画库。
     Particle animation library for Android

     # Example

     ![](https://gank.io/images/23676e32cf1b4abc93ce2c4e125e42b3)

     # Setup

     ``` gradle
     allprojects {
     repositories {
     ...
     maven { url 'https://jitpack.io' }
     }
     }
     dependencies {
     implementation 'com.github.ibrahimsn98:android-particles:1.7'
     }
     ```

     # Attributions

     ``` xml
     <me.ibrahimsn.particle.particleview android:id="@+id/particleView" android:layout_width="match_parent" android:layout_height="match_parent" app:particlecount="20" app:minparticleradius="5" app:maxparticleradius="12" app:particlecolor="@android:color/white" app:backgroundcolor="@android:color/holo_red_light"></me.ibrahimsn.particle.particleview>
     ```

     # Usage

     ``` kotlin
     class MainActivity : AppCompatActivity() {

     private lateinit var particleView: ParticleView

     override fun onCreate(savedInstanceState: Bundle?) {
     super.onCreate(savedInstanceState)
     setContentView(R.layout.activity_main)

     particleView = findViewById(R.id.particleView)
     }

     override fun onResume() {
     super.onResume()
     particleView.resume()
     }

     override fun onPause() {
     super.onPause()
     particleView.pause()
     }
     }
     ```

     # Inspired From

     Thanks to [VincentGarreau](https://github.com/VincentGarreau) for sharing that awesome [javascript library](https://github.com/VincentGarreau/particles.js)
     * _id : 5e51fb2545daba871ab7b607
     * category : GanHuo
     * views : 44
     * desc : 适用于Android的粒子动画库。
     * status : 1
     * updatedAt : 2020-03-30 22:50:44
     */
    private List<String> images;
    private String publishedAt;
    private String author;
    private int likeCount;
    private int stars;
    private String title;
    private String type;
    private String url;
    private String createdAt;
    private int likeCounts;
    private String markdown;
    private String _id;
    private String category;
    private int views;
    private String desc;
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

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
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

    public int getLikeCount() {
        return likeCount;
    }

    public int getStars() {
        return stars;
    }

    public String getTitle() {
        return title;
    }

    public String getType() {
        return type;
    }

    public String getUrl() {
        return url;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public int getLikeCounts() {
        return likeCounts;
    }

    public String getMarkdown() {
        return markdown;
    }

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

    public int getStatus() {
        return status;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }
}
