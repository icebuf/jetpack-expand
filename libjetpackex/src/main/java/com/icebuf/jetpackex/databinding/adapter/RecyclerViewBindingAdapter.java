package com.icebuf.jetpackex.databinding.adapter;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;
import androidx.databinding.BindingMethod;
import androidx.databinding.BindingMethods;
import androidx.databinding.InverseBindingListener;
import androidx.databinding.InverseBindingMethod;
import androidx.databinding.InverseBindingMethods;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableInt;
import androidx.databinding.ObservableList;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.icebuf.jetpackex.OnItemClickListener;
import com.icebuf.jetpackex.OnItemLongClickListener;
import com.icebuf.jetpackex.databinding.RecyclerViewAdapter;
import com.icebuf.jetpackex.util.ReflectUtil;

import java.util.List;

/**
 * @author IceTang
 * @version 1.0
 * Data: 2020/8/7
 * E-mail：bflyff@hotmail.com
 *
 */
@BindingMethods({
        @BindingMethod(type = RecyclerView.class, attribute = "adapter",
                method = "setAdapter"),
        @BindingMethod(type = RecyclerView.class, attribute = "itemAnimator",
                method = "setItemAnimator"),
        @BindingMethod(type = RecyclerView.class, attribute = "childDrawingOrder",
                method = "setChildDrawingOrderCallback"),
        @BindingMethod(type = RecyclerView.class, attribute = "clipToPadding",
                method = "setClipToPadding"),
        @BindingMethod(type = RecyclerView.class, attribute = "edgeEffectFactory",
                method = "setEdgeEffectFactory"),
        @BindingMethod(type = RecyclerView.class, attribute = "hasFixedSize",
                method = "setHasFixedSize"),
        @BindingMethod(type = RecyclerView.class, attribute = "itemViewCacheSize",
                method = "setItemViewCacheSize"),
        @BindingMethod(type = RecyclerView.class, attribute = "nestedScrollingEnabled",
                method = "setNestedScrollingEnabled"),
        @BindingMethod(type = RecyclerView.class, attribute = "onFlingListener",
                method = "setOnFlingListener"),
        @BindingMethod(type = RecyclerView.class, attribute = "preserveFocusAfterLayout",
                method = "setPreserveFocusAfterLayout"),
        @BindingMethod(type = RecyclerView.class, attribute = "recycledViewPool",
                method = "setRecycledViewPool"),
        @BindingMethod(type = RecyclerView.class, attribute = "scrollingTouchSlop",
                method = "setScrollingTouchSlop"),
        @BindingMethod(type = RecyclerView.class, attribute = "recyclerListener",
                method = "setRecyclerListener"),
        @BindingMethod(type = RecyclerView.class, attribute = "viewCacheExtension",
                method = "setViewCacheExtension"),
        @BindingMethod(type = RecyclerView.class, attribute = "accessibilityDelegateCompat",
                method = "setAccessibilityDelegateCompat"),
        @BindingMethod(type = RecyclerView.class, attribute = "scrollTo",
                method = "scrollToPosition"),
})
@InverseBindingMethods({
        @InverseBindingMethod(type = RecyclerView.class, attribute = "layoutManager",
                method = "getLayoutManager"),
        @InverseBindingMethod(type = RecyclerView.class, attribute = "adapter",
                method = "getAdapter"),
        @InverseBindingMethod(type = RecyclerView.class, attribute = "itemAnimator",
                method = "getItemAnimator"),
        @InverseBindingMethod(type = RecyclerView.class, attribute = "childDrawingOrder",
                method = "getChildDrawingOrderCallback"),
        @InverseBindingMethod(type = RecyclerView.class, attribute = "clipToPadding",
                method = "getClipToPadding"),
        @InverseBindingMethod(type = RecyclerView.class, attribute = "edgeEffectFactory",
                method = "getEdgeEffectFactory"),
        @InverseBindingMethod(type = RecyclerView.class, attribute = "hasFixedSize",
                method = "getHasFixedSize"),
        @InverseBindingMethod(type = RecyclerView.class, attribute = "itemViewCacheSize",
                method = "getItemViewCacheSize"),
        @InverseBindingMethod(type = RecyclerView.class, attribute = "nestedScrollingEnabled",
                method = "getNestedScrollingEnabled"),
        @InverseBindingMethod(type = RecyclerView.class, attribute = "onFlingListener",
                method = "getOnFlingListener"),
        @InverseBindingMethod(type = RecyclerView.class, attribute = "preserveFocusAfterLayout",
                method = "getPreserveFocusAfterLayout"),
        @InverseBindingMethod(type = RecyclerView.class, attribute = "recycledViewPool",
                method = "getRecycledViewPool"),
        @InverseBindingMethod(type = RecyclerView.class, attribute = "scrollingTouchSlop",
                method = "getScrollingTouchSlop"),
        @InverseBindingMethod(type = RecyclerView.class, attribute = "recyclerListener",
                method = "getRecyclerListener"),
        @InverseBindingMethod(type = RecyclerView.class, attribute = "viewCacheExtension",
                method = "getViewCacheExtension"),
        @InverseBindingMethod(type = RecyclerView.class, attribute = "accessibilityDelegateCompat",
                method = "getAccessibilityDelegateCompat"),
})
public class RecyclerViewBindingAdapter {

    private static final int KEY_ITEM_CLICK = -1000;
    private static final int KEY_ITEM_LONG_CLICK = -1001;

    /**
     * 将数据集直接通过{@link RecyclerViewAdapter}绑定到RecyclerView
     * 案例如下：
     * 在viewModel中定义一个{@link ObservableArrayList}类型到strings
     * <code>
     *  private ObservableArrayList<String> strings = new ObservableArrayList<>();
     *
     *  public ObservableArrayList<String> getStrings() {
     *      return strings;
     *  }
     * </code>
     * 在xml布局文件中使用strings
     * <code>
     *  <variable
     *      name="viewModel"
     *      type="com.xxx.MainViewModel" />
     *
     *  <androidx.recyclerview.widget.RecyclerView
     *      app:adapterDataSet="@{viewModel.strings}" />
     * </code>
     *
     * @param view 要绑定的RecyclerView
     * @param data 数据集合
     * @param <T> 数据类型
     */
    @BindingAdapter(value = "adapterDataSet")
    public static <T> void setAdapterDataSet(RecyclerView view, List<T> data) {
        RecyclerView.Adapter<?> adapter = view.getAdapter();
        if(adapter instanceof RecyclerViewAdapter) {
            RecyclerViewAdapter recyclerViewAdapter = (RecyclerViewAdapter) adapter;
            recyclerViewAdapter.updateDataSet(data);
        } else {
            RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(data);
            OnItemClickListener clickListener = (OnItemClickListener) view.getTag(KEY_ITEM_CLICK);
            OnItemLongClickListener longClickListener = (OnItemLongClickListener) view.getTag(KEY_ITEM_LONG_CLICK);
            recyclerViewAdapter.setOnItemClickListener(clickListener);
            recyclerViewAdapter.setOnItemLongClickListener(longClickListener);
            view.setAdapter(recyclerViewAdapter);
        }
    }

    @BindingAdapter(value = "dropUpScrollListener")
    public static <T> void setDropUpScrollListener(RecyclerView view, String clazzName) {
        RecyclerView.OnScrollListener listener = ReflectUtil.newInstance(clazzName);
        view.addOnScrollListener(listener);
    }

    @BindingAdapter(value = {"dropDownScrollListener", "dropDownAttrScrollListener"}, requireAll = false)
    public static <T> void setDropDownScrollListener(RecyclerView view,
                                                     RecyclerView.OnScrollListener listener,
                                                     InverseBindingListener attrChange) {

        if (attrChange == null) {
            view.addOnScrollListener(listener);
        } else {
            view.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                    if (listener != null) {
                        listener.onScrollStateChanged(recyclerView, newState);
                    }
                    attrChange.onChange();
                }

                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    if (listener != null) {
                        listener.onScrolled(recyclerView, dx, dy);
                    }
                    attrChange.onChange();
                }
            });
        }
    }

    @BindingAdapter(value = "adapterTag")
    public static void setAdapterViewModel(RecyclerView view, Object object) {
        RecyclerView.Adapter<?> adapter = view.getAdapter();
        if(adapter instanceof RecyclerViewAdapter) {
            RecyclerViewAdapter recyclerViewAdapter = (RecyclerViewAdapter) adapter;
            recyclerViewAdapter.setTag(object);
        }
    }

    @BindingAdapter(value = "itemAnimator", requireAll = false)
    public static <T> void setItemAnimator(RecyclerView view, String className) {
        try {
            Class<?> clazz = Class.forName(className);
            if(clazz.isAssignableFrom(RecyclerView.ItemAnimator.class)) {
                RecyclerView.ItemAnimator animator = (RecyclerView.ItemAnimator) clazz.newInstance();
                view.setItemAnimator(animator);
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @BindingAdapter(value = "onItemClick", requireAll = false)
    public static <T> void setOnItemClick(RecyclerView view, OnItemClickListener listener) {
        RecyclerView.Adapter<?> adapter = view.getAdapter();
        if(adapter == null) {
            view.setTag(KEY_ITEM_CLICK, listener);
        } else if(adapter instanceof RecyclerViewAdapter) {
            RecyclerViewAdapter recyclerViewAdapter = (RecyclerViewAdapter) adapter;
            recyclerViewAdapter.setOnItemClickListener(listener);
        }
    }

    @BindingAdapter(value = "onItemLongClick", requireAll = false)
    public static <T> void setOnItemLongClick(RecyclerView view, OnItemLongClickListener listener) {
        RecyclerView.Adapter<?> adapter = view.getAdapter();
        if(adapter == null) {
            view.setTag(KEY_ITEM_LONG_CLICK, listener);
        } else if(adapter instanceof RecyclerViewAdapter) {
            RecyclerViewAdapter recyclerViewAdapter = (RecyclerViewAdapter) adapter;
            recyclerViewAdapter.setOnItemLongClickListener(listener);
        }
    }

    /**
     * 绑定列表底部与视图同步
     * 当{@link RecyclerView}的标签<code>onScrollToPosition</code>的值设置为
     * {@link ObservableList}的时候，用来监听当{@link ObservableList}大小发生变化时可以
     * 自动当滑动到对应位置
     *
     * @param view {@link RecyclerView}
     * @param observableList {@link ObservableList}
     * @param <T> 数组元素类型
     */
    @BindingAdapter(value = "onScrollToPosition", requireAll = false)
    public static <T> void onScrollToPosition(RecyclerView view, ObservableList<T> observableList) {
        onScrollToPosition(view, observableList.size() - 1);
    }

    @BindingAdapter(value = "onScrollToPosition", requireAll = false)
    public static void onScrollToPosition(RecyclerView view, ObservableInt observable) {
        onScrollToPosition(view, observable.get());
    }

    @BindingAdapter(value = "onScrollToPosition", requireAll = false)
    public static <T extends List<?>> void onScrollToPosition(RecyclerView view, LiveData<T> liveData) {
        if(liveData.getValue() != null) {
            onScrollToPosition(view, liveData.getValue().size() - 1);
        }
    }

    @BindingAdapter(value = "onSmoothScrollToPosition", requireAll = false)
    public static <T> void onSmoothScrollToPosition(RecyclerView view, ObservableList<T> observableList) {
        onSmoothScrollToPosition(view, observableList.size() - 1);
    }

    @BindingAdapter(value = "onSmoothScrollToPosition", requireAll = false)
    public static void onSmoothScrollToPosition(RecyclerView view, ObservableInt observable) {
        onSmoothScrollToPosition(view, observable.get());
    }

    @BindingAdapter(value = "onSmoothScrollToPosition", requireAll = false)
    public static <T extends List<?>> void onSmoothScrollToPosition(RecyclerView view, LiveData<T> liveData) {
        if(liveData.getValue() != null) {
            onSmoothScrollToPosition(view, liveData.getValue().size() - 1);
        }
    }

    private static void onScrollToPosition(RecyclerView view, int position) {
        RecyclerView.Adapter<?> adapter = view.getAdapter();
        if(adapter != null) {
            int pos = Math.max(0, Math.min(position, adapter.getItemCount() - 1));
            view.scrollToPosition(pos);
        }
    }

    private static void onSmoothScrollToPosition(RecyclerView view, int position) {
        RecyclerView.Adapter<?> adapter = view.getAdapter();
        if(adapter != null) {
            int pos = Math.max(0, Math.min(position, adapter.getItemCount() - 1));
            view.smoothScrollToPosition(pos);
        }
    }
}
