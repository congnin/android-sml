package vn.kingbee.widget.textview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.View;
import vn.kingbee.widget.R;
import vn.kingbee.widget.utils.CommonUtils;

public class SATextView extends AppCompatTextView implements View.OnClickListener {

    protected OnClickListener onClickListener;

    private String[] mTypeFaceList = {
            "Aachen_Bold.ttf",
            "helvetica_neue_medium_italic.ttf",
            "HelveticaNeue.ttf",
            "HelveticaNeue_Italic.otf",
            "HelveticaNeue_LightItalic.ttf",
            "HelveticaNeue_UltraLight.otf",
            "HelveticaNeue_Light.ttf",
            "Roboto_Regular.ttf",
            "Roboto_Medium.ttf",
            "Roboto_Light.ttf",
            "Roboto_Bold.ttf"};

    public SATextView(Context context) {
        super(context);
    }

    public SATextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public SATextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        int fontIndex;
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.SATextView, 0, 0);
        try {
            fontIndex = ta.getInteger(R.styleable.SATextView_typeFace, -1);
        } finally {
            ta.recycle();
        }

        if (fontIndex != -1) {
            Typeface typeFace = Typeface.createFromAsset(getContext().getAssets(), "fonts/" + mTypeFaceList[fontIndex]);
            setTypeface(typeFace);
        }
    }

    @Override
    public void onClick(View v) {
        if (CommonUtils.Companion.isClickAvailable() && onClickListener != null) {
            onClickListener.onClick(v);
        }
    }

    @Override
    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
        super.setOnClickListener(this);
    }
}
