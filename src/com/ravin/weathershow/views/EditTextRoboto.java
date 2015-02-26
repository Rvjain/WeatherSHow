package com.ravin.weathershow.views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

public class EditTextRoboto extends EditText{

	public EditTextRoboto(Context context) {
        super(context);
        style(context);
    }

    public EditTextRoboto(Context context, AttributeSet attrs) {
        super(context, attrs);
        style(context);
    }
    
    public EditTextRoboto(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        style(context);
    }
    
    private void style(Context context) {
        Typeface tf = Typeface.createFromAsset(context.getAssets(),
                "fonts/RobotoCondensed_Regular.ttf");
        setTypeface(tf);
    }
	
}
