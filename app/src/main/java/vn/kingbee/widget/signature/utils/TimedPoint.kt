package vn.kingbee.widget.signature.utils

class TimedPoint {
    var x: Float = 0.toFloat()
    var y: Float = 0.toFloat()
    var timestamp: Long = 0

    operator fun set(x: Float, y: Float): TimedPoint {
        this.x = x
        this.y = y
        this.timestamp = System.currentTimeMillis()
        return this
    }

    fun velocityFrom(start: TimedPoint): Float {
        val velocity = distanceTo(start) / (this.timestamp - start.timestamp)
        return if (velocity != velocity) 0f else velocity
    }

    fun distanceTo(point: TimedPoint): Float {
        return Math.sqrt(
            Math.pow(
                (point.x - this.x).toDouble(),
                2.0
            ) + Math.pow((point.y - this.y).toDouble(), 2.0)
        ).toFloat()
    }
}