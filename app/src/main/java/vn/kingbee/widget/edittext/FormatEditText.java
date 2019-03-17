package vn.kingbee.widget.edittext;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.TransformationMethod;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import timber.log.Timber;
import vn.kingbee.widget.R;

import java.util.ArrayList;
import java.util.List;

public class FormatEditText extends AppCompatEditText implements TextWatcher, View.OnKeyListener {
    private static final int MAX_LENGTH_DELETE_SPACE = 3;
    private static final char DEFAULT_MASK_CHARACTER = '•';
    private static final String SUFFIX_CHAR = " ";
    private FormatEditText.NumberEditTextListener mListener;
    private List<Integer> posAddSuffix = new ArrayList();
    private char maskCharacter = 8226;
    private int delayMask;

    public FormatEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.init(context, attrs);
    }

    public FormatEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        String formatInput = "";
        boolean isMask = false;
        char maskCharacter = this.maskCharacter;
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.FormatEditTextStyle);
        int delayTime = 0;

        try {
            formatInput = attributes.getString(R.styleable.FormatEditTextStyle_formatInput);
            isMask = attributes.getBoolean(R.styleable.FormatEditTextStyle_enableMask, false);
            delayTime = attributes.getInt(R.styleable.FormatEditTextStyle_delayMask, 1000);
        } catch (Exception var12) {
            var12.printStackTrace();
        } finally {
            attributes.recycle();
        }

        this.setOnKeyListener(this);
        this.addTextChangedListener(this);
        this.setFormatInput(formatInput);
        this.setDelayMask(delayTime);
        this.setMaskCharacter(maskCharacter);
        this.setEnableMask(isMask);
    }

    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    public void onTextChanged(CharSequence s, int start, int before, int count) {
        int selection = this.getSelectionStart();
        if (before == 0) {
            if (this.isNeedAddSuffixIdNumber(selection, s.length())) {
                this.formatInput(s, selection);
            } else if (this.isHaveCharacterAtSpacePosition(selection)) {
                this.getText().insert(selection - 1, " ");
            }
        } else if (before == 1 && selection == start) {
            this.removeChar();
        }

    }

    private void formatInput(CharSequence s, int selection) {
        if (selection != s.length()) {
            this.formatTextIfNeed(selection);
        } else {
            this.append(" ");
        }

    }

    public void afterTextChanged(Editable s) {
        if (this.mListener != null) {
            this.mListener.afterTextChange();
        }

    }

    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (keyCode == 67 && event.getAction() == 0) {
            this.onRemoveChar();
            return true;
        } else {
            return false;
        }
    }

    public void setMaxLength(int length) {
        this.setMaxEms(length);
        InputFilter[] filterArray = new InputFilter[]{new InputFilter.LengthFilter(length)};
        this.setFilters(filterArray);
    }

    public void setDelayMask(int delayTime) {
        this.delayMask = delayTime;
    }

    public void setMaskCharacter(char character) {
        this.maskCharacter = character;
    }

    public void setEnableMask(boolean isMask) {
        if (isMask) {
            this.setTransformationMethod(new CustomPasswordTransformationMethod(this.maskCharacter, this.delayMask));
        } else {
            this.setTransformationMethod((TransformationMethod)null);
        }

    }

    public void setFormatInput(String formatInput) {
        if (!TextUtils.isEmpty(formatInput)) {
            this.posAddSuffix.clear();

            for(int i = 0; i < formatInput.length(); ++i) {
                if (formatInput.charAt(i) == ' ') {
                    this.posAddSuffix.add(i);
                }
            }

            this.setMaxLength(formatInput.length());
        }

    }

    @SuppressLint({"TimberArgCount", "TimberArgTypes"})
    private void onRemoveChar() {
        int selectionStart = this.getSelectionStart();
        int selectionEnd = this.getSelectionEnd();
        int length = this.getText().length();
        String currentText = this.getText().toString();
        if (length > 0) {
            if (selectionStart == selectionEnd) {
                this.onRemoveTextAtPosition(selectionEnd, length);
            } else {
                this.getText().delete(selectionStart, selectionEnd);
            }

            int selectionStartAfterDelete = this.getSelectionStart();
            if (selectionStartAfterDelete != this.getText().length()) {
                this.formatTextIfNeed(selectionStartAfterDelete);
            }
        }

        Timber.i("onRemoveChar:  %d - %d - %s - %s - %d - %d",
                new Object[]{selectionStart, selectionEnd, currentText,
                        this.getText().toString(), this.getSelectionStart(), this.getSelectionEnd()});
    }

    @SuppressLint("TimberArgTypes")
    private void formatTextIfNeed(int selection) {
        Timber.d("format text if need: %d", new Object[]{selection});
        String text = this.trimNumber(this.getText().toString());
        if (text.length() > 0 && this.posAddSuffix != null && !this.posAddSuffix.isEmpty()) {
            StringBuilder builder = new StringBuilder(text);

            for(int i = 0; i < this.posAddSuffix.size(); ++i) {
                int pos = (Integer)this.posAddSuffix.get(i);
                if (pos < builder.length()) {
                    builder.insert(pos, " ");
                }
            }

            this.removeTextChangedListener(this);
            this.setText(builder.toString());
            this.setSelection(selection);
            this.addTextChangedListener(this);
        }

    }

    private void onRemoveTextAtPosition(int pos, int length) {
        if (pos <= length && pos > 0) {
            if (length > 3 && this.getText().charAt(pos - 1) == ' ') {
                this.getText().delete(pos - 2, pos);
            } else {
                this.getText().delete(pos - 1, pos);
            }
        }

    }

    private void removeChar() {
        int selectionStart = this.getSelectionStart();
        int selectionEnd = this.getSelectionEnd();
        int length = this.getText().length();
        if (length > 0 && selectionStart == selectionEnd) {
            this.removeTextAtPosition(selectionEnd, length);
        }

    }

    private void removeTextAtPosition(int pos, int length) {
        if (pos <= length && pos > 0 && length > 3 && this.getText().charAt(pos - 1) == ' ') {
            this.removeTextChangedListener(this);
            this.getText().delete(pos - 1, pos);
            this.addTextChangedListener(this);
        }

    }

    private String trimNumber(String number) {
        return number.trim().replace(" ", "");
    }

    private boolean isNeedAddSuffixIdNumber(int selection, int length) {
        return selection != length || this.isPosSuffixContainPos(selection);
    }

    private boolean isPosSuffixContainPos(int selection) {
        return this.posAddSuffix != null && this.posAddSuffix.contains(selection);
    }

    private boolean isHaveCharacterAtSpacePosition(int selection) {
        return this.isPosSuffixContainPos(selection - 1) && this.getText().charAt(selection - 1) != ' ';
    }

    public void setListener(FormatEditText.NumberEditTextListener listener) {
        this.mListener = listener;
    }

    public interface NumberEditTextListener {
        void afterTextChange();
    }
}
