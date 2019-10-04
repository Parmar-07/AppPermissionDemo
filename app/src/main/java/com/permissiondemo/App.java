package com.permissiondemo;

import android.app.Application;

import app.exception.uncaught_logger.UCE;
import app.exception.uncaught_logger.logger.FileLogger;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        UCE.builder(this)
                .attachLogger(new FileLogger.FileLoggerBuilder()
                        .customGenerateLog("pdemo","crash.txt")
                        .build(this.getPackageName()))
                .caught();

    }



}
