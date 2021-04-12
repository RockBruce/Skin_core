package cn.edsmall.skin_library;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;


import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import cn.edsmall.skin_library.utils.SkinThemeUtils;

/**
 * 观察者  用来观察SkinManager类0
 */

public class SkinLayoutFactory implements LayoutInflater.Factory2, Observer {
   //控件下几个包名
    private static final String[] mClassPrefixList = {
            "android.widget.",
            "android.view.",
            "android.webkit."
    };
    private static final Class<?>[] mConstructorSignature = new Class[]{
            Context.class, AttributeSet.class};
    private static final HashMap<String, Constructor<? extends View>> sConstructorMap =
            new HashMap<String, Constructor<? extends View>>();
    private final Activity activity;

    // 属性处理类
    SkinAttribute skinAttribute;

    public SkinLayoutFactory(Activity activity) {
        this.activity=activity;
        skinAttribute = new SkinAttribute();
    }

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        //反射 classloader
        View view = createViewFromTag(name, context, attrs);
        // 如果经过createViewFromTag方法获取的View为空，那个这个view是自定义控件
        if (null == view) {
            view = createView(name, context, attrs);
        }
        //筛选符合属性的View
        skinAttribute.load(view, attrs);

        return view;
    }

    /**
     *  不是自定义View，要拼包名
     * @param name
     * @param context
     * @param attrs
     * @return
     */
    private View createViewFromTag(String name, Context context, AttributeSet attrs) {
        //包含了 . 自定义控件
        if (-1 != name.indexOf(".")) {
            //暂时不处理
            return null;
        }
        View view = null;
        for (int i = 0; i < mClassPrefixList.length; i++) {
            view = createView(mClassPrefixList[i] + name, context, attrs);
            if (null != view) {
                break;
            }
        }
        return view;
    }

    private View createView(String name, Context context, AttributeSet attrs) {
        //缓存保存解析过的View
        Constructor<? extends View> constructor = sConstructorMap.get(name);
        if (null == constructor) {
            try {
                Class<? extends View> aClass = context.getClassLoader().loadClass(name).asSubclass
                        (View.class);
                constructor = aClass.getConstructor(mConstructorSignature);
                sConstructorMap.put(name, constructor);
            } catch (Exception e) {
            }
        }
        if (null != constructor) {
            try {
                return constructor.newInstance(context, attrs);
            } catch (Exception e) {
            }
        }
        return null;
    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        return null;
    }

    @Override
    public void update(Observable o, Object arg) {
        SkinThemeUtils.updateStatusBar(activity);
        // 更换皮肤
        skinAttribute.applySkin();
    }
}
