package vn.kingbee.widget.textview.fading

import androidx.annotation.IntDef
import android.view.animation.Animation
import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.util.AttributeSet
import android.view.animation.AnimationUtils
import androidx.annotation.ArrayRes
import androidx.appcompat.widget.AppCompatTextView
import vn.kingbee.widget.R

//https://github.com/rosenpin/fading-text-view
class FadingTextView : AppCompatTextView {

    private var fadeInAnimation: Animation? = null
    private var fadeOutAnimation: Animation? = null
    private var mHandler: Handler? = null
    /**
     * Get a list of the texts
     *
     * @return the texts array
     */
    var texts: Array<CharSequence>? = null
    private var isShown: Boolean? = null
    private var position: Int = 0
    private var timeout = DEFAULT_TIME_OUT
    private var stopped: Boolean = false

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
        handleAttrs(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
        handleAttrs(attrs)
    }

    /**
     * Resumes the animation
     * Should only be used if you notice [.onAttachedToWindow] ()} is not being executed as expected
     */
    fun resume() {
        isShown = true
        startAnimation()
    }

    /**
     * Pauses the animation
     * Should only be used if you notice [.onDetachedFromWindow] is not being executed as expected
     */
    fun pause() {
        isShown = false
        stopAnimation()
    }

    /**
     * Stops the animation
     * Unlike the pause function, the stop method will permanently stop the animation until the view is restarted
     */
    fun stop() {
        isShown = false
        stopped = true
        stopAnimation()
    }

    /**
     * Restarts the animation
     * Only use this to restart the animation after stopping it using [.stop]
     */
    fun restart() {
        isShown = true
        stopped = false
        startAnimation()
        invalidate()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        pause()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        resume()
    }

    /**
     * Initialize the view and the animations
     */
    private fun init() {
        fadeInAnimation = AnimationUtils.loadAnimation(context, R.anim.fadein)
        fadeOutAnimation = AnimationUtils.loadAnimation(context, R.anim.fadeout)
        mHandler = Handler()
        isShown = true
    }

    /**
     * Handle the attributes
     * set the texts
     * set the timeout
     *
     * @param attrs provided attributes
     */
    private fun handleAttrs(attrs: AttributeSet) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.FadingTextView)
        this.texts = a.getTextArray(R.styleable.FadingTextView_texts)
        this.timeout = Math.abs(a.getInteger(R.styleable.FadingTextView_timeout, 14500)) + resources.getInteger(android.R.integer.config_longAnimTime)
        a.recycle()
    }

    /**
     * Sets the texts to be shuffled using a string array
     *
     * @param texts The string array to use for the texts
     */
    fun setTexts(texts: Array<String>) {
        if (texts.isEmpty()) {
            throw IllegalArgumentException("There must be at least one text")
        } else {
            this.texts = texts.map { s -> s as CharSequence }.toTypedArray()
            stopAnimation()
            position = 0
            startAnimation()
        }
    }

    /**
     * Sets the texts to be shuffled using a string array resource
     *
     * @param texts The string array resource to use for the texts
     */
    fun setTexts(@ArrayRes texts: Int) {
        if (resources.getStringArray(texts).isEmpty()) {
            throw IllegalArgumentException("There must be at least one text")
        } else {
            this.texts = resources.getStringArray(texts).map { s -> s as CharSequence }.toTypedArray()
            stopAnimation()
            position = 0
            startAnimation()
        }
    }

    /**
     * This method should only be used to forcefully apply timeout changes
     * It will dismiss the currently queued animation change and start a new animation
     */
    fun forceRefresh() {
        stopAnimation()
        startAnimation()
    }

    /**
     * Sets the length of time to wait between text changes in milliseconds
     *
     * @param timeout The length of time to wait between text change in milliseconds
     */
    @Deprecated("use {@link #setTimeout(long, java.util.concurrent.TimeUnit)} instead.")
    fun setTimeout(timeout: Int) {
        if (timeout < 1) {
            throw IllegalArgumentException("Timeout must be longer than 0")
        } else {
            this.timeout = timeout
        }
    }

    /**
     * Sets the length of time to wait between text changes in specific time units
     *
     * @param timeout  The length of time to wait between text change
     * @param timeUnit The time unit to use for the timeout parameter
     * Must be of [TimeUnit] type.    Either [.MILLISECONDS] or
     * [.SECONDS] or
     * [.MINUTES]
     */
    @Deprecated("use {@link #setTimeout(long, java.util.concurrent.TimeUnit)} instead.")
    fun setTimeout(timeout: Double, @TimeUnit timeUnit: Int) {
        if (timeout <= 0) {
            throw IllegalArgumentException("Timeout must be longer than 0")
        } else {
            val multiplier: Int
            when (timeUnit) {
                MILLISECONDS -> multiplier = 1
                SECONDS -> multiplier = 1000
                MINUTES -> multiplier = 60000
                else -> multiplier = 1
            }
            this.timeout = (timeout * multiplier).toInt()
        }
    }

    @SuppressLint("reference not found")
            /**
             * Sets the length of time to wait between text changes in specific time units
             *
             * @param timeout  The length of time to wait between text change
             * @param timeUnit The time unit to use for the timeout parameter
             * Must be of [java.util.concurrent.TimeUnit] type.
             * Must be one of
             * [java.util.concurrent.TimeUnit.NANOSECONDS] or
             * [java.util.concurrent.TimeUnit.MICROSECONDS] or
             * [java.util.concurrent.TimeUnit.MILLISECONDS] or
             * [java.util.concurrent.TimeUnit.SECONDS] or
             * [java.util.concurrent.TimeUnit.MINUTES] or
             * [java.util.concurrent.TimeUnit.HOURS] or
             * [java.util.concurrent.TimeUnit.DAYS] or
             */
    fun setTimeout(timeout: Long, timeUnit: java.util.concurrent.TimeUnit) {
        if (timeout <= 0) {
            throw IllegalArgumentException("Timeout must be longer than 0")
        } else {
            this.timeout = java.util.concurrent.TimeUnit.MILLISECONDS
                .convert(timeout, timeUnit).toInt()
        }
    }

    /**
     * Start the specified animation now if should
     *
     * @param animation the animation to start now
     */
    override fun startAnimation(animation: Animation?) {
        if (isShown != null && isShown as Boolean && !stopped) {
            super.startAnimation(animation)
        }
    }

    /**
     * Start the animation
     */
    protected fun startAnimation() {
        if (!isInEditMode) {
            text = texts!![position]
            startAnimation(fadeInAnimation)
            handler!!.postDelayed(Runnable {
                startAnimation(fadeOutAnimation)
                if (animation != null) {
                    animation.setAnimationListener(object : Animation.AnimationListener {
                        override fun onAnimationStart(animation: Animation) {

                        }

                        override fun onAnimationEnd(animation: Animation) {
                            if (isShown != null && isShown as Boolean) {
                                position = if (position == texts!!.size - 1) 0 else position + 1
                                startAnimation()
                            }
                        }

                        override fun onAnimationRepeat(animation: Animation) {

                        }
                    })
                }
            }, timeout.toLong())
        }
    }

    /**
     * Stop the currently active animation
     */
    private fun stopAnimation() {
        handler!!.removeCallbacksAndMessages(null)
        if (animation != null) animation.cancel()
    }


    @IntDef(MILLISECONDS, SECONDS, MINUTES)
    @Retention(AnnotationRetention.SOURCE)
    annotation class TimeUnit

    companion object {
        const val DEFAULT_TIME_OUT = 15000
        const val MILLISECONDS = 1
        const val SECONDS = 2
        const val MINUTES = 3
    }
}