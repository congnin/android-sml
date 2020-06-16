package vn.kingbee.widget.button.fitbutton.drawer

internal abstract class Drawer<V, T> constructor(private val view: V, private val button: T) {

    /**
     * Draw an element in the view
     */
    abstract fun draw()

    /**
     * Check that an element ready to draw
     */
    abstract fun isReady() : Boolean

}