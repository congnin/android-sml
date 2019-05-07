package vn.kingbee.widget.signature.utils

class ControlTimedPoints {
    var c1: TimedPoint? = null
    var c2: TimedPoint? = null

    operator fun set(c1: TimedPoint, c2: TimedPoint): ControlTimedPoints {
        this.c1 = c1
        this.c2 = c2
        return this
    }
}