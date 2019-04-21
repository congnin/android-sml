package vn.kingbee.widget.button.fitbutton

import android.util.AttributeSet
import vn.kingbee.widget.button.fitbutton.drawer.ContainerDrawer
import vn.kingbee.widget.button.fitbutton.drawer.DividerDrawer
import vn.kingbee.widget.button.fitbutton.drawer.IconDrawer
import vn.kingbee.widget.button.fitbutton.drawer.TextDrawer
import vn.kingbee.widget.button.fitbutton.model.FButton
import vn.kingbee.widget.button.fitbutton.model.IconPosition

internal class DrawManager constructor(view: FitButton, attrs : AttributeSet?) {

    private val controller = AttributeController(view, attrs)

    private val container = ContainerDrawer(view, controller.button)
    private val icon = IconDrawer(view, controller.button)
    private val divider = DividerDrawer(view, controller.button)
    private val text = TextDrawer(view, controller.button)

    companion object Create {
        fun init(view: FitButton, attrs : AttributeSet): DrawManager {
            return DrawManager(view, attrs)
        }
    }

    /**
     * Draw customized [FitButton] on [View]
     */
    fun drawButton() : DrawManager {
        container.draw()
        defineDrawingOrder()
        return this
    }

    /**
     * Resize button measure
     * @param width new button width
     * @param height new button height
     */
    fun changeMeasure(width: Int, height: Int) {
        container.draw()
    }

    /**
     * @return [FButton] with attrs or default values
     */
    fun getButton() : FButton {
        return controller.button
    }

    private fun defineDrawingOrder() {
        val icPosition: IconPosition = getButton().iconPosition
        if (icon.isReady()) {
            if (IconPosition.LEFT == icPosition || IconPosition.TOP == icPosition) {
                icon.draw()
                if (divider.isReady()) {
                    divider.draw()
                }
                if (text.isReady()) {
                    text.draw()
                }
            } else {
                if (text.isReady()) {
                    text.draw()
                }
                if (divider.isReady()) {
                    divider.draw()
                }
                icon.draw()
            }
        } else{
            if (text.isReady()) {
                text.draw()
            }
        }
    }

}