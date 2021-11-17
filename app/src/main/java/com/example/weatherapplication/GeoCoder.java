package com.example.weatherapplication;

import android.content.Context;

import java.util.Locale;

public class GeoCoder {
    Context context;
    Locale locale;

    public GeoCoder(Context context, Locale locale) {
        this.context = context;
        this.locale = locale;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }
}
