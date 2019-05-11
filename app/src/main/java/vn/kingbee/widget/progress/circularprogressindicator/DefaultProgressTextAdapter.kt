package vn.kingbee.widget.progress.circularprogressindicator

class DefaultProgressTextAdapter : CircularProgressIndicator.ProgressTextAdapter {

    override fun formatText(currentProgress: Double): String {
        return currentProgress.toInt().toString()
    }
}