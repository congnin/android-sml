package vn.kingbee.widget.viewpager.viewpager2

import android.os.Bundle
import android.widget.*
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.ORIENTATION_HORIZONTAL
import androidx.viewpager2.widget.ViewPager2.ORIENTATION_VERTICAL
import vn.kingbee.widget.R
import vn.kingbee.widget.viewpager.viewpager2.cards.Card
import kotlin.math.abs

abstract class BaseCardActivity : FragmentActivity() {
    protected lateinit var viewPager: ViewPager2
    private lateinit var cardSelector: Spinner
    private lateinit var smoothScrollCheckBox: CheckBox
    private lateinit var rotateCheckBox: CheckBox
    private lateinit var translateCheckBox: CheckBox
    private lateinit var scaleCheckBox: CheckBox
    private lateinit var gotoPage: Button

    private val translateX
        get() = viewPager.orientation == ORIENTATION_VERTICAL &&
                translateCheckBox.isChecked
    private val translateY
        get() = viewPager.orientation == ORIENTATION_HORIZONTAL &&
                translateCheckBox.isChecked

    protected open val layoutId: Int = R.layout.activity_no_tablayout

    private val mAnimator = ViewPager2.PageTransformer { page, position ->
        val absPos = abs(position)
        page.apply {
            rotation = if (rotateCheckBox.isChecked) position * 360 else 0f
            translationY = if (translateY) absPos * 500f else 0f
            translationX = if (translateX) absPos * 350f else 0f
            if (scaleCheckBox.isChecked) {
                val scale = if (absPos > 1) 0F else 1 - absPos
                scaleX = scale
                scaleY = scale
            } else {
                scaleX = 1f
                scaleY = 1f
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutId)

        viewPager = findViewById(R.id.view_pager)
        cardSelector = findViewById(R.id.card_spinner)
        smoothScrollCheckBox = findViewById(R.id.smooth_scroll_checkbox)
        rotateCheckBox = findViewById(R.id.rotate_checkbox)
        translateCheckBox = findViewById(R.id.translate_checkbox)
        scaleCheckBox = findViewById(R.id.scale_checkbox)
        gotoPage = findViewById(R.id.jump_button)

        UserInputController(viewPager, findViewById(R.id.disable_user_input_checkbox)).setUp()
        OrientationController(viewPager, findViewById(R.id.orientation_spinner)).setUp()
        cardSelector.adapter = createCardAdapter()

        viewPager.setPageTransformer(mAnimator)

        gotoPage.setOnClickListener {
            val card = cardSelector.selectedItemPosition
            val smoothScroll = smoothScrollCheckBox.isChecked
            viewPager.setCurrentItem(card, smoothScroll)
        }

        rotateCheckBox.setOnClickListener { viewPager.requestTransform() }
        translateCheckBox.setOnClickListener { viewPager.requestTransform() }
        scaleCheckBox.setOnClickListener { viewPager.requestTransform() }
    }

    private fun createCardAdapter(): SpinnerAdapter {
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, Card.DECK)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        return adapter
    }
}