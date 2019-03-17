package vn.kingbee.widget.edittext;

import android.graphics.Rect;
import android.os.Handler;
import android.os.SystemClock;
import android.text.*;
import android.text.method.TransformationMethod;
import android.text.style.UpdateLayout;
import android.view.View;
import org.jetbrains.annotations.NotNull;

import java.lang.ref.WeakReference;

public class CustomPasswordTransformationMethod implements TransformationMethod, TextWatcher {
    static final Object ACTIVE = new Concrete();
    public static final char DEFAULT_MASK_CHARACTER = '•';
    public static final int DELAY_SHOW_PASSWORD = 1000;
    private static CustomPasswordTransformationMethod sInstance;
    private char maskCharacter;
    private int delayTime;

    public CustomPasswordTransformationMethod() {
        this(DEFAULT_MASK_CHARACTER, DELAY_SHOW_PASSWORD);
    }

    public CustomPasswordTransformationMethod(char maskCharacter, int delayShowPassword) {
        this.maskCharacter = 8226;
        this.delayTime = DELAY_SHOW_PASSWORD;
        this.maskCharacter = maskCharacter;
        this.delayTime = delayShowPassword;
    }

    public CharSequence getTransformation(CharSequence source, View view) {
        if (source instanceof Spannable) {
            Spannable sp = (Spannable)source;
            CustomPasswordTransformationMethod.ViewReference[] vr = (CustomPasswordTransformationMethod.ViewReference[])sp.getSpans(0, sp.length(), CustomPasswordTransformationMethod.ViewReference.class);

            for(int i = 0; i < vr.length; ++i) {
                sp.removeSpan(vr[i]);
            }

            removeVisibleSpans(sp);
            sp.setSpan(new CustomPasswordTransformationMethod.ViewReference(view), 0, 0, 34);
        }

        return new CustomPasswordTransformationMethod.PasswordCharSequence(source, this.maskCharacter);
    }

    public static CustomPasswordTransformationMethod getInstance() {
        if (sInstance != null) {
            return sInstance;
        } else {
            sInstance = new CustomPasswordTransformationMethod();
            return sInstance;
        }
    }

    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s instanceof Spannable) {
            Spannable sp = (Spannable)s;
            CustomPasswordTransformationMethod.ViewReference[] vr = (CustomPasswordTransformationMethod.ViewReference[])sp.getSpans(0, s.length(), CustomPasswordTransformationMethod.ViewReference.class);
            if (vr.length == 0) {
                return;
            }

            View v = null;

            for(int i = 0; v == null && i < vr.length; ++i) {
                v = (View)vr[i].get();
            }

            if (v == null) {
                return;
            }

            removeVisibleSpans(sp);
            if (sp.length() >= start) {
                sp.setSpan(new CustomPasswordTransformationMethod.Visible(sp, this, this.delayTime), start, start + count, 33);
            }
        }

    }

    public void afterTextChanged(Editable s) {
    }

    public void onFocusChanged(View view, CharSequence sourceText, boolean focused, int direction, Rect previouslyFocusedRect) {
        if (!focused && sourceText instanceof Spannable) {
            Spannable sp = (Spannable)sourceText;
            removeVisibleSpans(sp);
        }

    }

    private static void removeVisibleSpans(Spannable sp) {
        CustomPasswordTransformationMethod.Visible[] old =
                sp.getSpans(0, sp.length(), Visible.class);

        for (Visible anOld : old) {
            sp.removeSpan(anOld);
        }

    }

    private static class ViewReference extends WeakReference<View> implements NoCopySpan {
        public ViewReference(View v) {
            super(v);
        }
    }

    private static class Visible extends Handler implements UpdateLayout, Runnable {
        private Spannable mText;
        private CustomPasswordTransformationMethod mTransformer;

        public Visible(Spannable sp, CustomPasswordTransformationMethod ptm, int delayTime) {
            this.mText = sp;
            this.mTransformer = ptm;
            this.postAtTime(this, SystemClock.uptimeMillis() + (long)delayTime);
        }

        public void run() {
            this.mText.removeSpan(this);
        }
    }

    private static class PasswordCharSequence implements CharSequence, GetChars {
        private CharSequence mSource;
        private char maskCharacter;

        public PasswordCharSequence(CharSequence source, char maskCharacter) {
            this.mSource = source;
            this.maskCharacter = maskCharacter;
        }

        public int length() {
            return this.mSource.length();
        }

        public char charAt(int i) {
            if (this.mSource instanceof Spanned) {
                Spanned sp = (Spanned)this.mSource;
                int st = sp.getSpanStart(CustomPasswordTransformationMethod.ACTIVE);
                int en = sp.getSpanEnd(CustomPasswordTransformationMethod.ACTIVE);
                if (i >= st && i < en) {
                    return this.mSource.charAt(i);
                }

                CustomPasswordTransformationMethod.Visible[] visible = (CustomPasswordTransformationMethod.Visible[])sp.getSpans(0, sp.length(), CustomPasswordTransformationMethod.Visible.class);

                for (Visible aVisible : visible) {
                    if (sp.getSpanStart(aVisible.mTransformer) >= 0) {
                        st = sp.getSpanStart(aVisible);
                        en = sp.getSpanEnd(aVisible);
                        if (i >= st && i < en) {
                            return this.mSource.charAt(i);
                        }
                    }
                }
            }

            return this.getCharacterMaskIfNeed(i);
        }

        public CharSequence subSequence(int start, int end) {
            char[] buf = new char[end - start];
            this.getChars(start, end, buf, 0);
            return new String(buf);
        }

        @NotNull
        public String toString() {
            return this.subSequence(0, this.length()).toString();
        }

        public void getChars(int start, int end, char[] dest, int off) {
            TextUtils.getChars(this.mSource, start, end, dest, off);
            int st = -1;
            int en = -1;
            int nvisible = 0;
            int[] starts = null;
            int[] ends = null;
            int a;
            if (this.mSource instanceof Spanned) {
                Spanned sp = (Spanned)this.mSource;
                st = sp.getSpanStart(CustomPasswordTransformationMethod.ACTIVE);
                en = sp.getSpanEnd(CustomPasswordTransformationMethod.ACTIVE);
                CustomPasswordTransformationMethod.Visible[] visible = (CustomPasswordTransformationMethod.Visible[])sp.getSpans(0, sp.length(), CustomPasswordTransformationMethod.Visible.class);
                nvisible = visible.length;
                starts = new int[nvisible];
                ends = new int[nvisible];

                for(a = 0; a < nvisible; ++a) {
                    if (sp.getSpanStart(visible[a].mTransformer) >= 0) {
                        starts[a] = sp.getSpanStart(visible[a]);
                        ends[a] = sp.getSpanEnd(visible[a]);
                    }
                }
            }

            for(int i = start; i < end; ++i) {
                if (i < st || i >= en) {
                    boolean visible = false;

                    for(a = 0; a < nvisible; ++a) {
                        if (i >= starts[a] && i < ends[a]) {
                            visible = true;
                            break;
                        }
                    }

                    if (!visible) {
                        dest[i - start + off] = this.getCharacterMaskIfNeed(i);
                    }
                }
            }

        }

        private char getCharacterMaskIfNeed(int i) {
            return this.mSource.charAt(i) != ' ' ? this.maskCharacter : this.mSource.charAt(i);
        }
    }
}
