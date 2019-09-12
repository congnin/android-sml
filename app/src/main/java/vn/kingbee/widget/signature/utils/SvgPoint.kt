package vn.kingbee.widget.signature.utils

/**
 * Represent a point as it would be in the generated SVG document.
 */
class SvgPoint {
    var x: Int = 0
    var y: Int = 0

    constructor(point: TimedPoint) {
        // one optimisation is to get rid of decimals as they are mostly non-significant in the
        // produced SVG image
        x = Math.round(point.x)
        y = Math.round(point.y)
    }

    constructor(x: Int, y: Int) {
        this.x = x
        this.y = y
    }

    fun toAbsoluteCoordinates(): String {
        val stringBuilder = StringBuilder()
        stringBuilder.append(x)
        if (y >= 0) {
            stringBuilder.append(" ")
        }
        stringBuilder.append(y)
        return stringBuilder.toString()
    }

    fun toRelativeCoordinates(referencePoint: SvgPoint): String {
        return SvgPoint(x - referencePoint.x, y - referencePoint.y).toString()
    }

    override fun toString(): String {
        return toAbsoluteCoordinates()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false

        val svgPoint = other as SvgPoint?

        return if (x != svgPoint!!.x) false else y == svgPoint.y

    }

    override fun hashCode(): Int {
        var result = x.hashCode()
        result = 31 * result + y.hashCode()
        return result
    }
}