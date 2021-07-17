package cn.demomaster.huan.quickdeveloplibrary.view.banner.qdlayout;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import cn.demomaster.huan.quickdeveloplibrary.util.xml.DomSaxHandler;
import cn.demomaster.huan.quickdeveloplibrary.util.xml.NodeElement;
import cn.demomaster.huan.quickdeveloplibrary.view.banner.AdsResource;
import cn.demomaster.huan.quickdeveloplibrary.view.banner.Banner;
import cn.demomaster.huan.quickdeveloplibrary.view.banner.BannerContentType;
import cn.demomaster.huan.quickdeveloplibrary.view.banner.BannerFileType;
import cn.demomaster.huan.quickdeveloplibrary.view.webview.QDWebView;
import cn.demomaster.qdlogger_library.QDLogger;

import static cn.demomaster.huan.quickdeveloplibrary.view.banner.AdsResource.backgroundColorDefault;
import static cn.demomaster.huan.quickdeveloplibrary.view.banner.AdsResource.layout_gravityDefault;
import static cn.demomaster.huan.quickdeveloplibrary.view.banner.AdsResource.textBackgroundColorDefault;
import static cn.demomaster.huan.quickdeveloplibrary.view.banner.AdsResource.textColorDefault;
import static cn.demomaster.huan.quickdeveloplibrary.view.banner.AdsResource.textGravityDefault;
import static cn.demomaster.huan.quickdeveloplibrary.view.banner.AdsResource.textSizeDefault;

public class AjsLayoutInflater {

    public static NodeElement parseXmlFile(Context context, File file, DomSaxHandler.OnParseCompleteListener onParseCompleteListener) {
        XMLReader xr = null;
        try {
            //使用工厂方法初始化SAXParserFactory变量spf
            SAXParserFactory spf = SAXParserFactory.newInstance();
            //通过SAXParserFactory得到SAXParser的实例
            SAXParser sp = spf.newSAXParser();
            //通过SAXParser得到XMLReader的实例
            xr = sp.getXMLReader();

            DomSaxHandler handler = new DomSaxHandler(context, onParseCompleteListener);
            //handler.setUseLowerCase(true);//使用小写转换
            xr.setContentHandler(handler);
            xr.setErrorHandler(handler);

            //QDLogger.e("解析xml：" + file.getAbsolutePath());
            FileInputStream is = new FileInputStream(file);
            //通过获取到的InputStream来得到InputSource实例
            InputSource is2 = new InputSource(is);
            xr.parse(is2);
            return handler.getRootElement();
        } catch (SAXException e) {
            QDLogger.e(e);
        } catch (IOException e) {
            QDLogger.e(e);
        } catch (ParserConfigurationException e) {
            QDLogger.e(e);
        }
        return null;
    }

    public static void parseXmlString(String txt) {
        
    }

    // 泛型方法 printArray
    public static <E> void printArray(E[] inputArray) {
        // 输出数组元素
        for (E element : inputArray) {
            System.out.printf("%s ", element);
        }
    }

    /**
     * 解析资源文件中的xml文件
     *
     * @param <T>
     * @param context
     * @param xmlPath
     * @param onParseCompleteListener
     * @return
     */
    public static <T> NodeElement parseXmlAssets(Context context, String xmlPath, DomSaxHandler.OnParseCompleteListener onParseCompleteListener) {
        try {
            //使用工厂方法初始化SAXParserFactory变量spf
            SAXParserFactory spf = SAXParserFactory.newInstance();
            //通过SAXParserFactory得到SAXParser的实例
            SAXParser sp = spf.newSAXParser();
            //通过SAXParser得到XMLReader的实例
            XMLReader xr = sp.getXMLReader();

            DomSaxHandler handler = new DomSaxHandler(context, onParseCompleteListener);
            //handler.setUseLowerCase(true);//使用小写转换
            xr.setContentHandler(handler);
            xr.setErrorHandler(handler);

            //获取AssetManager管理器对象
            AssetManager as = context.getAssets();
            //通过AssetManager的open方法获取到beauties.xml文件的输入流
            InputStream is = as.open(xmlPath);
            //通过获取到的InputStream来得到InputSource实例
            InputSource is2 = new InputSource(is);
            xr.parse(is2);
            return handler.getRootElement();
        } catch (SAXException e) {
            QDLogger.e(e);
        } catch (IOException e) {
            QDLogger.e(e);
        } catch (ParserConfigurationException e) {
            QDLogger.e(e);
        }
        return null;
    }

    /**
     * 生成布局
     *
     * @param context
     * @param parent
     * @param element
     * @return
     */
    public static View generateLayout(Context context, ViewGroup parent, Element element) {
        View view = null;
        if (element != null) {
            if (element.getTag().equals("layout")) {
                LinearLayout linearLayout = new LinearLayout(context);
                linearLayout.setOrientation(element.getOrientation());
                //linearLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));
                element.getOrientation();
                view = linearLayout;
            } else if (element.getTag().equals("lable")) {
                TextView textView = new TextView(context);
                textView.setText(element.getText());
                view = textView;
            } else if (element.getTag().equals("image")) {
                ImageView imageView = new ImageView(context);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                Glide.with(context).load(element.getSrc()).into(imageView);
                view = imageView;
            } else if (element.getTag().equals("button")) {
                Button button = new Button(context);
                button.setText(element.getText());
                view = button;
            } else if (element.getTag().equalsIgnoreCase("PercentLayout")) {
                PercentLayout percentLayout = new PercentLayout(context);
                //int row = Integer.valueOf(element.getAttribute("row"));
                //gridView.setRow(row);
                //int column = Integer.valueOf(element.getAttribute("column"));
                //gridView.setColunms(column);
                String backgroundColor = element.getAttribute("backgroundColor");
                try {
                    if (!TextUtils.isEmpty(backgroundColor)) {
                        int color = Color.parseColor(backgroundColor);
                        percentLayout.setBackgroundColor(color);
                    }
                } catch (Exception e) {
                    Log.e("", "背景色颜色解析失败：" + backgroundColor + "," + e.getMessage());
                }
                percentLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                view = percentLayout;
            } else if (element.getTag().equalsIgnoreCase("banner")) {
                Banner banner = new Banner(context);
                String indicator = element.getAttribute("indicator");
                if (!TextUtils.isEmpty(indicator)) {
                    if (indicator.equalsIgnoreCase("none")) {
                        banner.setIndicatorStyle(Banner.IndicatorStyle.None);
                    } else if (indicator.equalsIgnoreCase("circle")) {
                        banner.setIndicatorStyle(Banner.IndicatorStyle.Circle);
                    } else if (indicator.equalsIgnoreCase("rectangle")) {
                        banner.setIndicatorStyle(Banner.IndicatorStyle.Rectangle);
                    }
                } else {
                    banner.setIndicatorStyle(Banner.IndicatorStyle.None);
                }

                String direction = element.getAttribute("direction");
                if (!TextUtils.isEmpty(direction)) {
                    if (direction.equalsIgnoreCase("horizontal")) {
                        banner.setDirection(LinearLayout.HORIZONTAL);
                    } else {
                        banner.setDirection(LinearLayout.VERTICAL);
                    }
                }

                String durationStr = element.getAttribute("duration");
                if (!TextUtils.isEmpty(durationStr)) {
                    int duration = Integer.valueOf(durationStr);
                    banner.setLoopTime(duration);
                }

                String gravityStr = element.getAttribute("gravity");
                int layout_gravity = layout_gravityDefault;//UNSPECIFIED_GRAVITY;
                if (!TextUtils.isEmpty(gravityStr)) {
                    String[] gravitys = gravityStr.split("\\|");
                    int gravity = 0;
                    for (String str : gravitys) {
                        if (str.equalsIgnoreCase("top")) {
                            gravity = gravity | Gravity.TOP;
                        } else if (str.equalsIgnoreCase("bottom")) {
                            gravity = gravity | Gravity.BOTTOM;
                        } else if (str.equalsIgnoreCase("left")) {
                            gravity = gravity | Gravity.LEFT;
                        } else if (str.equalsIgnoreCase("right")) {
                            gravity = gravity | Gravity.RIGHT;
                        } else if (str.equalsIgnoreCase("center")) {
                            gravity = gravity | Gravity.CENTER;
                        } else if (str.equalsIgnoreCase("CENTER_VERTICAL")) {
                            gravity = gravity | Gravity.CENTER_VERTICAL;
                        } else if (str.equalsIgnoreCase("CENTER_HORIZONTAL")) {
                            gravity = gravity | Gravity.CENTER_HORIZONTAL;
                        }
                    }
                    if (gravity != 0) {
                        layout_gravity = gravity;
                    }
                }

                String textGravityStr = element.getAttribute("textGravity");
                int textGravity = textGravityDefault;//UNSPECIFIED_GRAVITY;
                if (!TextUtils.isEmpty(textGravityStr)) {
                    String[] gravitys = textGravityStr.split("\\|");
                    int gravity = 0;
                    for (String str : gravitys) {
                        if (str.equalsIgnoreCase("top")) {
                            gravity = gravity | Gravity.TOP;
                        } else if (str.equalsIgnoreCase("bottom")) {
                            gravity = gravity | Gravity.BOTTOM;
                        } else if (str.equalsIgnoreCase("left")) {
                            gravity = gravity | Gravity.LEFT;
                        } else if (str.equalsIgnoreCase("right")) {
                            gravity = gravity | Gravity.RIGHT;
                        } else if (str.equalsIgnoreCase("center")) {
                            gravity = gravity | Gravity.CENTER;
                        } else if (str.equalsIgnoreCase("CENTER_VERTICAL")) {
                            gravity = gravity | Gravity.CENTER_VERTICAL;
                        } else if (str.equalsIgnoreCase("CENTER_HORIZONTAL")) {
                            gravity = gravity | Gravity.CENTER_HORIZONTAL;
                        }
                    }
                    if (gravity != 0) {
                        textGravity = gravity;
                    }
                }

                String desc = element.getAttribute("desc");
                String[] descArray = new String[0];
                if (!TextUtils.isEmpty(desc)) {
                    descArray = desc.trim().split(";");
                }
                String[] urls = new String[0];
                String url = element.getAttribute("url");
                if (!TextUtils.isEmpty(url)) {
                    urls = url.trim().split(";");
                }

                //解析文本大小
                String textSizeStrs = element.getAttribute("textSize");
                Float[] textSizesArray = new Float[0];
                if (!TextUtils.isEmpty(textSizeStrs)) {
                    String[] textSizeArray = textSizeStrs.trim().split(",");
                    if (textSizeArray != null && textSizeArray.length > 0) {
                        List<Float> textSizesList = new ArrayList<>();
                        for (int i = 0; i < textSizeArray.length; i++) {
                            String str = textSizeArray[i];
                            if (!TextUtils.isEmpty(str)) {
                                try {
                                    float textSize = Float.valueOf(str);
                                    textSizesList.add(textSize);
                                } catch (Exception e) {
                                    System.out.println("解析文本大小异常：" + str);
                                }
                            }
                        }
                        textSizesArray = textSizesList.toArray(new Float[textSizesList.size()]);
                    }
                }

                //解析圆角配置
                String radiusStrs = element.getAttribute("radius");
                float[] radiusArray = new float[8];
                if (!TextUtils.isEmpty(radiusStrs)) {
                    String[] radiusStrArray = radiusStrs.trim().split(",");
                    if (radiusStrArray != null && radiusStrArray.length > 0) {
                        if (radiusStrArray.length == 1) {
                            try {
                                float radius = Float.valueOf(radiusStrArray[0]);
                                radiusArray = new float[]{radius, radius, radius, radius, radius, radius, radius, radius};
                            } catch (Exception e) {
                                System.out.println("圆角转换异常");
                            }
                        } else {
                            for (int i = 0; i < Math.min(radiusStrArray.length, 4); i++) {
                                String str = radiusStrArray[i];
                                if (!TextUtils.isEmpty(str)) {
                                    try {
                                        float radius = Float.valueOf(str);
                                        radiusArray[i * 2] = radius;
                                        radiusArray[i * 2 + 1] = radius;
                                    } catch (Exception e) {
                                        System.out.println("圆角转换异常：" + str);
                                    }
                                }
                            }
                        }
                    }
                }

                //解析文本颜色配置
                String textColorStrs = element.getAttribute("textColor");
                Integer[] textColorsArray = new Integer[0];
                if (!TextUtils.isEmpty(textColorStrs)) {
                    String[] textColorStrArray = textColorStrs.trim().split(";");
                    List<Integer> colorList = new ArrayList<>();
                    for (String str : textColorStrArray) {
                        int color = textColorDefault;
                        try {
                            if (!TextUtils.isEmpty(str)) {
                                color = Color.parseColor(str);
                            }
                        } catch (Exception e) {
                            System.out.println("颜色转换异常：" + str);
                        }
                        colorList.add(color);
                    }
                    textColorsArray = colorList.toArray(new Integer[colorList.size()]);
                }

                //解析文本背景颜色配置
                String textBackgroundColorStrs = element.getAttribute("textBackgroundColor");
                Integer[] textBackgroundColorsArray = new Integer[0];
                if (!TextUtils.isEmpty(textBackgroundColorStrs)) {
                    String[] textBackgroundColorStr = textBackgroundColorStrs.trim().split(";");
                    List<Integer> colorList = new ArrayList<>();
                    for (String str : textBackgroundColorStr) {
                        int color = textBackgroundColorDefault;
                        try {
                            color = Color.parseColor(str);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        colorList.add(color);
                    }
                    textBackgroundColorsArray = colorList.toArray(new Integer[colorList.size()]);
                }
                //解析布局背景色配置
                String backgroundColorStrs = element.getAttribute("backgroundColor");
                Integer[] backgroundColorsArray = new Integer[0];
                if (!TextUtils.isEmpty(backgroundColorStrs)) {
                    String[] backgroundColorStr = backgroundColorStrs.trim().split(";");
                    List<Integer> colorList = new ArrayList<>();
                    for (String str : backgroundColorStr) {
                        int color = backgroundColorDefault;
                        try {
                            color = Color.parseColor(str);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        colorList.add(color);
                    }
                    backgroundColorsArray = colorList.toArray(new Integer[colorList.size()]);
                }

                if (descArray.length > 0 || urls.length > 0) {
                    List<AdsResource> list = new ArrayList<AdsResource>();
                    int count = Math.max(descArray.length, urls.length);
                    for (int i = 0; i < count; i++) {
                        AdsResource adsResource = new AdsResource();
                        String urlstr = null;
                        if (urls.length > i) {
                            urlstr = urls[i];
                        }
                        String descStr = null;
                        if (descArray.length > i) {
                            descStr = descArray[i];
                        }
                        if (backgroundColorsArray.length > i) {
                            int backgroundColor = backgroundColorDefault;
                            backgroundColor = backgroundColorsArray[i];
                            adsResource.setBackgroundColor(backgroundColor);
                        }


                        if (textSizesArray.length > i) {
                            float textSize = textSizeDefault;
                            textSize = textSizesArray[i];
                            adsResource.setTextSize(textSize);
                        }

                        if (textColorsArray.length > i) {
                            int textColor = textColorDefault;
                            textColor = textColorsArray[i];
                            adsResource.setTextColor(textColor);
                        }

                        if (textBackgroundColorsArray.length > i) {
                            int textBackgroundColor = textBackgroundColorDefault;
                            textBackgroundColor = textBackgroundColorsArray[i];
                            adsResource.setTextBackgroundColor(textBackgroundColor);
                        }

                        if ((urls.length <= i) && (descArray.length > i)) {
                            adsResource.setFrom(BannerFileType.local.value());
                            adsResource.setType(BannerContentType.text.value());
                        }

                        adsResource.setLayout_gravity(layout_gravity);
                        adsResource.setTextGravity(textGravity);
                        adsResource.setUrl(urlstr);
                        adsResource.setDesc(descStr);
                        adsResource.setRadius(radiusArray);
                        list.add(adsResource);
                    }
                    banner.setData(list);
                }
                view = banner;
            } else if (element.getTag().equalsIgnoreCase("video")) {
                view = new QdVideo(context);
                //videoView.setBackgroundColor(Color.GREEN);
                /*String url = element.getAttribute("url");
                System.out.println("url="+url);
                if(!TextUtils.isEmpty(url)){
                    videoView.setVideoPath(url);
                    videoView.start();
                }*/
            } else if (element.getTag().equalsIgnoreCase("web")) {
                QDWebView qdWebView = new QDWebView(context);
                qdWebView.setTouchAble(false);
                qdWebView.getSettings().setJavaScriptEnabled(true);
                qdWebView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
                // 支持多窗口
                qdWebView.getSettings().setSupportMultipleWindows(true);
                // 缓存模式说明:
                // LOAD_CACHE_ONLY: 不使用网络，只读取本地缓存数据
                // LOAD_NO_CACHE: 不使用缓存，只从网络获取数据.
                // LOAD_DEFAULT: （默认）根据cache-control决定是否从网络上取数据。
                // LOAD_CACHE_ELSE_NETWORK，只要本地有，无论是否过期，或者no-cache，都使用缓存中的数据。
                qdWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
                // 通过设置WebView的settings来实现
                WebSettings settings = qdWebView.getSettings();
                String cacheDirPath = context.getFilesDir().getAbsolutePath() + "cache/";
                //settings.setAppCachePath(cacheDirPath);
                // 1. 设置缓存路径
                //settings.setAppCacheMaxSize(20*1024*1024);
                // 2. 设置缓存大小
                settings.setAppCacheEnabled(true);
                // 3. 开启Application Cache存储机制
// 特别注意
// 每个 Application 只调用一次 WebSettings.setAppCachePath() 和
                // WebSettings.setAppCacheMaxSize()
                // 开启 DOM storage API 功能
                qdWebView.getSettings().setDomStorageEnabled(true);

                String url = element.getAttribute("url");
                if (!TextUtils.isEmpty(url)) {
                    // qdWebView.set(url);
                    qdWebView.loadUrl(url);
                }

                // 设置可以支持缩放
                qdWebView.getSettings().setSupportZoom(false);
                // 设置出现缩放工具
                qdWebView.getSettings().setBuiltInZoomControls(false);
                view = qdWebView;
            }
            if (view != null && view.getLayoutParams() == null) {
                view.setLayoutParams(new ViewGroup.LayoutParams((int) element.getWidth(), (int) element.getHeight()));
            }
            if (element.getNodes() != null) {
                for (int i = 0; i < element.getNodes().size(); i++) {
                    generateLayout(context, (ViewGroup) view, element.getNodes().get(i));
                }
            }
        }
        if (view != null && parent != null) {
            if (parent instanceof PercentLayout) {
                PercentLayout.PosionConfig posionConfig = new PercentLayout.PosionConfig();
                String xstr = element.getAttribute("x");
                String ystr = element.getAttribute("y");
                String wstr = element.getAttribute("width");
                String hstr = element.getAttribute("height");
                if (!TextUtils.isEmpty(xstr) && !TextUtils.isEmpty(ystr) && !TextUtils.isEmpty(wstr) && !TextUtils.isEmpty(hstr)) {
                    xstr = xstr.trim();
                    ystr = ystr.trim();
                    float x = 0;
                    if (xstr.contains("%")) {
                        x = Float.valueOf(xstr.replace("%", "")) / 100;
                    } else {
                        x = Float.valueOf(xstr);
                    }
                    float y = 0;
                    if (ystr.contains("%")) {
                        y = Float.valueOf(ystr.replace("%", "")) / 100;
                    } else {
                        y = Float.valueOf(ystr);
                    }

                    float width = 0;
                    if (wstr.contains("%")) {
                        width = Float.valueOf(wstr.replace("%", "")) / 100;
                    } else {
                        width = Float.valueOf(wstr);
                    }

                    float height = 0;
                    if (hstr.contains("%")) {
                        height = Float.valueOf(hstr.replace("%", "")) / 100;
                    } else {
                        height = Float.valueOf(hstr);
                    }

                    posionConfig.setX(x);
                    posionConfig.setY(y);
                    posionConfig.setWidth(width);
                    posionConfig.setHeight(height);
                    ((PercentLayout) parent).addView(view, posionConfig);
                }
            } else {
                parent.addView(view);
            }
        }
        if (parent == null) {
            return view;
        }
        return parent;
    }
}
