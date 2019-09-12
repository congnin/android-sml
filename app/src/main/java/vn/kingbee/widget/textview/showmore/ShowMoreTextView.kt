package vn.kingbee.widget.textview.showmore

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.ViewTreeObserver
import android.widget.TextView
import timber.log.Timber
import kotlin.Exception
import android.text.method.LinkMovementMethod
import android.text.Spannable
import android.text.style.ForegroundColorSpan
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.text.SpannableString
import android.view.View


class ShowMoreTextView : TextView {
    private var showingLine = 1
    private var showingChar: Int = 0
    private var isCharEnable: Boolean = false

    private var showMore = "Show more"
    private var showLess = "Show less"
    private var dotdot = "..."

    private var MAGIC_NUMBER = 5

    private var showMoreTextColor = Color.RED
    private var showLessTextColor = Color.RED

    private var mainText: String? = null

    private var isAlreadySet: Boolean = false

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    override fun onFinishInflate() {
        super.onFinishInflate()
        mainText = text.toString()
    }

    private fun addShowMore() {
        val vto = viewTreeObserver
        vto.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                val text = text.toString()
                if (!isAlreadySet) {
                    mainText = text
                    isAlreadySet = true
                }
                var showingText = ""
                if (isCharEnable) {
                    if (showingChar >= text.length) {
                        try {
                            throw Exception("Character count cannot be exceed total line count")
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                    var newText = text.substring(0, showingChar)
                    newText += dotdot + showMore

                    SaveState.isCollapse = true

                    setText(newText)
                    Timber.d("Text: %s", newText)
                } else {
                    if (showingLine >= lineCount) {
                        try {
                            throw Exception("Line Number cannot be exceed total line count")
                        } catch (e: Exception) {
                            e.printStackTrace()
                            Timber.e("Error: %s", e.message)
                        }

                        viewTreeObserver.removeOnGlobalLayoutListener(this)
                        return
                    }
                    var start = 0
                    var end: Int
                    for (i in 0 until showingLine) {
                        end = layout.getLineEnd(i)
                        showingText += text.substring(start, end)
                        start = end
                    }

                    var newText = showingText.substring(0, showingText.length
                            - (dotdot.length + showMore.length + MAGIC_NUMBER))
                    Timber.d("Text: $newText")
                    Timber.d("Text: $showingText")
                    newText += dotdot + showMore

                    SaveState.isCollapse = true

                    setText(newText)
                }
                setShowMoreColoringAndClickable()
                viewTreeObserver.removeOnGlobalLayoutListener(this)
            }

        })
    }

    private fun setShowMoreColoringAndClickable() {
        val spannableString = SpannableString(text)

        Timber.d("Text: $text")
        spannableString.setSpan(object : ClickableSpan() {
            override fun updateDrawState(ds: TextPaint) {
                ds.isUnderlineText = false
            }

            override fun onClick(view: View?) {
                maxLines = Integer.MAX_VALUE
                text = mainText
                SaveState.isCollapse = false
                showLessButton()
                Timber.d("Item clicked: $mainText")

            }
        },
            text.length - (dotdot.length + showMore.length),
            text.length, 0)

        spannableString.setSpan(ForegroundColorSpan(showMoreTextColor),
            text.length - (dotdot.length + showMore.length),
            text.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        movementMethod = LinkMovementMethod.getInstance()
        setText(spannableString, TextView.BufferType.SPANNABLE)
    }

    private fun showLessButton() {

        val text = text.toString() + dotdot + showLess
        val spannableString = SpannableString(text)

        spannableString.setSpan(object : ClickableSpan() {
            override fun updateDrawState(ds: TextPaint) {
                ds.isUnderlineText = false
            }

            override fun onClick(view: View?) {

                maxLines = showingLine

                addShowMore()

                Timber.d("Item clicked: ")

            }
        },
            text.length - (dotdot.length + showLess.length),
            text.length, 0)

        spannableString.setSpan(ForegroundColorSpan(showLessTextColor),
            text.length - (dotdot.length + showLess.length),
            text.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        movementMethod = LinkMovementMethod.getInstance()
        setText(spannableString, TextView.BufferType.SPANNABLE)
    }


    /*
     * User added field
     * */

    /**
     * User can add minimum line number to show collapse text
     *
     * @param lineNumber int
     */
    fun setShowingLine(lineNumber: Int) {
        if (lineNumber == 0) {
            try {
                throw Exception("Line Number cannot be 0")
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return
        }

        isCharEnable = false

        showingLine = lineNumber

        maxLines = showingLine

        if (SaveState.isCollapse) {
            addShowMore()
        } else {
            maxLines = Integer.MAX_VALUE
            showLessButton()
        }

    }

    /**
     * User can limit character limit of text
     *
     * @param character int
     */
    fun setShowingChar(character: Int) {
        if (character == 0) {
            try {
                throw Exception("Character length cannot be 0")
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return
        }

        isCharEnable = true
        this.showingChar = character

        if (SaveState.isCollapse) {
            addShowMore()
        } else {
            maxLines = Integer.MAX_VALUE
            showLessButton()
        }
    }

    /**
     * User can add their own  show more text
     *
     * @param text String
     */
    fun addShowMoreText(text: String) {
        showMore = text
    }

    /**
     * User can add their own show less text
     *
     * @param text String
     */
    fun addShowLessText(text: String) {
        showLess = text
    }

    /**
     * User Can add show more text color
     *
     * @param color Integer
     */
    fun setShowMoreColor(color: Int) {
        showMoreTextColor = color
    }

    /**
     * User can add show less text color
     *
     * @param color Integer
     */
    fun setShowLessTextColor(color: Int) {
        showLessTextColor = color
    }
}