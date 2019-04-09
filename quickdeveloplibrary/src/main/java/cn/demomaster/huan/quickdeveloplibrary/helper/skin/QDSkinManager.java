package cn.demomaster.huan.quickdeveloplibrary.helper.skin;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class QDSkinManager {
    private Context context;
    private Resources skinResource;
    private static final QDSkinManager ourInstance = new QDSkinManager();
    private String defPackage;

    public static QDSkinManager getInstance() {
        return ourInstance;
    }

    private QDSkinManager() {
    }

    /**
     * 初始化，传入context
     *
     * @param context
     */
    public void init(Context context) {
        this.context = context;
    }

    /**
     * 加载皮肤，这里是进行模拟插件化形式，所以先把skin.apk复制到设备的sd卡。
     *
     * @param path 皮肤的apk路径  /storage/emulated/0/skin.apk
     */
    public void loadSkin(String path) {
        PackageManager packageManager = context.getPackageManager();
        //得到插件的包名
        defPackage = packageManager.getPackageArchiveInfo(path, PackageManager.GET_ACTIVITIES).packageName;
        //资源管理器
        AssetManager assetManager = null;
        try {
            assetManager = AssetManager.class.newInstance();
            Method addAssetPath = assetManager.getClass().getMethod("addAssetPath", String.class);
            addAssetPath.invoke(assetManager, path);
            //得到皮肤插件的resource
            skinResource = new Resources(assetManager, context.getResources().getDisplayMetrics(),
                    context.getResources().getConfiguration());

        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /**
     * 通过资源名 去插件中找对应的资源，用来替换宿主apk里面的。
     *
     * @param resId 是宿主view 对应的属性对应的id
     * @return
     */
    public int getColor(int resId) {
        if (skinResource == null) {
            return resId;
        }
        //1，首先通过宿主的resId找到资源名，然后因为宿主和插件apk的资源名是一样的，
        // 2，通过资源名去插件中找到对应的资源名的id
        // 3，再通过找到的id去获取插件中对应的资源的值，返回给宿主进行替换。
        String resName = context.getResources().getResourceName(resId);  //andfix.shenbin.com.skindemo:color/colorAccent
        String resName2 = resName.split("/")[1];
        int skinId = skinResource.getIdentifier(resName2, "color", defPackage);//资源名，资源类型，包名
        if (skinId == 0) {  //如果为0，就表示在插件的apk里面没有找到相同名字的资源。
            return resId;
        }
        return skinResource.getColor(resId);

    }


    /**
     * 图片资源
     * 通过资源名 去插件中找对应的资源，用来替换宿主apk里面的。
     *
     * @param resId 是宿主view 对应的属性对应的id
     * @return
     */
    public Drawable getDrawable(int resId) {
        if (skinResource == null) {
            return context.getResources().getDrawable(resId);
        }
        //1，首先通过宿主的resId找到资源名，然后因为宿主和插件apk的资源名是一样的，
        // 2，通过资源名去插件中找到对应的资源名的id
        // 3，再通过找到的id去获取插件中对应的资源的值，返回给宿主进行替换。
        String resName = context.getResources().getResourceName(resId);  //andfix.shenbin.com.skindemo:color/colorAccent
        String resName2 = resName.split("/")[1];
        int skinId = skinResource.getIdentifier(resName2, "drawable", defPackage);//资源名，资源类型，包名
        if (skinId == 0) {  //如果为0，就表示在插件的apk里面没有找到相同名字的资源。
            return context.getResources().getDrawable(resId);
        }
        return skinResource.getDrawable(skinId);

    }

}