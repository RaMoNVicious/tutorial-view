/**
 * File TutorialView
 *
 * JDK version 8
 *
 * @author RaMoNVicious
 * @category tutorial-view
 * @copyright 2017-2018 RaMoNVicious
 * @created 28.11.18 13:46
 */

package cg.viciousconcepts.tutorialview;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import cg.viciousconcepts.tutorialview.models.TargetPosition;
import cg.viciousconcepts.tutorialview.models.TutorialTargetType;

/**
 * Class TutorialView
 *
 * JDK version 8
 *
 * @author RaMoNVicious
 * @category tutorial-view
 * @package cg.viciousconcepts.tutorialview
 * @copyright 2017-2018 RaMoNVicious
 * @created 28.11.18 13:46
 */

public class TutorialView extends FrameLayout {

    public interface OnTutorialEndsListener {
        void onTutorialEnds();
    }

    public interface OnTutorialShowListener {
        void onTutorialShow();
    }

    FrameLayout frame;
    ImageView arrowTopLeft;
    ImageView arrowTop;
    ImageView arrowTopRight;
    ImageView arrowBottomLeft;
    ImageView arrowBottom;
    ImageView arrowBottomRight;
    TextView txtTitle;
    TextView txtDescription;

    private View target;
    private CharSequence title;
    private CharSequence description;
    private OnTutorialEndsListener onTutorialEndsListener;
    private OnTutorialShowListener onTutorialShowListener;

    private boolean isShowing;

    protected TutorialView(Context context) {
        this(context, null);
    }

    public TutorialView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void showTargetInformation(TargetPosition targetPosition, RectF viewRect) {
        txtTitle.setText(title);
        txtDescription.setText(description);

        FrameLayout.LayoutParams lpFrame = (FrameLayout.LayoutParams) frame.getLayoutParams();
        switch (targetPosition) {
            default:
            case BOTTOM_LEFT:
            case BOTTOM_RIGHT:
            case BOTTOM:
                lpFrame.height = (int) viewRect.top;
                break;
            case TOP_LEFT:
            case TOP_RIGHT:
            case TOP:
                lpFrame.topMargin = (int) viewRect.bottom;
                lpFrame.height = LayoutParams.WRAP_CONTENT;
                break;
        }
        frame.setLayoutParams(lpFrame);

        switch (targetPosition) {
            default:
            case BOTTOM_LEFT:
                arrowBottomLeft.setImageResource(R.drawable.ic_arrow_bottom_left);
                break;
            case BOTTOM_RIGHT:
                arrowBottomRight.setImageResource(R.drawable.ic_arrow_bottom_right);
                break;
            case BOTTOM:
                arrowBottom.setImageResource(R.drawable.ic_arrow_bottom);
                break;
            case TOP_LEFT:
                arrowTopLeft.setImageResource(R.drawable.ic_arrow_top_left);
                break;
            case TOP_RIGHT:
                arrowTopRight.setImageResource(R.drawable.ic_arrow_top_right);
                break;
            case TOP:
                arrowTop.setImageResource(R.drawable.ic_arrow_top);
                break;
        }
    }

    private static void insertTutorialView(TutorialView tutorialView, ViewGroup parent, int parentIndex) {
        parent.addView(tutorialView, parentIndex);
        tutorialView.show();
    }

    public void setTarget(View target) {
        this.target = target;
    }

    public View getTarget() {
        return target;
    }

    public void setTitle(CharSequence title) {
        this.title = title;
    }

    public void setDescription(CharSequence description) {
        this.description = description;
    }

    public OnTutorialEndsListener getOnTutorialEndsListener() {
        return onTutorialEndsListener;
    }

    public void setOnTutorialEndsListener(OnTutorialEndsListener onTutorialEndsListener) {
        this.onTutorialEndsListener = onTutorialEndsListener;
    }

    public OnTutorialShowListener getOnTutorialShowListener() {
        return onTutorialShowListener;
    }

    public void setOnTutorialShowListener(OnTutorialShowListener onTutorialShowListener) {
        this.onTutorialShowListener = onTutorialShowListener;
    }

    public void show() {
        isShowing = true;

        View tutorialViewLayout = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.tutorial, null);
        addView(tutorialViewLayout);

        frame = ((Activity) getContext()).findViewById(R.id.frame);
        txtTitle = ((Activity) getContext()).findViewById(R.id.txtTitle);
        txtDescription = ((Activity) getContext()).findViewById(R.id.txtDescription);
        arrowTopLeft = ((Activity) getContext()).findViewById(R.id.arrowTopLeft);
        arrowTop = ((Activity) getContext()).findViewById(R.id.arrowTop);
        arrowTopRight = ((Activity) getContext()).findViewById(R.id.arrowTopRight);
        arrowBottomLeft = ((Activity) getContext()).findViewById(R.id.arrowBottomLeft);
        arrowBottom = ((Activity) getContext()).findViewById(R.id.arrowBottom);
        arrowBottomRight = ((Activity) getContext()).findViewById(R.id.arrowBottomRight);

        fadeInShowcase();
    }

    public void dismiss() {
        dismiss(false);
    }

    public void dismiss(boolean force) {
        if (isShowing) {
            ViewGroup viewGroup = ((ViewGroup) getParent());
            if (viewGroup != null) viewGroup.removeView(this);

            if (onTutorialEndsListener != null && !force) {
                onTutorialEndsListener.onTutorialEnds();
            }
        }
    }

    private void fadeInShowcase() {
        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.apear);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) { /* */ }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (onTutorialShowListener != null)
                    onTutorialShowListener.onTutorialShow();
            }

            @Override
            public void onAnimationRepeat(Animation animation) { /* */ }
        });
        setVisibility(View.VISIBLE);
        startAnimation(animation);
    }

    public static class Builder {

        private final Activity activity;

        private final TutorialView tutorialView;
        private final TutorialOverlay tutorialOverlay;

        private ViewGroup parent;
        private int parentIndex;

        private boolean isPerformClickOnTarget = false;

        public Builder(Activity activity) {
            this.activity = activity;
            this.tutorialView = new TutorialView(activity);
            this.parent = activity.findViewById(android.R.id.content);
            this.parentIndex = parent.getChildCount();

            this.tutorialOverlay = new TutorialOverlay(activity, null);
            this.tutorialOverlay.setOnTouchListener(new OnTouchListener() {
                private static final int CLICK_ACTION_THRESHOLD = 200;
                private float startX;
                private float startY;

                @Override
                public boolean onTouch(View v, MotionEvent motionEvent) {
                    switch (motionEvent.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            startX = motionEvent.getX();
                            startY = motionEvent.getY();
                            break;
                        case MotionEvent.ACTION_UP:
                            float endX = motionEvent.getX();
                            float endY = motionEvent.getY();
                            if (tutorialView.getTarget() != null && isPerformClickOnTarget) {
                                if (tutorialOverlay.isClickedOnView(endX, endY)) {
                                    tutorialView.getTarget().performClick();
                                    tutorialView.dismiss();
                                }
                            } else if (isClick(startX, endX, startY, endY)) {
                                tutorialView.dismiss();
                            }
                        default:
                    }
                    return false;
                }

                private boolean isClick(float startX, float endX, float startY, float endY) {
                    return !(Math.abs(startX - endX) > CLICK_ACTION_THRESHOLD || Math.abs(startY - endY) > CLICK_ACTION_THRESHOLD);
                }
            });
            this.tutorialOverlay.setOnTargetFoundListener(this.tutorialView::showTargetInformation);

            this.tutorialView.addView(this.tutorialOverlay);
        }

        public TutorialView show() {
            insertTutorialView(tutorialView, parent, parentIndex);
            return tutorialView;
        }

        Builder setTarget(View view, TutorialTargetType targetType) {
            tutorialView.setTarget(view);
            switch (targetType) {
                default:
                case TARGET_CIRCLE:
                    tutorialOverlay.setCircleTarget(view);
                    break;
                case TARGET_RECTANGLE:
                    tutorialOverlay.setRectangleTarget(view);
                    break;
                case TARGET_SWIPE:
                    tutorialOverlay.setRectangleTarget(view, getBitmapFromVectorDrawable(view.getContext(), R.drawable.ic_swipe));
                    break;
            }
            return this;
        }

        public Builder setContentTitle(@StringRes int resId) {
            return setContentTitle(activity.getString(resId));
        }

        public Builder setContentTitle(CharSequence title) {
            tutorialView.setTitle(title);
            return this;
        }

        public Builder setContentDescription(@StringRes int resId) {
            return setContentDescription(activity.getString(resId));
        }

        public Builder setContentDescription(CharSequence description) {
            tutorialView.setDescription(description);
            return this;
        }

        public Builder setOnClickListener(OnClickListener onClickListener) {
            tutorialView.setOnClickListener(onClickListener);
            return this;
        }

        public Builder setOnTutorialEndsListener(OnTutorialEndsListener onTutorialEndsListener) {
            tutorialView.setOnTutorialEndsListener(onTutorialEndsListener);
            return this;
        }

        public Builder setOnTutorialShowListener(OnTutorialShowListener onTutorialShowListener) {
            this.tutorialView.setOnTutorialShowListener(onTutorialShowListener);
            return this;
        }

        public Builder setPerformClickOnTarget(boolean performClickOnTarget) {
            this.isPerformClickOnTarget = performClickOnTarget;
            return this;
        }

        private static Bitmap getBitmapFromVectorDrawable(Context context, int drawableId) {
            Drawable drawable = ContextCompat.getDrawable(context, drawableId);
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                drawable = (DrawableCompat.wrap(drawable)).mutate();
            }

            Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);

            return bitmap;
        }
    }
}
