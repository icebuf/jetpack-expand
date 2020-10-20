package com.icebuf.jetpackex.sample.fragment;

import android.content.Context;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;
import androidx.databinding.ObservableField;
import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.icebuf.jetpackex.sample.plugins.GrammarLocatorDef;
import com.icebuf.jetpackex.sample.pojo.GankArticleEntity;
import com.icebuf.jetpackex.sample.repo.GankRepository;

import dagger.hilt.android.qualifiers.ApplicationContext;
import io.noties.markwon.Markwon;
import io.noties.markwon.core.CorePlugin;
import io.noties.markwon.html.HtmlPlugin;
import io.noties.markwon.image.glide.GlideImagesPlugin;
import io.noties.markwon.movement.MovementMethodPlugin;
import io.noties.markwon.syntax.Prism4jThemeDefault;
import io.noties.markwon.syntax.SyntaxHighlightPlugin;
import io.noties.prism4j.Prism4j;

/**
 * @author IceTang
 * @version 1.0
 * Data: 2020/9/29
 * E-mail：bflyff@hotmail.com
 */
public class ArticleViewModel extends ViewModel {

    private static final int TAG_MARKDOWN_TEXT = -1011;

    private ObservableField<CharSequence> mContentText = new ObservableField<>();

    private MutableLiveData<GankArticleEntity> mArticle = new MutableLiveData<>();

    GankRepository repository;

    private Markwon markwon;

    @ViewModelInject
    public ArticleViewModel(@ApplicationContext Context context, GankRepository repo) {
        repository = repo;

        Prism4j prism4j = new Prism4j(new GrammarLocatorDef());
        SyntaxHighlightPlugin plugin = SyntaxHighlightPlugin.create(
                prism4j, Prism4jThemeDefault.create(), "java");
        markwon = Markwon.builder(context)
                .usePlugin(CorePlugin.create())
                .usePlugin(MovementMethodPlugin.create(ScrollingMovementMethod.getInstance()))
                .usePlugin(GlideImagesPlugin.create(context))
                .usePlugin(HtmlPlugin.create())
                .usePlugin(plugin)
                .build();
    }


    public void loadArticle(String id) {
        repository.loadArticle(id, mArticle);
    }

    public ObservableField<CharSequence> getContentText() {
        return mContentText;
    }

    public Markwon getMarkwon() {
        return markwon;
    }

    @BindingAdapter(value = {"android:text", "useMarkdown"})
    public static void setText(TextView view, ObservableField<CharSequence> field, Markwon markwon) {
        CharSequence value = field.get();
        value = value == null ? "" : value;
        if(markwon != null && value instanceof String) {
            markwon.setMarkdown(view, (String) value);
        } else {
            view.setText(value);
        }
    }

    public LiveData<GankArticleEntity> getArticle() {
        return mArticle;
    }

    public void setContent(CharSequence content) {
        mContentText.set(content);
    }
}
