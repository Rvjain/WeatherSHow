package com.ravin.weathershow.views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class TextViewRoboto extends TextView{

	public TextViewRoboto(Context context) {
        super(context);
        style(context);
    }

    public TextViewRoboto(Context context, AttributeSet attrs) {
        super(context, attrs);
        style(context);
    }
    
    public TextViewRoboto(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        style(context);
    }
    
    private void style(Context context) {
        Typeface tf = Typeface.createFromAsset(context.getAssets(),
                "fonts/RobotoCondensed_Regular.ttf");
        setTypeface(tf);
    }
    
}
