package vn.kingbee.widget.indicator

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.widget.ProgressBar
import vn.kingbee.widget.R
import timber.log.Timber
import android.animation.TimeInterpolator
import androidx.annotation.NonNull


class AnimateHorizontalProgressBar : ProgressBar {

    private var mProgressAnimator: ValueAnimator? = null
    private var mSecondaryProgressAnimator: ValueAnimator? = null
    private var mMaxAnimator: ValueAnimator? = null
    private var mIsAnimating = false
    private var mIsSecondaryAnimating = false
    private var mDuration = 1000
    private var mAnimateProgressListener: AnimateProgressListener? = null

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        getConfig(attrs)
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle) {
        getConfig(attrs)
        init()
    }

    fun setDuration(duration: Int) {
        mDuration = duration
        mProgressAnimator?.duration = duration.toLong()
        mSecondaryProgressAnimator?.duration = duration.toLong()
        mMaxAnimator?.duration = duration.toLong()
    }

    fun getDuration(): Int = mDuration

    private fun getConfig(attrs: AttributeSet?) {
        if (attrs != null) {
            val ta = context.obtainStyledAttributes(attrs, R.styleable.AnimateHorizontalProgressBar)
            this.mDuration = ta.getInt(R.styleable.AnimateHorizontalProgressBar_ahp_duration, DEFAULT_DURATION)
            ta.recycle()
        }
    }

    fun init() {
        this.mProgressAnimator = ValueAnimator()
        this.mProgressAnimator?.addUpdateListener { animation ->
            super@AnimateHorizontalProgressBar.setProgress(animation?.animatedValue as Int)
        }
        this.mProgressAnimator?.addListener(object : AnimateHorizontalProgressBar.SimpleAnimatorListener() {
            override fun onAnimationStart(animation: Animator) {
                this@AnimateHorizontalProgressBar.mIsAnimating = true
                if (this@AnimateHorizontalProgressBar.mAnimateProgressListener != null) {
                    this@AnimateHorizontalProgressBar.mAnimateProgressListener?.onAnimationStart(
                        this@AnimateHorizontalProgressBar.progress,
                        this@AnimateHorizontalProgressBar.max)
                }
            }

            override fun onAnimationEnd(animation: Animator) {
                this@AnimateHorizontalProgressBar.mIsAnimating = false
                if (this@AnimateHorizontalProgressBar.mAnimateProgressListener != null) {
                    this@AnimateHorizontalProgressBar.mAnimateProgressListener?.onAnimationEnd(
                        this@AnimateHorizontalProgressBar.progress,
                        this@AnimateHorizontalProgressBar.max)
                }
            }
        })
        this.mProgressAnimator?.duration = mDuration.toLong()
        this.mSecondaryProgressAnimator = ValueAnimator()
        this.mSecondaryProgressAnimator?.addUpdateListener { animation ->
            this@AnimateHorizontalProgressBar.secondaryProgress = animation?.animatedValue as Int
        }
        this.mSecondaryProgressAnimator?.addListener(object : AnimateHorizontalProgressBar.SimpleAnimatorListener() {
            override fun onAnimationStart(animation: Animator) {
                this@AnimateHorizontalProgressBar.mIsSecondaryAnimating = true
                if (this@AnimateHorizontalProgressBar.mAnimateProgressListener != null) {
                    this@AnimateHorizontalProgressBar.mAnimateProgressListener?.onAnimationStart(
                        this@AnimateHorizontalProgressBar.progress,
                        this@AnimateHorizontalProgressBar.max)
                }
            }

            override fun onAnimationEnd(animation: Animator) {
                this@AnimateHorizontalProgressBar.mIsSecondaryAnimating = false
                if (this@AnimateHorizontalProgressBar.mAnimateProgressListener != null) {
                    this@AnimateHorizontalProgressBar.mAnimateProgressListener?.onAnimationEnd(
                        this@AnimateHorizontalProgressBar.progress,
                        this@AnimateHorizontalProgressBar.max)
                }
            }
        })
        this.mSecondaryProgressAnimator?.duration = mDuration.toLong()
        this.mMaxAnimator = ValueAnimator()
        this.mMaxAnimator?.addUpdateListener { animation ->
            super@AnimateHorizontalProgressBar.setMax(animation.animatedValue as Int)
        }
        this.mMaxAnimator?.addListener(object : AnimateHorizontalProgressBar.SimpleAnimatorListener() {
            override fun onAnimationStart(animation: Animator) {
                this@AnimateHorizontalProgressBar.mIsAnimating = true
                if (this@AnimateHorizontalProgressBar.mAnimateProgressListener != null) {
                    this@AnimateHorizontalProgressBar.mAnimateProgressListener?.onAnimationStart(
                        this@AnimateHorizontalProgressBar.progress,
                        this@AnimateHorizontalProgressBar.max)
                }

            }

            override fun onAnimationEnd(animation: Animator) {
                this@AnimateHorizontalProgressBar.mIsAnimating = false
                if (this@AnimateHorizontalProgressBar.mAnimateProgressListener != null) {
                    this@AnimateHorizontalProgressBar.mAnimateProgressListener?.onAnimationEnd(
                        this@AnimateHorizontalProgressBar.progress,
                        this@AnimateHorizontalProgressBar.max)
                }

            }
        })
        this.mMaxAnimator?.duration = this.mDuration.toLong()
    }

    fun setProgressWithAnim(progress: Int) {
        if (this.mIsAnimating) {
            Timber.d("Now is animating. can't override animator")
        } else {
            if(this.mProgressAnimator == null) {
                this.init()
            }

            this.mProgressAnimator?.setIntValues(this.progress, progress)
            this.mProgressAnimator?.start()
        }
    }

    fun setSecondaryProgressWithAnim(progress: Int) {
        if (this.mIsSecondaryAnimating) {
            Timber.d("now is animating. cant override animator")
        } else {
            if (this.mSecondaryProgressAnimator == null) {
                this.init()
            }

            this.mSecondaryProgressAnimator?.setIntValues(this.secondaryProgress, progress)
            this.mSecondaryProgressAnimator?.start()
        }
    }

    @Synchronized
    override fun setProgress(progress: Int) {
        if (!this.mIsAnimating) {
            super.setProgress(progress)
        }
    }

    @Synchronized
    override fun setSecondaryProgress(secondaryProgress: Int) {
        if (!this.mIsSecondaryAnimating) {
            super.setSecondaryProgress(secondaryProgress)
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        if (this.mProgressAnimator != null) {
            this.mProgressAnimator?.cancel()
        }

        if (this.mSecondaryProgressAnimator != null) {
            this.mSecondaryProgressAnimator?.cancel()
        }

        if (this.mMaxAnimator != null) {
            this.mMaxAnimator?.cancel()
        }
    }

    fun cancelAnimation() {
        if (!this.mIsAnimating) {
            Timber.w("now is no animating.")
        } else {
            if (this.mProgressAnimator != null) {
                this.mProgressAnimator?.cancel()
            }

            if (this.mSecondaryProgressAnimator != null) {
                this.mSecondaryProgressAnimator?.cancel()
            }

            if (this.mMaxAnimator != null) {
                this.mMaxAnimator?.cancel()
            }

            this.mIsAnimating = false
        }
    }

    @Synchronized
    override fun setMax(max: Int) {
        if (!this.mIsAnimating) {
            super.setMax(max)
        }
    }

    fun getAnimDuration(): Long? {
        return this.mProgressAnimator?.duration
    }

    fun setAnimDuration(duration: Long) {
        this.mProgressAnimator?.duration = duration
        this.mMaxAnimator?.duration = duration
    }

    fun setStartDelay(delay: Long) {
        this.mProgressAnimator?.startDelay = delay
        this.mMaxAnimator?.startDelay = delay
    }

    fun setAnimInterpolator(@NonNull timeInterpolator: TimeInterpolator) {
        this.mProgressAnimator?.interpolator = timeInterpolator
        this.mMaxAnimator?.interpolator = timeInterpolator
    }

    fun setAnimateProgressListener(animateProgressListener: AnimateProgressListener) {
        this.mAnimateProgressListener = animateProgressListener
    }

    companion object {
        private val TAG = AnimateHorizontalProgressBar::class.java.name
        private const val DEFAULT_DURATION = 1000
    }

    private abstract class SimpleAnimatorListener : Animator.AnimatorListener {
        abstract override fun onAnimationStart(animation: Animator)

        abstract override fun onAnimationEnd(animation: Animator)

        override fun onAnimationCancel(animation: Animator) {}

        override fun onAnimationRepeat(animation: Animator) {}
    }

    interface AnimateProgressListener {
        fun onAnimationStart(processValue: Int, maxProcessValue: Int)

        fun onAnimationEnd(processValue: Int, maxProcessValue: Int)
    }
}