package vn.kingbee.widget.progress.circularprogressindicator

class PatternProgressTextAdapter(private val pattern: String) :
    CircularProgressIndicator.ProgressTextAdapter {

    override fun formatText(currentProgress: Double): String {
        return String.format(pattern, currentProgress)
    }
}