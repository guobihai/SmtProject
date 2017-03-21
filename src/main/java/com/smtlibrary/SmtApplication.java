package com.smtlibrary;

import android.app.Application;

import com.google.code.microlog4android.config.PropertyConfigurator;
import com.smtlibrary.utils.CrashHandler;

/**
 * Created by gbh on 16/9/14.
 */
public class SmtApplication extends Application {
    private static SmtApplication smtApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        smtApplication = this;
        PropertyConfigurator.getConfigurator(this).configure();
        CrashHandler catchHandler = CrashHandler.getInstance();
        catchHandler.init(getApplicationContext());
    }

    public static SmtApplication getSmtApplication() {
        return smtApplication;
    }
}
