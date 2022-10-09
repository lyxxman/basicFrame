package com.frame.module.demo.activity.list;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class GridSpaceItemDecoration extends RecyclerView.ItemDecoration {
    private int mRowSpacing;//行间距
    private int mColumnSpacing;// 列间距

    /**
     * @param rowSpacing    行间距
     * @param columnSpacing 列间距
     */
    public GridSpaceItemDecoration(int rowSpacing, int columnSpacing) {
        this.mRowSpacing = rowSpacing;
        this.mColumnSpacing = columnSpacing;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        RecyclerView.LayoutManager manager = parent.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            int spanCount = ((GridLayoutManager) manager).getSpanCount();
            int position = parent.getChildAdapterPosition(view); // 获取view 在adapter中的位置。
            int column = position % spanCount; // view 所在的列
            outRect.left = column * mColumnSpacing / spanCount; // column * (列间距 * (1f / 列数))
            outRect.right = mColumnSpacing - (column + 1) * mColumnSpacing / spanCount; // 列间距 - (column + 1) * (列间距 * (1f /列数))
            // 如果position > 行数，说明不是在第一行，则不指定行高，其他行的上间距为 top=mRowSpacing
            if (position >= spanCount) {
                outRect.top = mRowSpacing; // item top
            }
        }

    }
}
