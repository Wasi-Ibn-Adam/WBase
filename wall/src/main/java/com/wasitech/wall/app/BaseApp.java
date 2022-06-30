package com.wasitech.wall.app;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.wasitech.wall.classes.Issue;
import com.wasitech.wall.classes.Storage;

public class BaseApp extends Application {
    private static SharedPreferences pref;

    @Override
    public void onCreate() {
        super.onCreate();
        BaseApp.pref = getSharedPreferences(getPackageName(), MODE_PRIVATE);
    }

    public static SharedPreferences getPref() {
        return pref;
    }

    public static boolean isFirstInstall(Context context) {
        try {
            long firstInstallTime = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).firstInstallTime;
            long lastUpdateTime = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).lastUpdateTime;
            return firstInstallTime == lastUpdateTime;
        } catch (Exception e) {
            Issue.print(e, BaseApp.class.getName());
            return true;
        }
    }

    public static boolean isInstallFromUpdate(Context context) {
        try {
            long firstInstallTime = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).firstInstallTime;
            long lastUpdateTime = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).lastUpdateTime;
            return firstInstallTime != lastUpdateTime;
        } catch (Exception e) {
            Issue.print(e, BaseApp.class.getName());
            return false;
        }
    }

    public void onTerminate() {
        super.onTerminate();
        try {
            Storage.deleteDir(getCacheDir());
        } catch (Exception e) {
            Issue.print(e, BaseApp.class.getName());
        }
    }

}
