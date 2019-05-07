package vn.kingbee.widget.signature.utils

class SvgPathBuilder {
    private val mStringBuilder: StringBuilder = StringBuilder()
    private val mStrokeWidth: Int?
    private val mStartPoint: SvgPoint
    private var mLastPoint: SvgPoint
    private var mLastSvgCommand: Char? = null

    constructor(startPoint: SvgPoint, strokeWidth: Int?) {
        mStrokeWidth = strokeWidth
        mStartPoint = startPoint
        mLastPoint = startPoint
        mLastSvgCommand = null
    }

    fun getStrokeWidth(): Int? {
        return mStrokeWidth
    }

    fun getLastPoint(): SvgPoint? {
        return mLastPoint
    }

    fun append(controlPoint1: SvgPoint,
               controlPoint2: SvgPoint,
               endPoint: SvgPoint): SvgPathBuilder {
        mStringBuilder.append(makeRelativeCubicBezierCurve(controlPoint1, controlPoint2, endPoint))
        mLastSvgCommand = SVG_RELATIVE_CUBIC_BEZIER_CURVE
        mLastPoint = endPoint
        return this
    }

    override fun toString(): String {
        return StringBuilder().append("<path ").append("stroke-width=\"").append(mStrokeWidth)
            .append("\" ").append("d=\"").append(SVG_MOVE).append(mStartPoint)
            .append(mStringBuilder).append("\"/>").toString()
    }

    private fun makeRelativeCubicBezierCurve(controlPoint1: SvgPoint,
                                             controlPoint2: SvgPoint,
                                             endPoint: SvgPoint): String {
        val sControlPoint1 = controlPoint1.toRelativeCoordinates(mLastPoint)
        val sControlPoint2 = controlPoint2.toRelativeCoordinates(mLastPoint)
        val sEndPoint = endPoint.toRelativeCoordinates(mLastPoint)

        val sb = StringBuilder()
        if (SVG_RELATIVE_CUBIC_BEZIER_CURVE != mLastSvgCommand) {
            sb.append(SVG_RELATIVE_CUBIC_BEZIER_CURVE)
            sb.append(sControlPoint1)
        } else {
            if (!sControlPoint1.startsWith("-")) {
                sb.append(" ")
            }
            sb.append(sControlPoint1)
        }

        if (!sControlPoint2.startsWith("-")) {
            sb.append(" ")
        }
        sb.append(sControlPoint2)

        if (!sEndPoint.startsWith("-")) {
            sb.append(" ")
        }
        sb.append(sEndPoint)

        // discard zero curve
        val svg = sb.toString()
        return if ("c0 0 0 0 0 0" == svg) {
            ""
        } else {
            svg
        }
    }

    companion object {
        const val SVG_RELATIVE_CUBIC_BEZIER_CURVE = 'c'
        const val SVG_MOVE = 'M'
    }
}