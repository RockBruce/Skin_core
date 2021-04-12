package cn.edsmall.skin_core;

import android.app.Application;

import cn.edsmall.skin_library.SkinManager;

public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        SkinManager.init(this);
    }
}
