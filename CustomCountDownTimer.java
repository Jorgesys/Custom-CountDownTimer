package com.jorgesys.myapplication;

import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;

/*
* Based in CountDownTimerWithPause created by Gautier Hayoun
* https://github.com/Gautier
*
* https://gist.github.com/Gautier/737759
* */


public abstract class CustomCountDownTimer {

    private static final String TAG = CustomCountDownTimer.class.getSimpleName();
    private static final int MSG = 1;
    private long mStopTimeInFuture;
    private long mMillisInFuture;
    private final long mTotalCountdown;
    private final long mCountdownInterval;
    private long mPauseTimeRemaining;

    public CustomCountDownTimer(long millisOnTimer, long countDownInterval) {
        mMillisInFuture = millisOnTimer;
        mTotalCountdown = mMillisInFuture;
        mCountdownInterval = countDownInterval;
    }

    public final void cancel() {
        mHandler.removeMessages(MSG);
    }

    public synchronized final CustomCountDownTimer create() {

        if (mMillisInFuture <= 0) {
            //End CountDownTimer
            onFinish();
        } else {
            mPauseTimeRemaining = mMillisInFuture;
        }

        //Start CountDownTimer!
        resume();

        return this;
    }

    public void pause () {
        if (isRunning()) {
            mPauseTimeRemaining = timeLeft();
            cancel();
        }
    }

    public void resume() {
        if (isPaused()) {
            mMillisInFuture = mPauseTimeRemaining;
            mStopTimeInFuture = SystemClock.elapsedRealtime() + mMillisInFuture;
            mHandler.sendMessage(mHandler.obtainMessage(MSG));
            mPauseTimeRemaining = 0;
        }
    }

    public boolean isPaused () {
        return (mPauseTimeRemaining > 0);
    }

    public boolean isRunning() {
        return (!isPaused());
    }

    public long timeLeft() {
        long millisUntilFinished;
        if (isPaused()) {
            millisUntilFinished = mPauseTimeRemaining;
        } else {
            millisUntilFinished = mStopTimeInFuture - SystemClock.elapsedRealtime();
            if (millisUntilFinished < 0) millisUntilFinished = 0;
        }
        return millisUntilFinished;
    }

    public long totalCountdown() {
        return mTotalCountdown;
    }

    public long elapsedTime() {
        return mTotalCountdown - timeLeft();
    }

    public boolean hasBeenStarted() {
        return (mPauseTimeRemaining <= mMillisInFuture);
    }

    public abstract void onTick(long millisUntilFinished);

    public abstract void onFinish();


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            synchronized (CustomCountDownTimer.this) {
                long millisLeft = timeLeft();

                if (millisLeft <= 0) {
                    cancel();
                    onFinish();
                } else if (millisLeft < mCountdownInterval) {
                    // no tick, just delay until done
                    sendMessageDelayed(obtainMessage(MSG), millisLeft);
                } else {
                    long lastTickStart = SystemClock.elapsedRealtime();
                    onTick(millisLeft);
                    // take into account user's onTick taking time to execute
                    long delay = mCountdownInterval - (SystemClock.elapsedRealtime() - lastTickStart);
                    // special case: user's onTick took more than mCountdownInterval to
                    // complete, skip to next interval
                    while (delay < 0) delay += mCountdownInterval;

                    sendMessageDelayed(obtainMessage(MSG), delay);
                }
            }
        }
    };
}