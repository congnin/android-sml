package vn.kingbee.rxjava

import vn.kingbee.widget.BaseActivity
import android.content.Intent
import android.os.Bundle
import android.view.View
import vn.kingbee.rxjava.operators.*
import vn.kingbee.widget.R

class OperatorsActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_operators)
    }

    fun startSimpleActivity(view: View) {
        startActivity(Intent(this@OperatorsActivity, SimpleExampleActivity::class.java))
    }

    fun startMapActivity(view: View) {
        startActivity(Intent(this@OperatorsActivity, MapExampleActivity::class.java))
    }

    fun startZipActivity(view: View) {
        startActivity(Intent(this@OperatorsActivity, ZipExampleActivity::class.java))
    }

    fun startDisposableActivity(view: View) {
        startActivity(Intent(this@OperatorsActivity, DisposableExampleActivity::class.java))
    }

    fun startTakeActivity(view: View) {
        startActivity(Intent(this@OperatorsActivity, TakeExampleActivity::class.java))
    }

    fun startTimerActivity(view: View) {
        startActivity(Intent(this@OperatorsActivity, TimerExampleActivity::class.java))
    }

    fun startIntervalActivity(view: View) {
        startActivity(Intent(this@OperatorsActivity, IntervalExampleActivity::class.java))
    }

    fun startSingleObserverActivity(view: View) {
        startActivity(Intent(this@OperatorsActivity, SingleObserverExampleActivity::class.java))
    }

    fun startCompletableObserverActivity(view: View) {
        startActivity(
            Intent(
                this@OperatorsActivity,
                CompletableObserverExampleActivity::class.java
            )
        )
    }

    fun startFlowableActivity(view: View) {
//        startActivity(Intent(this@OperatorsActivity, FlowableExampleActivity::class.java))
    }

    fun startReduceActivity(view: View) {
//        startActivity(Intent(this@OperatorsActivity, ReduceExampleActivity::class.java))
    }

    fun startBufferActivity(view: View) {
//        startActivity(Intent(this@OperatorsActivity, BufferExampleActivity::class.java))
    }

    fun startFilterActivity(view: View) {
//        startActivity(Intent(this@OperatorsActivity, FilterExampleActivity::class.java))
    }

    fun startSkipActivity(view: View) {
//        startActivity(Intent(this@OperatorsActivity, SkipExampleActivity::class.java))
    }

    fun startScanActivity(view: View) {
//        startActivity(Intent(this@OperatorsActivity, ScanExampleActivity::class.java))
    }

    fun startReplayActivity(view: View) {
//        startActivity(Intent(this@OperatorsActivity, ReplayExampleActivity::class.java))
    }

    fun startConcatActivity(view: View) {
//        startActivity(Intent(this@OperatorsActivity, ConcatExampleActivity::class.java))
    }

    fun startMergeActivity(view: View) {
//        startActivity(Intent(this@OperatorsActivity, MergeExampleActivity::class.java))
    }

    fun startDeferActivity(view: View) {
//        startActivity(Intent(this@OperatorsActivity, DeferExampleActivity::class.java))
    }

    fun startDistinctActivity(view: View) {
//        startActivity(Intent(this@OperatorsActivity, DistinctExampleActivity::class.java))
    }

    fun startLastOperatorActivity(view: View) {
//        startActivity(Intent(this@OperatorsActivity, LastOperatorExampleActivity::class.java))
    }

    fun startReplaySubjectActivity(view: View) {
//        startActivity(Intent(this@OperatorsActivity, ReplaySubjectExampleActivity::class.java))
    }

    fun startPublishSubjectActivity(view: View) {
        startActivity(Intent(this@OperatorsActivity, PublishSubjectExampleActivity::class.java))
    }

    fun startBehaviorSubjectActivity(view: View) {
//        startActivity(Intent(this@OperatorsActivity, BehaviorSubjectExampleActivity::class.java))
    }

    fun startAsyncSubjectActivity(view: View) {
//        startActivity(Intent(this@OperatorsActivity, AsyncSubjectExampleActivity::class.java))
    }

    fun startThrottleFirstActivity(view: View) {
        startActivity(Intent(this@OperatorsActivity, ThrottleFirstExampleActivity::class.java))
    }

    fun startThrottleLastActivity(view: View) {
//        startActivity(Intent(this@OperatorsActivity, ThrottleLastExampleActivity::class.java))
    }

    fun startDebounceActivity(view: View) {
//        startActivity(Intent(this@OperatorsActivity, DebounceExampleActivity::class.java))
    }

    fun startWindowActivity(view: View) {
//        startActivity(Intent(this@OperatorsActivity, WindowExampleActivity::class.java))
    }

    fun startDelayActivity(view: View) {
//        startActivity(Intent(this@OperatorsActivity, DelayExampleActivity::class.java))
    }

    fun startSwitchMapActivity(view: View) {
//        startActivity(Intent(this@OperatorsActivity, SwitchMapExampleActivity::class.java))
    }

    fun startTakeWhileActivity(view: View) {
//        startActivity(Intent(this@OperatorsActivity, TakeWhileExampleActivity::class.java))
    }

    fun startTakeUntilActivity(view: View) {
//        startActivity(Intent(this@OperatorsActivity, TakeUntilExampleActivity::class.java))
    }
}