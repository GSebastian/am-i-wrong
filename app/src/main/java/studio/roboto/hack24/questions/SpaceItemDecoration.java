package studio.roboto.hack24.questions;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by jordan on 18/03/17.
 */

public class SpaceItemDecoration extends RecyclerView.ItemDecoration {
    private int space;
    private int bottomSpace = 0;

    public SpaceItemDecoration(int bottomSpace) {
        this.space = 0;
        this.bottomSpace = bottomSpace;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {

        int childCount = parent.getChildCount();
        final int itemPosition = parent.getChildAdapterPosition(view);
        final int itemCount = state.getItemCount();

        outRect.left = space;
        outRect.right = space;
        outRect.bottom = space;
        outRect.top = space;

        if (itemCount > 0 && itemPosition == itemCount - 1) {
            outRect.bottom = bottomSpace;
        }
    }
}
