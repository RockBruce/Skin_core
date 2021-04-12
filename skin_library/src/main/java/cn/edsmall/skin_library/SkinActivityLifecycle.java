package cn.edsmall.skin_library;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.core.view.LayoutInflaterCompat;


import java.lang.reflect.Field;
import java.util.HashMap;

import cn.edsmall.skin_library.utils.SkinThemeUtils;

/**
 * 当每个Activity启动的时候都会调用这里，跟Activity生命周期同步
 */

public class SkinActivityLifecycle implements Application.ActivityLifecycleCallbacks {

    HashMap<Activity, SkinLayoutFactory> mLayoutFactoryMap = new HashMap<>();

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        //更换状态栏
        SkinThemeUtils.updateStatusBar(activity);
        LayoutInflater layoutInflater = LayoutInflater.from(activity);
        //获得Activity的布局加载器
        try {
            //Android 布局加载器 使用 mFactorySet 标记是否设置过Factory
            //如设置过抛出一次
            //设置 mFactorySet 标签为false
            Field field = LayoutInflater.class.getDeclaredField("mFactorySet");
            field.setAccessible(true);
            field.setBoolean(layoutInflater, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        SkinLayoutFactory skinLayoutFactory = new SkinLayoutFactory(activity);
        LayoutInflaterCompat.setFactory2(layoutInflater, skinLayoutFactory);
        //注册观察者
        SkinManager.getInstance().addObserver(skinLayoutFactory);
        mLayoutFactoryMap.put(activity, skinLayoutFactory);
    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        //删除观察者
        SkinLayoutFactory skinLayoutFactory = mLayoutFactoryMap.remove(activity);
        SkinManager.getInstance().deleteObserver(skinLayoutFactory);
    }
}
