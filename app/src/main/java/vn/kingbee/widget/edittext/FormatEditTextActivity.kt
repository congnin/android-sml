package vn.kingbee.widget.edittext

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import org.apache.commons.lang3.StringUtils
import vn.kingbee.widget.BaseActivity
import vn.kingbee.widget.R
import vn.kingbee.widget.button.fitbutton.FitButton
import vn.kingbee.widget.textview.showmore.ShowMoreTextView
import java.util.ArrayList
import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar
import vn.kingbee.widget.BuildConfig
import vn.kingbee.widget.dialog.common.CommonDialog
import vn.kingbee.widget.textview.fading.FadingTextView
import java.util.concurrent.TimeUnit

class FormatEditTextActivity : BaseActivity() {
    lateinit var edtValue1: EditText
    lateinit var edtValue2: EditText
    lateinit var edtValue3: EditText
    lateinit var edtValue4: EditText
    lateinit var edtValue5: EditText
    lateinit var edtValue6: EditText
    lateinit var ivError: ImageView
    lateinit var btValidation: FitButton
    lateinit var tvMore: ShowMoreTextView

    var lstEditTextParts: MutableList<EditText> = ArrayList()
    var lstEditTextMaxLength: MutableList<Int> = ArrayList()
    var jokes = intArrayOf(R.array.examples_1, R.array.examples_2, R.array.examples_3)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_format_edittext)

        addViews()
        addEvents()
    }

    private fun addViews() {
        edtValue1 = findViewById(R.id.edt_number_1)
        edtValue2 = findViewById(R.id.edt_number_2)
        edtValue3 = findViewById(R.id.edt_number_3)
        edtValue4 = findViewById(R.id.edt_number_4)
        edtValue5 = findViewById(R.id.edt_number_5)
        edtValue6 = findViewById(R.id.edt_number_6)
        ivError = findViewById(R.id.iv_error)
        btValidation = findViewById(R.id.bt_validate)

        lstEditTextParts.add(edtValue1)
        lstEditTextParts.add(edtValue2)
        lstEditTextParts.add(edtValue3)
        lstEditTextParts.add(edtValue4)
        lstEditTextParts.add(edtValue5)
        lstEditTextParts.add(edtValue6)

        lstEditTextMaxLength.add(MAX_LENGTH_TEXT_2)
        lstEditTextMaxLength.add(MAX_LENGTH_TEXT_3)
        lstEditTextMaxLength.add(MAX_LENGTH_TEXT_3)
        lstEditTextMaxLength.add(MAX_LENGTH_TEXT_1)
        lstEditTextMaxLength.add(MAX_LENGTH_TEXT_3)
        lstEditTextMaxLength.add(MAX_LENGTH_TEXT_3)

        tvMore = findViewById(R.id.text_view_show_more)


        //FadingTextView related code
        val fadingTextView = findViewById<FadingTextView>(R.id.fadingTextView)
        fadingTextView.setTimeout(2, TimeUnit.SECONDS)
        //Setting up the timeout seek bar
        val seekBar = findViewById<DiscreteSeekBar>(R.id.timeout_bar)
        seekBar.setOnProgressChangeListener(object : DiscreteSeekBar.OnProgressChangeListener {
            override fun onProgressChanged(seekBar: DiscreteSeekBar, value: Int, fromUser: Boolean) {
                fadingTextView.setTimeout(value.toLong(), TimeUnit.SECONDS)
                fadingTextView.forceRefresh()
            }

            override fun onStartTrackingTouch(seekBar: DiscreteSeekBar) {

            }

            override fun onStopTrackingTouch(seekBar: DiscreteSeekBar) {

            }
        })

        //Show jokes if the app is in production
        if (BuildConfig.DEBUG) {
//            fadingTextView.setTexts(jokes[(0..2).random()])
        }
    }

    private fun addEvents() {
        for (i in lstEditTextParts.indices) {
            val currentEditText = lstEditTextParts.get(i)
            val maxLength = lstEditTextMaxLength.get(i)
            //Set the behaviour for forward focusing
            currentEditText.addTextChangedListener(
                MyTextWatcher(
                    i, maxLength, lstEditTextParts
                )
            )
            //Set the behaviour for backward focusing
            currentEditText.setOnKeyListener(
                MyOnKeyListener(
                    i, maxLength, lstEditTextParts
                )
            )
            //Set the behaviour for losing focus
            currentEditText.setOnFocusChangeListener { v, hasFocus ->
                if (hasFocus) {
                    //Set border to blue
                    v.setBackgroundResource(R.drawable.edit_text_rounded_corner_input_bue)
                    val pos = (v as EditText).text.length
                    v.setSelection(pos)
                } else {
                    //Come back to gray border
                    v.setBackgroundResource(R.drawable.edit_text_rounded_corner_input)
                }
            }
        }

        btValidation.setOnClickListener {
            CommonDialog(this,
                R.string.otp_input_error_max_invalid_dialog_title,
                R.string.otp_input_error_max_invalid_dialog_message)
                .setCode("4F35#")
                .showNegativeButton()
                .setNegativeButtonString(R.string.cancel)
                .setClickListener(object : CommonDialog.ClickListener {
                    override fun onClickPositive() {
                        super.onClickPositive()
                        finish()
                    }
                })
                .showDialog()
        }

        tvMore.text = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum."
//        tvMore.setShowingLine(2);
        tvMore.setShowingChar(30);

        tvMore.addShowMoreText("Continue");
        tvMore.addShowLessText("Less");
    }

    internal class MyTextWatcher(currentIndex: Int,
                                 maxLength: Int,
                                 var lstEditTextWatcher: List<EditText>) : TextWatcher {
        var currentIndex = 0
        var maxLength = 0

        init {
            this.currentIndex = currentIndex
            this.maxLength = maxLength
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

        override fun afterTextChanged(s: Editable) {
            if (s.length >= maxLength) {
                if (currentIndex < lstEditTextWatcher.size - 1) {
                    lstEditTextWatcher[currentIndex + 1].requestFocus()
                }
                s.delete(maxLength, s.length)
            } else if (s.length == maxLength && currentIndex < lstEditTextWatcher.size - 1) {
                lstEditTextWatcher[currentIndex + 1].requestFocus()
            }
            if (s.isEmpty() && currentIndex > 0) {
                lstEditTextWatcher[currentIndex - 1].requestFocus()
            }

        }
    }


    /**
     * Class for move the cursor when user click on the Delete key and the current text is empty (no changed text event)
     */

    internal class MyOnKeyListener(currentIndex: Int,
                                   maxLength: Int,
                                   var lstEditTextWatcher: List<EditText>) : View.OnKeyListener {
        var currentIndex = 0
        var maxLength = 0

        init {
            this.currentIndex = currentIndex
            this.maxLength = maxLength
        }

        override fun onKey(v: View, keyCode: Int, event: KeyEvent): Boolean {
            if (keyCode == KeyEvent.KEYCODE_DEL) {
                val text = (v as EditText).text.toString()
                if (StringUtils.isBlank(text) && currentIndex > 0) {
                    val focusedEditText = lstEditTextWatcher[currentIndex - 1]
                    focusedEditText.requestFocus()
                    val pos = focusedEditText.text.length
                    focusedEditText.setSelection(pos)
                }
            }
            return false
        }
    }

    enum class TypeErrorEnum {
        ALL_EMPTY, INVALID
    }

    companion object {
        private const val MAX_LENGTH_TEXT_1 = 1
        private const val MAX_LENGTH_TEXT_2 = 2
        private const val MAX_LENGTH_TEXT_3 = 3
    }
}