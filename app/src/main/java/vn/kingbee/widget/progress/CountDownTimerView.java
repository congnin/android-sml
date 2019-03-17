package vn.kingbee.widget.progress;

import android.content.Context;
import android.os.Handler;
import android.widget.TextView;
import vn.kingbee.widget.R;

import java.text.DecimalFormat;

public class CountDownTimerView {
    private static final int TIME_DELAY_ONE_SECOND = 1000;
    private static final int TIME_DELAY_TO_END = 10;
    private CircularProgressbar mTimeoutProgressBar;
    private TextView mTxtTimeoutCounter;
    private TextView mTxtTimeoutSeconds;
    private Handler mTimeOutHandler = new Handler();
    private CountDownTimerCallback mTimeOutListener;
    private Context mContext;
    private int mCountDownTimer;
    private int mCountDown;
    private Runnable mRunnableCountDow = new Runnable() {
        public void run() {
            CountDownTimerView.this.mCountDown--;
            if (CountDownTimerView.this.mCountDown < 0) {
                if (CountDownTimerView.this.mTimeOutListener != null) {
                    CountDownTimerView.this.mTimeOutListener.onFinish();
                }
            } else {
                CountDownTimerView.this.setTextCounter();
                CountDownTimerView.this.mTimeOutHandler.postDelayed(this, (long)CountDownTimerView.this.getTimeDelay());
            }

        }
    };

    public CountDownTimerView(Context context, CircularProgressbar progress, TextView tvCounter, TextView tvLabel, int timer, CountDownTimerCallback listener) {
        this.mContext = context;
        this.mTimeoutProgressBar = progress;
        this.mTxtTimeoutCounter = tvCounter;
        this.mTxtTimeoutSeconds = tvLabel;
        this.mCountDownTimer = timer;
        this.mTimeOutListener = listener;
        this.setupProgress();
    }

    public void startCountDownTimer() {
        this.mCountDown = this.mCountDownTimer;
        this.setTextCounter();
        this.cancelCountDownTimer();
        this.mTimeOutHandler.postDelayed(this.mRunnableCountDow, (long)this.getTimeDelay());
        this.mTimeoutProgressBar.setProgress((float)this.mCountDownTimer, true);
    }

    public void cancelCountDownTimer() {
        this.mTimeOutHandler.removeCallbacksAndMessages((Object)null);
        this.mTimeoutProgressBar.stopAnimation();
    }

    private void setTextCounter() {
        DecimalFormat decimalFormat = new DecimalFormat("00");
        this.mTxtTimeoutCounter.setText(decimalFormat.format((long)this.mCountDown));
        if (this.mCountDown <= 1) {
            this.mTxtTimeoutSeconds.setText(this.mContext.getString(R.string.LBL_SECOND));
        } else {
            this.mTxtTimeoutSeconds.setText(this.mContext.getString(R.string.LBL_SECONDS));
        }

    }

    private void setupProgress() {
        this.mTimeoutProgressBar.setMaxProgress((float)this.mCountDownTimer);
        this.mTimeoutProgressBar.setAnimationDuration((long)(this.mCountDownTimer * TIME_DELAY_ONE_SECOND));
    }

    private int getTimeDelay() {
        return this.mCountDown > 0 ? 990 : TIME_DELAY_TO_END;
    }
}
