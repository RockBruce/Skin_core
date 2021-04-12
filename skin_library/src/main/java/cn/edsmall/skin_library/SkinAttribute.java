package cn.edsmall.skin_library;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.view.ViewCompat;

import java.util.ArrayList;
import java.util.List;

import cn.edsmall.skin_library.utils.SkinResources;
import cn.edsmall.skin_library.utils.SkinThemeUtils;

/**
 * Created by Administrator on 2018/3/16 0016.
 */

public class SkinAttribute {
   //可以替换的属性名
    private static final List<String> mAttributes = new ArrayList<>();

    List<SkinView> mSkinViews = new ArrayList<>();
    static {
        mAttributes.add("background");
        mAttributes.add("src");
        mAttributes.add("textColor");
        mAttributes.add("drawableLeft");
        mAttributes.add("drawableTop");
        mAttributes.add("drawableRight");
        mAttributes.add("drawableBottom");
    }
    

    public void load(View view, AttributeSet attrs) {
        //可以属性的节点结合（比如TextView、ImageView）
        List<SkinPair> skinPairs = new ArrayList<>();
        for (int i = 0; i < attrs.getAttributeCount(); i++) {
            //获得xml节点下的属性名例如src
            String attributeName = attrs.getAttributeName(i);
            //获取到的attributeName（属性名） 需要筛选的属性名
            if (mAttributes.contains(attributeName)) {
                String attributeValue = attrs.getAttributeValue(i);
                //写死了 不管了如果xml中以#222222开头，这是不能进行换肤
                if (attributeValue.startsWith("#")) {
                    continue;
                }
                //资源id
                int resId;
                //android:textColor="?attr/colorAccent"
                if (attributeValue.startsWith("?")) {
                    //当前主题下attr Id
                    int attrId = Integer.parseInt(attributeValue.substring(1));
                    //获得 主题 style 中的 对应 attr 的资源id值
                    resId = SkinThemeUtils.getResId(view.getContext(), new int[]{attrId})[0];
                } else {
                    // @12343455332
                    resId = Integer.parseInt(attributeValue.substring(1));
                }
                if (resId != 0) {
                    //可以被替换的属性
                    SkinPair skinPair = new SkinPair(attributeName, resId);
                    skinPairs.add(skinPair);
                }
            }
        }
        //将View与之对应的可以动态替换的属性集合 放入 集合中
        if (!skinPairs.isEmpty()) {
            SkinView skinView = new SkinView(view, skinPairs);
            skinView.applySkin();
            mSkinViews.add(skinView);
        }
    }

    /**
     * 换皮肤
     */
    public void applySkin() {
        for (SkinView mSkinView : mSkinViews) {
            mSkinView.applySkin();
        }
    }

    /**
     * view 对应可换(background->resId)
     */
    static class SkinView {
        View view;
        List<SkinPair> skinPairs;

        public SkinView(View view, List<SkinPair> skinPairs) {
            this.view = view;
            this.skinPairs = skinPairs;
        }

        public void applySkin() {
            for (SkinPair skinPair : skinPairs) {
                Drawable left = null, top = null, right = null, bottom = null;
                switch (skinPair.attributeName) {
                    case "background":
                        Object background = SkinResources.getInstance().getBackground(skinPair
                                .resId);
                        //Color
                        if (background instanceof Integer) {
                            view.setBackgroundColor((Integer) background);
                        } else {
                            ViewCompat.setBackground(view, (Drawable) background);
                        }
                        break;
                    case "src":
                        background = SkinResources.getInstance().getBackground(skinPair
                                .resId);
                        if (background instanceof Integer) {
                            ((ImageView) view).setImageDrawable(new ColorDrawable((Integer)
                                    background));
                        } else {
                            ((ImageView) view).setImageDrawable((Drawable) background);
                        }
                        break;
                    case "textColor":
                        ((TextView) view).setTextColor(SkinResources.getInstance().getColorStateList
                                (skinPair.resId));
                        break;
                    case "drawableLeft":
                        left = SkinResources.getInstance().getDrawable(skinPair.resId);
                        break;
                    case "drawableTop":
                        top = SkinResources.getInstance().getDrawable(skinPair.resId);
                        break;
                    case "drawableRight":
                        right = SkinResources.getInstance().getDrawable(skinPair.resId);
                        break;
                    case "drawableBottom":
                        bottom = SkinResources.getInstance().getDrawable(skinPair.resId);
                        break;
                    default:
                        break;
                }
                if (null != left || null != right || null != top || null != bottom) {
                    ((TextView) view).setCompoundDrawablesWithIntrinsicBounds(left, top, right,
                            bottom);
                }
            }
        }
    }

    /**
     * 可以属性名对应的资源id
     */
    static class SkinPair {
        String attributeName;
        int resId;
        public SkinPair(String attributeName, int resId) {
            this.attributeName = attributeName;
            this.resId = resId;
        }
    }
}
