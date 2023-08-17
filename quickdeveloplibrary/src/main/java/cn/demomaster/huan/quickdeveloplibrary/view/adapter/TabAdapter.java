
package cn.demomaster.huan.quickdeveloplibrary.view.adapter;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import cn.demomaster.huan.quickdeveloplibrary.widget.ScrollableTabView;

public abstract class TabAdapter implements TabAdapterInterFace{

    private final ScrollableTabView.AdapterDataObservable mObservable = new ScrollableTabView.AdapterDataObservable();
    public final void notifyDataSetChanged() {
        mObservable.notifyChanged();
    }

    public OnSelectChangeListener mOnSelectChangeListener;
    public void setOnSelectChangeListener(OnSelectChangeListener onSelectChangeListener) {
        this.mOnSelectChangeListener = onSelectChangeListener;
    }
    int selectIndex;
    public boolean onSelectedChange(ArrayList<View> views, int position, View view, boolean isSelected) {
        if(mOnSelectChangeListener!=null&&selectIndex != position){
            mOnSelectChangeListener.onSelectChange(position,view);
        }
        selectIndex = position;
        return false;
    }

    public void registerAdapterDataObserver(@NonNull AdapterDataObserver observer) {
        mObservable.registerObserver(observer);
    }
    
    public void unregisterAdapterDataObserver(@NonNull AdapterDataObserver observer) {
        mObservable.unregisterObserver(observer);
    }

    /**
     * Observer base class for watching changes to an {@link RecyclerView.Adapter}.
     * See {@link RecyclerView.Adapter#registerAdapterDataObserver(RecyclerView.AdapterDataObserver)}.
     */
    public abstract static class AdapterDataObserver {
        public void onChanged() {
            // Do nothing
        }

        public void onItemRangeChanged(int positionStart, int itemCount) {
            // do nothing
        }

        public void onItemRangeChanged(int positionStart, int itemCount, @Nullable Object payload) {
            // fallback to onItemRangeChanged(positionStart, itemCount) if app
            // does not override this method.
            onItemRangeChanged(positionStart, itemCount);
        }

        public void onItemRangeInserted(int positionStart, int itemCount) {
            // do nothing
        }

        public void onItemRangeRemoved(int positionStart, int itemCount) {
            // do nothing
        }

        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            // do nothing
        }
    }

    public interface OnSelectChangeListener {
        void onSelectChange(int position,View view);
    }
}
