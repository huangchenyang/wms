package com.feiruirobots.jabil.p1.ui;

import android.content.Context;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.EditText;

public class ExtendedEditText extends EditText {
    private TextWatcher mListener = null;

    public ExtendedEditText(Context ctx) {
        super(ctx);
    }

    public ExtendedEditText(Context ctx, AttributeSet attrs) {
        super(ctx, attrs);
    }

    public ExtendedEditText(Context ctx, AttributeSet attrs, int defStyle) {
        super(ctx, attrs, defStyle);
    }

    @Override
    public void addTextChangedListener(TextWatcher watcher) {
        if (mListener != null) {
            super.removeTextChangedListener(mListener);
        }
        mListener = watcher;
        super.addTextChangedListener(watcher);
    }
}
