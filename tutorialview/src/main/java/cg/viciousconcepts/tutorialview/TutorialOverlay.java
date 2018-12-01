/**
 * File TutorialOverlay
 * <p>
 * JDK version 8
 *
 * @author RaMoNVicious
 * @category tutorial-view
 * @copyright 2017-2018 RaMoNVicious
 * @created 28.11.18 13:46
 */

package cg.viciousconcepts.tutorialview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import cg.viciousconcepts.tutorialview.models.TargetPosition;
import cg.viciousconcepts.tutorialview.models.TutorialTargetType;

/**
 * Class TutorialOverlay
 *
 * JDK version 8
 *
 * @author RaMoNVicious
 * @category tutorial-view
 * @package cg.viciousconcepts.tutorialview
 * @copyright 2017-2018 RaMoNVicious
 * @created 28.11.18 13:46
 */

public class TutorialOverlay extends android.support.v7.widget.AppCompatImageView {

    public interface OnTargetFound {
        void targetFound(TargetPosition targetPosition, RectF viewRect);
    }

    private static final PorterDuffXfermode DRAW_MODE_NORMAL = new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER);
    private static final PorterDuffXfermode DRAW_MODE_CLEAR = new PorterDuffXfermode(PorterDuff.Mode.CLEAR);

    private TutorialTargetType targetType;

    private Rect viewRect = new Rect();

    private Paint paint;

    private int viewMargin;
    private RectF highlight;
    private float round;
    private Bitmap bitmap;

    private OnTargetFound onTargetFoundListener;

    public TutorialOverlay(Context context, AttributeSet attrs) {
        super(context, attrs);
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        viewMargin = getResources().getDimensionPixelSize(R.dimen.padding_large);

        setClickable(true);
    }

    public void setOnTargetFoundListener(OnTargetFound onTargetFoundListener) {
        this.onTargetFoundListener = onTargetFoundListener;
    }

    public void setCircleTarget(View view) {
        targetType = TutorialTargetType.TARGET_CIRCLE;

        Rect rect = new Rect();
        view.getGlobalVisibleRect(rect);

        if (rect.width() > rect.height()) {
            float dSize = (rect.width() - rect.height()) / 2.0f;
            rect.top -= dSize;
            rect.bottom += dSize;
        } else if (rect.width() < rect.height()) {
            float dSize = (rect.height() - rect.width()) / 2.0f;
            rect.left -= dSize;
            rect.right += dSize;
        }

        RectF rectF = new RectF(rect.left - viewMargin, rect.top - viewMargin, rect.right + viewMargin, rect.bottom + viewMargin);
        round = rectF.width() / 2.0f;

        setTarget(rectF);
    }

    public void setRectangleTarget(View view) {
        targetType = TutorialTargetType.TARGET_RECTANGLE;

        Rect rect = new Rect();
        view.getGlobalVisibleRect(rect);

        RectF rectF = new RectF(rect.left, rect.top, rect.right, rect.bottom);
        round = 0;

        setTarget(rectF);
    }

    public void setRectangleTarget(View view, Bitmap centerImage) {
        this.bitmap = centerImage;
        setRectangleTarget(view);
    }

    private void setTarget(RectF rectF, Bitmap centerBitmap) {
        this.bitmap = centerBitmap;
        setTarget(rectF);
    }

    private void setTarget(RectF rectF) {

        highlight = rectF;

        postInvalidate();
    }

    public boolean isClickedOnView(float x, float y) {
        return x >= highlight.left && x <= highlight.right && y >= highlight.top && y <= highlight.bottom;
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (highlight != null) {
            updateTargetPosition();

            paint.setXfermode(DRAW_MODE_NORMAL);
            paint.setColor(getResources().getColor(R.color.background));
            paint.setStyle(Paint.Style.FILL);
            canvas.drawPaint(paint);

            paint.setXfermode(DRAW_MODE_CLEAR);
            canvas.drawRoundRect(highlight, round, round, paint);

            if (bitmap != null) {
                paint.setXfermode(DRAW_MODE_NORMAL);
                paint.setColor(getResources().getColor(R.color.background));
                paint.setAlpha(127);
                paint.setStyle(Paint.Style.FILL);
                canvas.drawCircle(highlight.centerX(), highlight.centerY(), Math.max(bitmap.getWidth(), bitmap.getHeight()) / 2.0f + 2.0f * viewMargin, paint);

                paint.setAlpha(255);
                canvas.drawBitmap(bitmap, highlight.centerX() - bitmap.getWidth() / 2.0f, highlight.centerY() - bitmap.getHeight() / 2.0f, paint);
            }
        }
    }

    private void updateTargetPosition() {
        ((View) getParent()).getGlobalVisibleRect(viewRect);
        highlight.offset(-1f * viewRect.left, -1f * viewRect.top);

        if (targetType == TutorialTargetType.TARGET_RECTANGLE) {
            tuneRectangle();
        }

        TargetPosition targetPosition;
        if (highlight.centerY() < viewRect.bottom / 2 && highlight.centerX() <= 0.33f * viewRect.right) {
            targetPosition = TargetPosition.TOP;
        } else if (highlight.centerY() < viewRect.bottom / 2 && highlight.centerX() > 0.33f * viewRect.right) {
            targetPosition = TargetPosition.TOP_RIGHT;
        } else if (highlight.centerY() >= viewRect.bottom / 2 && highlight.centerX() <= 0.33f * viewRect.right) {
            targetPosition = TargetPosition.BOTTOM;
        } else {
            targetPosition = TargetPosition.BOTTOM_RIGHT;
        }

        if (onTargetFoundListener != null) {
            onTargetFoundListener.targetFound(targetPosition, highlight);
        }
    }

    private void tuneRectangle() {
        if (highlight.width() >= highlight.height() && (highlight.left - viewRect.left > 0 || viewRect.right - highlight.right > 0)) {
            highlight.top -= viewMargin / 2.0f;
            highlight.bottom += viewMargin / 2.0f;
            round = highlight.height() / 2.0f;
            highlight.left -= round;
            highlight.right += round;
        } else if (highlight.width() < highlight.height() && (highlight.top - viewRect.top > 0 || viewRect.bottom - highlight.bottom > 0)) {
            highlight.left -= viewMargin / 2.0f;
            highlight.right += viewMargin / 2.0f;
            round = highlight.width() / 2.0f;
            highlight.top -= round;
            highlight.bottom += round;
        }
    }
}
