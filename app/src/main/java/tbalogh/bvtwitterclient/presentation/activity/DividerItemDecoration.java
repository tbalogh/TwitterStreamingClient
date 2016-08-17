package tbalogh.bvtwitterclient.presentation.activity;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import tbalogh.bvtwitterclient.R;

/**
 * Created by tbalogh on 16/08/16.
 */
public class DividerItemDecoration extends RecyclerView.ItemDecoration {
    private static final int DIVIDER_WIDTH = 4;

    private final Drawable divider;

    public DividerItemDecoration(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            this.divider = new ColorDrawable(context.getColor(R.color.colorDivider));
        } else {
            this.divider = new ColorDrawable(context.getResources().getColor(R.color.colorDivider));
        }
    }

    @Override
    public void onDraw(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
        drawHorizontalDivider(canvas, parent);
    }

    public void drawHorizontalDivider(Canvas canvas, RecyclerView parent) {
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();

        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            int top = child.getBottom() + params.bottomMargin;
            int bottom = top + DIVIDER_WIDTH;
            this.divider.setBounds(left, top, right, bottom);
            this.divider.draw(canvas);
        }
    }

    @Override
    public void getItemOffsets(Rect rect, View view, RecyclerView parent, RecyclerView.State state) {
        rect.set(0, 0, 0, DIVIDER_WIDTH);
    }
}
