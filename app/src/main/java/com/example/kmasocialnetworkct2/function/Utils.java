package com.example.kmasocialnetworkct2.function;

import android.content.Context;
import android.graphics.Typeface;

public class Utils {
    private static Typeface bTypeface;
    private static Typeface dbTypeface;
    private static Typeface rTypeface;

    public static Typeface getbTypeface(Context context) {
        if (bTypeface == null) {
            bTypeface = Typeface.createFromAsset(context.getAssets(), "fonts/BRLNSB.TTF");
        }
        return bTypeface;
    }

    public static Typeface getDbTypeface(Context context) {
        if (dbTypeface == null) {
            dbTypeface = Typeface.createFromAsset(context.getAssets(), "fonts/BRLNSDB.TTF");
        }
        return dbTypeface;
    }

    public static Typeface getrTypeface(Context context) {

        if (rTypeface == null) {
            rTypeface = Typeface.createFromAsset(context.getAssets(), "fonts/BRLNSR.TTF.TTF");
        }
        return rTypeface;
    }
}
