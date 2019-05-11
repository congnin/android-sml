package vn.kingbee.widget.progress;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;
import vn.kingbee.widget.R;

public class CircularProgressbar extends View {
    public static final int CLOCKWISE = 0;
    public static final int ANTICLOCKWISE = 1;
    public static final int LEFT = 0;
    public static final int TOP = 1;
    public static final int RIGHT = 2;
    public static final int BOTTOM = 3;
    private static final float CIRCLE_DEGREE = 360.0F;
    private static final int DEFAULT_START_ANGLE = 270;
    private final RectF mOuterCircleBounds;
    private final RectF mUnprogressedBounds;
    private float mTraverse;
    private float mCurrentProgress;
    private float mMaxProgress;
    private int mThickness;
    private int mProgressbarColor;
    private int mInnerProgressbarColor;
    private int mProgressbarBackgroundColor;
    private int mBackgroundColor;
    private int mRotation;
    private Paint mProgressbarPaint;
    private Paint mUnProgressbarPaint;
    private Paint mInnerProgressbarPaint;
    private Paint mPaintBackground;
    private boolean mIsEnableAnimation;
    private long mAnimationDuration;
    private float mStartAngle;

    public CircularProgressbar(Context context) {
        this(context, (AttributeSet)null);
    }

    public CircularProgressbar(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.circularProgressbarStyle);
    }

    public CircularProgressbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mOuterCircleBounds = new RectF();
        this.mUnprogressedBounds = new RectF();
        this.mTraverse = 0.0F;
        this.mBackgroundColor = 0;
        this.mStartAngle = DEFAULT_START_ANGLE;
        TypedArray attributes = context.obtainStyledAttributes(attrs,
                R.styleable.CircularProgressbar, defStyleAttr, 0);
        if (attributes != null) {
            try {
                this.mMaxProgress = attributes.getFloat(R.styleable.CircularProgressbar_maxProgress, 100.0F);
                this.mCurrentProgress = attributes.getFloat(R.styleable.CircularProgressbar_currentProgress, 0.0F);
                this.mStartAngle = (float)attributes.getInteger(R.styleable.CircularProgressIndicator_startAngle, DEFAULT_START_ANGLE);
                this.mRotation = attributes.getInteger(R.styleable.CircularProgressbar_rotation, 0);
                this.mInnerProgressbarColor = attributes.getColor(R.styleable.CircularProgressbar_innerProgressColor, 0);
                this.mProgressbarColor = attributes.getColor(R.styleable.CircularProgressIndicator_progressColor, 0);
                this.mProgressbarBackgroundColor = attributes.getColor(R.styleable.CircularProgressIndicator_progressBackgroundColor, 0);
                this.mThickness = attributes.getDimensionPixelOffset(R.styleable.CircularProgressbar_progressbar_thickness, 2);
            } finally {
                attributes.recycle();
            }

            this.init();
        }

    }

    public void setStartAngle(int angle) {
        this.mStartAngle = (float)angle;
    }

    public void setAnimationDuration(long mAnimationDuration) {
        this.mAnimationDuration = mAnimationDuration;
    }

    private void init() {
        this.mInnerProgressbarPaint = new Paint(1);
        this.mInnerProgressbarPaint.setColor(this.mInnerProgressbarColor);
        this.mInnerProgressbarPaint.setDither(true);
        this.mInnerProgressbarPaint.setAntiAlias(true);
        this.mInnerProgressbarPaint.setStyle(Paint.Style.FILL);
        this.mProgressbarPaint = new Paint(1);
        this.mProgressbarPaint.setColor(this.mProgressbarColor);
        this.mProgressbarPaint.setDither(true);
        this.mProgressbarPaint.setAntiAlias(true);
        this.mProgressbarPaint.setStrokeWidth((float)this.mThickness);
        this.mProgressbarPaint.setStyle(Paint.Style.STROKE);
        this.mUnProgressbarPaint = new Paint(1);
        this.mUnProgressbarPaint.setColor(this.mProgressbarBackgroundColor);
        this.mUnProgressbarPaint.setDither(true);
        this.mUnProgressbarPaint.setAntiAlias(true);
        this.mUnProgressbarPaint.setStrokeWidth((float)this.mThickness);
        this.mUnProgressbarPaint.setStyle(Paint.Style.STROKE);
        this.mPaintBackground = new Paint(1);
        this.mPaintBackground.setColor(this.mBackgroundColor);
        this.mPaintBackground.setDither(true);
        this.mPaintBackground.setAntiAlias(true);
        this.getProgressAngle(0.0F);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int defH = getDefaultSize(this.getSuggestedMinimumHeight() + this.getPaddingBottom() + this.getPaddingTop(), heightMeasureSpec);
        int defW = getDefaultSize(this.getSuggestedMinimumWidth() + this.getPaddingLeft() + this.getPaddingRight(), widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (widthMode == 0 || widthMode == -2147483648) {
            defW = 75;
        }

        if (heightMode == 0 || heightMode == -2147483648) {
            defH = 75;
        }

        this.setMeasuredDimension(defW, defH);
        float pHalfH = (float)defH / 2.0F;
        float pHalfW = (float)defW / 2.0F;
        float halfH;
        if (defH < defW) {
            defH -= this.mThickness;
            halfH = (float)defH / 2.0F;
            this.mOuterCircleBounds.set(pHalfW - halfH, pHalfH - halfH, pHalfW + halfH, pHalfH + halfH);
            this.mUnprogressedBounds.set(pHalfW - halfH, pHalfH - halfH, pHalfW + halfH, pHalfH + halfH);
        } else {
            defW -= this.mThickness;
            halfH = (float)defW / 2.0F;
            this.mOuterCircleBounds.set(pHalfW - halfH, pHalfH - halfH, pHalfW + halfH, pHalfH + halfH);
            this.mUnprogressedBounds.set(pHalfW - halfH, pHalfH - halfH, pHalfW + halfH, pHalfH + halfH);
        }

    }

    protected void onDraw(Canvas canvas) {
        float sweepAngle = this.mTraverse;
        float sweepAngleUnProgress = this.mTraverse - CIRCLE_DEGREE;
        if (this.mRotation == 1) {
            sweepAngle = -sweepAngle;
            sweepAngleUnProgress = -sweepAngleUnProgress;
        }

        canvas.drawOval(this.mOuterCircleBounds, this.mPaintBackground);
        canvas.drawArc(this.mOuterCircleBounds, this.mStartAngle, sweepAngle, false, this.mProgressbarPaint);
        canvas.drawArc(this.mUnprogressedBounds, this.mStartAngle, sweepAngleUnProgress, false, this.mUnProgressbarPaint);
        canvas.drawOval(this.mUnprogressedBounds, this.mInnerProgressbarPaint);
    }

    public void setProgress(float progress, boolean isAnimate) {
        this.mCurrentProgress = Math.abs(progress);
        this.setRotation(progress < 0.0F ? 1 : 0);
        if (isAnimate) {
            this.startAnimation();
        } else {
            this.mTraverse = this.getProgressAngle(this.mCurrentProgress);
            this.invalidate();
        }

    }

    public void setProgress(float progress) {
        this.setProgress(progress, false);
    }

    public void setMaxProgress(float maxProgress) {
        this.mMaxProgress = maxProgress;
    }

    public void setRotation(int rotation) {
        switch(rotation) {
            case 1:
                this.mRotation = 1;
                break;
            default:
                this.mRotation = 0;
        }

    }

    public void setThickness(int thickness) {
        this.mThickness = thickness;
        this.mProgressbarPaint.setStrokeWidth((float)this.mThickness);
        this.mUnProgressbarPaint.setStrokeWidth((float)this.mThickness);
    }

    public void setProgressColor(int color) {
        this.mProgressbarColor = color;
        this.mProgressbarPaint.setColor(this.mProgressbarColor);
    }

    public void setSecondaryColor(int color) {
        this.mProgressbarBackgroundColor = color;
        this.mUnProgressbarPaint.setColor(this.mProgressbarBackgroundColor);
    }

    public void setBackgroundColor(int color) {
        this.mBackgroundColor = color;
        this.mPaintBackground.setColor(this.mBackgroundColor);
    }

    private float getProgressAngle(float progress) {
        return progress >= this.mMaxProgress ? CIRCLE_DEGREE : progress * CIRCLE_DEGREE / this.mMaxProgress;
    }

    private void startAnimation() {
        this.mIsEnableAnimation = true;
        ValueAnimator anim = ValueAnimator.ofFloat(new float[]{0.0F, this.getProgressAngle(this.mCurrentProgress)});
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                if (CircularProgressbar.this.mIsEnableAnimation) {
                    CircularProgressbar.this.mTraverse = (Float)valueAnimator.getAnimatedValue();
                    CircularProgressbar.this.invalidate();
                } else {
                    valueAnimator.cancel();
                }

            }
        });
        anim.setDuration(this.mAnimationDuration);
        anim.setInterpolator(new LinearInterpolator());
        anim.start();
    }

    public void stopAnimation() {
        this.mIsEnableAnimation = false;
    }
}
