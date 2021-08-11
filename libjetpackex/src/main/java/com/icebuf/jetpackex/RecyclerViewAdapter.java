package com.icebuf.jetpackex;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableList;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import com.icebuf.jetpackex.util.ReflectUtil;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author IceTang
 * @version 1.0
 * Data: 2020/8/7
 * E-mail：bflyff@hotmail.com
 *
 * 在DataBinding的环境下的万能RecyclerView适配器
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.BindingHolder> {

    public static final String DEFAULT_BR = "androidx.databinding.library.baseAdapters.BR";

    /**
     * DataBinding编译生成BR资源类.
     * @see androidx.databinding.library.baseAdapters.BR
     * <code>你的项目包名.BR></code>
     */
    public static Class<?> BR_CLASS;

    /**
     * 缓存带有注解的类.
     */
    private static volatile Map<Class<?>, RecyclerViewItem> mItemAnnMap;

    private List<?> mItems;

    private ObservableListCallback<Object> mCallback;

    private Map<Object, BindingItem> mItemMap;

    private Object mTag;

    private String mPackageName;

    private ItemListenerHandler mItemListenerHandler = new ItemListenerHandler(this);

    private OnItemBindListener mItemBindListener;

    public RecyclerViewAdapter(@NonNull Context context, @NonNull List<?> items) {
        mPackageName = context.getPackageName();
        this.mItems = items;
    }

    @NonNull
    @Override
    public BindingHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        ViewDataBinding binding = DataBindingUtil.inflate(inflater,
                viewType, parent, false);
        return new BindingHolder(binding);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

        if (mItems instanceof ObservableList) {
            ObservableList<Object> list = ReflectUtil.cast(mItems);;
            if (mCallback == null) {
                mCallback = new ObservableListCallback<>(this);
            }
            list.addOnListChangedCallback(mCallback);
        }
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);

        if (mItems instanceof ObservableList<?>) {
            ObservableList<Object> list = ReflectUtil.cast(mItems);
            list.removeOnListChangedCallback(mCallback);
            mCallback = null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull BindingHolder holder, int position) {
        mItemListenerHandler.bindHolder(holder);
        holder.bindItem(getItem(mItems.get(position)), mTag);
        if (mItemBindListener != null) {
            mItemBindListener.onItemBind(holder.binding, position, mTag);
        }
    }

    public <T> void updateDataSet(List<T> data) {
        if (mItems != data) {
            mItems = data;
            notifyDataSetChanged();
        }
    }

    public void setOnItemBindListener(OnItemBindListener listener) {
        mItemBindListener = listener;
    }

    static class ItemListenerHandler implements View.OnClickListener,
            View.OnLongClickListener, View.OnFocusChangeListener {

        private static final int KEY_HOLDER = -1001;

        private final WeakReference<RecyclerViewAdapter> mAdapter;

        private OnItemClickListener mClickListener;

        private OnItemLongClickListener mLongClickListener;

        private OnItemFocusListener mFocusListener;

        public ItemListenerHandler(RecyclerViewAdapter adapter) {
            mAdapter = new WeakReference<>(adapter);
        }

        public void setOnItemClickListener(OnItemClickListener listener) {
            this.mClickListener = listener;
        }

        public void setOnItemLongClickListener(OnItemLongClickListener listener) {
            this.mLongClickListener = listener;
        }

        public void setOnItemFocusListener(OnItemFocusListener listener) {
            this.mFocusListener = listener;
        }

        public void bindHolder(RecyclerView.ViewHolder holder) {
            View view = holder.itemView;
            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
            view.setOnFocusChangeListener(this);
            view.setTag(KEY_HOLDER, holder);
        }

        public void unbindHolder(RecyclerView.ViewHolder holder) {
            View view = holder.itemView;
            view.setOnClickListener(null);
            view.setOnLongClickListener(null);
            view.setOnFocusChangeListener(null);
            view.setTag(KEY_HOLDER, null);
        }

        @Override
        public void onClick(View v) {
            if (mClickListener != null) {
                int position = getPosition(v);
                mClickListener.onItemClick(mAdapter.get(), v, position);
            }
        }


        private int getPosition(View v) {
            RecyclerView.ViewHolder holder = (RecyclerView.ViewHolder) v.getTag(KEY_HOLDER);
            return holder.getAdapterPosition();
        }


        @Override
        public boolean onLongClick(View v) {
            if (mLongClickListener != null) {
                int position = getPosition(v);
                return mLongClickListener.onItemLongClick(mAdapter.get(), v, position);
            }
            return false;
        }


        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (mFocusListener != null) {
                int position = getPosition(v);
                mFocusListener.onItemFocusChanged(mAdapter.get(), v, position, hasFocus);
            }
        }
    }

    @Override
    public void onViewRecycled(@NonNull BindingHolder holder) {
        super.onViewRecycled(holder);

        mItemListenerHandler.unbindHolder(holder);
    }

    private BindingItem getItem(Object object) {
        if (object instanceof BindingItem) {
            return (BindingItem) object;
        }
        if (object == null) {
            throw new NullPointerException();
        }
        if (mItemMap == null) {
            mItemMap = new HashMap<>();
        }
        BindingItem item = mItemMap.get(object);
        if (item == null) {
            item = getItemFormAnnotation(object, mPackageName);
            if (item == null) {
                throw new RuntimeException("You must implement " + BindingItem.class.getName()
                        + " or use annotation " + RecyclerViewItem.class.getName());
            }
            mItemMap.put(object, item);
        }
        return item;
    }

    @SuppressLint("ResourceType")
    private static BindingItem getItemFormAnnotation(Object object, String pkgName) {
        if (mItemAnnMap == null) {
            mItemAnnMap = new HashMap<>();
        }
        RecyclerViewItem item = mItemAnnMap.get(object.getClass());
        if (item == null) {
            item = ReflectUtil.getAnnotation(object.getClass(), RecyclerViewItem.class);
            if (item == null) {
                return null;
            }
        }
        int layoutId = getLayoutId(item, pkgName);
        int variableId = getVariableId(item.variableId(), item.variable());
        int tagId = getVariableId(item.tagId(), item.tag());
        if (layoutId > 0 && variableId > 0) {
            mItemAnnMap.put(object.getClass(), item);
            return new ObjectItem(object, layoutId, variableId, tagId);
        }
        throw new RuntimeException("Invalid params for annotation "
                + RecyclerViewItem.class.getName()
                + " in " + object.getClass().getName());
    }

    private static int getLayoutId(RecyclerViewItem item, String pkgName) {
        int id = item.layoutId();
        if (id > 0) {
            return item.layoutId();
        }
        if (TextUtils.isEmpty(item.layout())) {
            return 0;
        }
        try {
            Class<?> clazz = Class.forName(pkgName + ".R$layout");
            Field field = clazz.getDeclaredField(item.layout());
            return field.getInt(null);
        } catch (NoSuchFieldException | IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private static int getVariableId(int variableId, String variable) {
        if (variableId > 0) {
            return variableId;
        }
        if (TextUtils.isEmpty(variable)) {
            return 0;
        }
        if (BR_CLASS == null) {
            try {
                BR_CLASS = Class.forName(DEFAULT_BR);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
        try {
            Field field = BR_CLASS.getField(variable);
            if (!field.isAccessible()) {
                field.setAccessible(true);
            }
            return (int) field.get(null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int getItemCount() {
        return mItems == null ? 0 : mItems.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mItems == null ? 0 : getItem(mItems.get(position)).getViewType();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mItemListenerHandler.setOnItemClickListener(listener);
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        mItemListenerHandler.setOnItemLongClickListener(listener);
    }

    public void setOnItemFocusListener(OnItemFocusListener listener) {
        mItemListenerHandler.setOnItemFocusListener(listener);
    }

    public void setTag(Object object) {
        mTag = object;
    }

    public static class BindingHolder extends RecyclerView.ViewHolder {

        ViewDataBinding binding;

        public BindingHolder(@NonNull ViewDataBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bindItem(BindingItem item, Object tag) {
            if (item instanceof ObjectItem) {
                ObjectItem objectItem = (ObjectItem) item;
                binding.setVariable(item.getVariableId(), objectItem.getObject());
            } else {
                binding.setVariable(item.getVariableId(), item);
            }
            if (tag != null) {
                binding.setVariable(item.getTagId(), tag);
            }
        }
    }

    public interface OnItemBindListener {
        void onItemBind(ViewDataBinding bind, int position, Object tag);
    }

    public interface BindingItem {

        int getViewType();

        int getVariableId();

        int getTagId();
    }

    static class ObjectItem implements BindingItem {

        private final Object object;

        private final int viewType;

        private final int variableId;

        private final int tagId;

        public ObjectItem(Object object, int viewType, int variableId, int tagId) {
            this.object = object;
            this.viewType = viewType;
            this.variableId = variableId;
            this.tagId = tagId;
        }

        public Object getObject() {
            return object;
        }

        @Override
        public int getViewType() {
            return viewType;
        }

        @Override
        public int getVariableId() {
            return variableId;
        }

        @Override
        public int getTagId() {
            return tagId;
        }

    }

    static class ObservableListCallback<T> extends
            ObservableList.OnListChangedCallback<ObservableList<T>> {

        private final WeakReference<RecyclerViewAdapter> mAdapter;

        public ObservableListCallback(RecyclerViewAdapter adapter) {
            mAdapter = new WeakReference<>(adapter);
        }

        @Override
        public void onChanged(ObservableList<T> sender) {
            mAdapter.get().notifyDataSetChanged();
        }

        @Override
        public void onItemRangeChanged(ObservableList<T> sender,
                                       int positionStart, int itemCount) {
            RecyclerViewAdapter adapter = mAdapter.get();
            if (adapter != null) {
                adapter.notifyItemRangeChanged(positionStart, itemCount);
            }
        }

        @Override
        public void onItemRangeInserted(ObservableList<T> sender,
                                        int positionStart, int itemCount) {
            RecyclerViewAdapter adapter = mAdapter.get();
            if (adapter != null) {
                adapter.notifyItemRangeInserted(positionStart, itemCount);
            }
        }

        @Override
        public void onItemRangeMoved(ObservableList<T> sender,
                                     int fromPosition, int toPosition, int itemCount) {
            RecyclerViewAdapter adapter = mAdapter.get();
            if (adapter != null) {
                adapter.notifyItemRangeChanged(fromPosition, itemCount);
                adapter.notifyItemRangeChanged(toPosition, itemCount);
            }
        }

        @Override
        public void onItemRangeRemoved(ObservableList<T> sender,
                                       int positionStart, int itemCount) {
            RecyclerViewAdapter adapter = mAdapter.get();
            if (adapter != null) {
                adapter.notifyItemRangeRemoved(positionStart, itemCount);
            }
        }
    }
}
