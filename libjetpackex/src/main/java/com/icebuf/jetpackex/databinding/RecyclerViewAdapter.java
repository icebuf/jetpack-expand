package com.icebuf.jetpackex.databinding;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableList;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;

import com.icebuf.jetpackex.util.ReflectUtil;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
     * DataBinding编译生成BR资源类
     * @see androidx.databinding.library.baseAdapters.BR
     * <code>你的项目包名.BR></>
     */
    public static Class<?> BR_CLASS;

    /**
     * 缓存带有注解的类
     */
    private volatile static Map<Class<?>, RecyclerViewItem> mItemAnnMap;

    private List<?> mItems;

    private ObservableListCallback<Object> mCallback;

    private Map<Object, BindingItem> mItemMap;

    private ViewModel mViewModel;

    private ItemClickHandler mItemClickHandler = new ItemClickHandler(this);

    public RecyclerViewAdapter(List<?> items) {
        this.mItems = items;
    }

    @NonNull
    @Override
    public BindingHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ViewDataBinding binding = DataBindingUtil.inflate(inflater, viewType, parent, false);
        return new BindingHolder(binding);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

        if(mItems instanceof ObservableList) {
            ObservableList<Object> list = (ObservableList<Object>) mItems;
            if(mCallback == null) {
                mCallback = new ObservableListCallback<>(this);
            }
            list.addOnListChangedCallback(mCallback);
        }
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);

        if(mItems instanceof ObservableList) {
            ObservableList<Object> list = (ObservableList<Object>) mItems;
            list.removeOnListChangedCallback(mCallback);
            mCallback = null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull BindingHolder holder, int position) {
        mItemClickHandler.bindHolder(holder);
        holder.bindItem(getItem(mItems.get(position)), mViewModel);
    }

    static class ItemClickHandler implements View.OnClickListener, View.OnLongClickListener {

        private static final int KEY_POSITION = -1001;

        private WeakReference<RecyclerViewAdapter> mAdapter;

        private OnItemClickListener mClickListener;

        private OnItemLongClickListener mLongClickListener;

        public ItemClickHandler(RecyclerViewAdapter adapter) {
            mAdapter = new WeakReference<>(adapter);
        }

        public void setOnItemClickListener(OnItemClickListener mClickListener) {
            this.mClickListener = mClickListener;
        }

        public void setOnItemLongClickListener(OnItemLongClickListener mLongClickListener) {
            this.mLongClickListener = mLongClickListener;
        }

        public void bindHolder(RecyclerView.ViewHolder holder) {
            View view = holder.itemView;
            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
            view.setTag(KEY_POSITION, holder.getAdapterPosition());
        }

        public void unbindHolder(RecyclerView.ViewHolder holder) {
            View view = holder.itemView;
            view.setOnClickListener(null);
            view.setOnLongClickListener(null);
        }

        @Override
        public void onClick(View v) {
            if(mClickListener != null) {
                int position = (int) v.getTag(KEY_POSITION);
                mClickListener.onItemClick(mAdapter.get(), v, position);
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if(mLongClickListener != null) {
                int position = (int) v.getTag(KEY_POSITION);
                return mLongClickListener.onItemLongClick(mAdapter.get(), v, position);
            }
            return false;
        }
    }

    @Override
    public void onViewRecycled(@NonNull BindingHolder holder) {
        super.onViewRecycled(holder);

        mItemClickHandler.unbindHolder(holder);
    }

    private BindingItem getItem(Object object) {
        if(object instanceof BindingItem) {
            return (BindingItem) object;
        }
        if(object == null) {
            throw new NullPointerException();
        }
        if(mItemMap == null) {
            mItemMap = new HashMap<>();
        }
        BindingItem item = mItemMap.get(object);
        if(item == null) {
            item = getItemFormAnnotation(object);
            if(item == null) {
                throw new RuntimeException("You must implement " + BindingItem.class.getName()
                        + " or use annotation " + RecyclerViewItem.class.getName());
            }
            mItemMap.put(object, item);
        }
        return item;
    }

    @SuppressLint("ResourceType")
    private static BindingItem getItemFormAnnotation(Object object) {
        if(mItemAnnMap == null) {
            mItemAnnMap = new HashMap<>();
        }
        RecyclerViewItem item = mItemAnnMap.get(object.getClass());
        if(item == null) {
            item = ReflectUtil.getAnnotation(object.getClass(), RecyclerViewItem.class);
            if(item == null) {
                return null;
            }
        }
        if(item.layoutId() > 0 && item.variableId() > 0) {
            mItemAnnMap.put(object.getClass(), item);
            return new ObjectItem(object, item.layoutId(), item.variableId(), item.viewModelId());
        }
        if(item.layoutId() > 0 &&!TextUtils.isEmpty(item.variable())) {
            int variableId = getVariableId(item.variable());
            if(variableId > 0) {
                mItemAnnMap.put(object.getClass(), item);
                return new ObjectItem(object, item.layoutId(), variableId, item.viewModelId());
            }
        }
        throw new RuntimeException("Invalid params for annotation "
                + RecyclerViewItem.class.getName()
                + " in " + object.getClass().getName());
    }

    private static int getVariableId(String variable) {
        if(BR_CLASS == null) {
            try {
                BR_CLASS = Class.forName(DEFAULT_BR);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
        try {
            Field field = BR_CLASS.getField(variable);
            if(!field.isAccessible()) {
                field.setAccessible(true);
            }
            return (int) field.get(null);
        } catch (NoSuchFieldException | IllegalAccessException ignore) {
        }
        return 0;
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(mItems.get(position)).getViewType();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mItemClickHandler.setOnItemClickListener(listener);
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        mItemClickHandler.setOnItemLongClickListener(listener);
    }

    public void setViewModel(ViewModel viewModel) {
        mViewModel = viewModel;
    }

    static class BindingHolder extends RecyclerView.ViewHolder {

        ViewDataBinding binding;

        public BindingHolder(@NonNull ViewDataBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bindItem(BindingItem item, ViewModel viewModel) {
            if(item instanceof ObjectItem) {
                ObjectItem objectItem = (ObjectItem) item;
                binding.setVariable(item.getVariableId(), objectItem.getObject());
            } else {
                binding.setVariable(item.getVariableId(), item);
            }
            if(viewModel != null) {
                binding.setVariable(item.getViewModelId(), viewModel);
            }
        }
    }

    public interface BindingItem {

        int getViewType();

        int getVariableId();

        int getViewModelId();
    }

    static class ObjectItem implements BindingItem {

        private Object object;

        private int viewType;

        private int variableId;

        private int viewModelId;

        public ObjectItem(Object object, int viewType, int variableId, int viewModelId) {
            this.object = object;
            this.viewType = viewType;
            this.variableId = variableId;
            this.viewModelId = viewModelId;
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
        public int getViewModelId() {
            return viewModelId;
        }

    }

    static class ObservableListCallback<T> extends
            ObservableList.OnListChangedCallback<ObservableList<T>> {

        private RecyclerViewAdapter mAdapter;

        public ObservableListCallback(RecyclerViewAdapter adapter) {
            this.mAdapter = adapter;
        }

        @Override
        public void onChanged(ObservableList<T> sender) {
            mAdapter.notifyDataSetChanged();
        }

        @Override
        public void onItemRangeChanged(ObservableList<T> sender,
                                       int positionStart, int itemCount) {
            mAdapter.notifyItemRangeChanged(positionStart, itemCount);
        }

        @Override
        public void onItemRangeInserted(ObservableList<T> sender,
                                        int positionStart, int itemCount) {
            mAdapter.notifyItemRangeInserted(positionStart, itemCount);
        }

        @Override
        public void onItemRangeMoved(ObservableList<T> sender,
                                     int fromPosition, int toPosition, int itemCount) {
            mAdapter.notifyItemRangeChanged(fromPosition, itemCount);
            mAdapter.notifyItemRangeChanged(toPosition, itemCount);
        }

        @Override
        public void onItemRangeRemoved(ObservableList<T> sender,
                                       int positionStart, int itemCount) {
            mAdapter.notifyItemRangeRemoved(positionStart, itemCount);
        }
    }
}
