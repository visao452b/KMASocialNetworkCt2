package com.example.kmasocialnetworkct2.function;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import org.jetbrains.annotations.NotNull;

public class dbTextView extends AppCompatTextView {

    public dbTextView(@NonNull @NotNull Context context) {
        super(context);
        setFontsTextView();
    }

    public dbTextView(@NonNull @NotNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs) {
        super(context, attrs);
        setFontsTextView();
    }

    public dbTextView(@NonNull @NotNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setFontsTextView();
    }

    private void setFontsTextView(){
        Typeface typeface = Utils.getDbTypeface(getContext());
        setTypeface(typeface);
    }
}
