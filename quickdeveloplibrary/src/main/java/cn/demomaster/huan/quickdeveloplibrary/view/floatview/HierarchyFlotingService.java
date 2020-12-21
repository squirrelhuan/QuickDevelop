package cn.demomaster.huan.quickdeveloplibrary.view.floatview;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import java.util.LinkedHashMap;
import java.util.Map;

import cn.demomaster.huan.quickdeveloplibrary.R;
import cn.demomaster.huan.quickdeveloplibrary.helper.toast.QdToast;
import cn.demomaster.huan.quickdeveloplibrary.service.AccessibilityHelper;
import cn.demomaster.huan.quickdeveloplibrary.service.QDAccessibilityService;
import cn.demomaster.huan.quickdeveloplibrary.util.ClipboardUtil;
import cn.demomaster.huan.quickdeveloplibrary.util.DisplayUtil;
import cn.demomaster.huan.quickdeveloplibrary.util.system.QDAppInfoUtil;
import cn.demomaster.qdlogger_library.QDLogger;

/**
 * 页面视图结构查看
 */
public class HierarchyFlotingService extends QDFloatingService2 {
    HierarchyView hierarchyView;
    View dialogView;
    TextView button, tv_title;
    String currentActivityName;
    FrameLayout linearLayout;

    @Override
    public void onCreateView(Context context, WindowManager windowManager) {
        linearLayout = new FrameLayout(context.getApplicationContext());
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        hierarchyView = new HierarchyView(context);
        //hierarchyView.setBackgroundColor(context.getResources().getColor(R.color.transparent_dark_33));

        int width = DisplayUtil.getScreenWidth(getApplicationContext());
        int height = DisplayUtil.getScreenHeight(getApplicationContext());
        linearLayout.addView(hierarchyView, new LinearLayout.LayoutParams(width, height));
        hierarchyView.setVisibility(View.GONE);
        button = new TextView(context);
        button.setBackgroundColor(Color.GREEN);
        button.setTextColor(Color.WHITE);
        button.setText("捕获");
        button.setPadding(20, 20, 20, 20);
        button.setOnTouchListener(new QDFloatingService.FloatingOnTouchListener(linearLayout));
        linearLayout.addView(button, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hierarchyView.getVisibility() == View.VISIBLE) {
                    //layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
                    showDetailInfo();
                } else {
                    //layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|WindowManager.LayoutParams.FLAG_FULLSCREEN;
                    hideDetailInfo();
                }
            }
        });

        hierarchyView.setOnNodeInfoClickListener(new HierarchyView.OnNodeInfoClickListener() {
            @Override
            public void onClick(HierarchyView.ViewNodeInfo nodeInfo) {
                ViewGroup.LayoutParams layoutParams = dialogView.getLayoutParams();
                layoutParams.width = DisplayUtil.getScreenWidth(context);
                layoutParams.height = DisplayUtil.getScreenHeight(context);
                dialogView.setLayoutParams(layoutParams);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    showNodeInfo(nodeInfo);
                }
            }
        });

        dialogView = LayoutInflater.from(context).inflate(cn.demomaster.huan.quickdeveloplibrary.R.layout.layout_floating_hierachy, null);
        dialogView.setBackgroundColor(context.getResources().getColor(R.color.transparent_dark_55));
        dialogView.setVisibility(View.GONE);
        tv_title = dialogView.findViewById(R.id.tv_title);
        TextView tv_copy = dialogView.findViewById(R.id.tv_copy);
        tv_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardUtil.setClip(getApplicationContext(), stringMap.toString());
                QdToast.show(getApplicationContext(), "copy success", 1000);
            }
        });
        ImageView iv_colse = dialogView.findViewById(R.id.iv_colse);
        iv_colse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogView.setVisibility(View.GONE);
            }
        });
        linearLayout.addView(dialogView);

        ViewGroup viewGroup = dialogView.findViewById(R.id.rl_bg);
        viewGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogView.setVisibility(View.GONE);
            }
        });

        layoutParams = new WindowManager.LayoutParams();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;//TYPE_APPLICATION_OVERLAY;
        } else {
            //layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
            layoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        }
        layoutParams.format = PixelFormat.RGBA_8888;
        layoutParams.gravity = Gravity.LEFT | Gravity.TOP;
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        if (!getTouchAble()) {
            layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        }

        PointF pointF = getOriginPoint();
        if (pointF != null) {
            layoutParams.x = (int) pointF.x;
            layoutParams.y = (int) pointF.y;
        }
        layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        this.windowManager = windowManager;
        windowManager.addView(linearLayout, layoutParams);
        linearLayout.setOnTouchListener(new QDFloatingService.FloatingOnTouchListener(linearLayout));
        QDLogger.i("HierarchyFlotingService");
    }

    public Point getPosition() {
        layoutParams = (WindowManager.LayoutParams) linearLayout.getLayoutParams();
        return new Point(layoutParams.x, layoutParams.y);
    }

    public void setPosition(Point point) {
        layoutParams = (WindowManager.LayoutParams) linearLayout.getLayoutParams();
        layoutParams.x = (int) point.x;
        layoutParams.y = (int) point.y;
        linearLayout.setLayoutParams(layoutParams);
    }

    private void showDetailInfo() {
        button.setOnTouchListener(new QDFloatingService.FloatingOnTouchListener(linearLayout));
        Point point = new Point(button.getLeft(), button.getTop());
        hierarchyView.setVisibility(View.GONE);
        setViewPosition(button, 0, 0);
        setPosition(point);
        button.setBackgroundColor(Color.GREEN);
        button.setText("捕获");
    }

    private void hideDetailInfo() {
        Point point = getPosition();
        hierarchyView.setVisibility(View.VISIBLE);
        setViewPosition(button, Math.min(Math.max(0, point.x), DisplayUtil.getScreenWidth(getApplicationContext())), Math.min(Math.max(0, point.y), DisplayUtil.getScreenHeight(getApplicationContext())));
        button.setOnTouchListener(new QDFloatingService.FloatingOnTouchListener(button));
        button.setBackgroundColor(Color.RED);
        button.setText("结束");

        QDAccessibilityService qdAccessibilityService = AccessibilityHelper.getService();
        if (qdAccessibilityService != null) {
            currentActivityName = qdAccessibilityService.getCurrentActivityName();
            hierarchyView.setAccessibilityNodeInfo(qdAccessibilityService.getRootInActiveWindow());
        }
    }

    Map<String, String> stringMap;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void showNodeInfo(HierarchyView.ViewNodeInfo nodeInfo) {
        dialogView.setVisibility(View.VISIBLE);
        TableLayout tableLayout = dialogView.findViewById(R.id.tab_info);
        tableLayout.removeAllViews();
        stringMap = new LinkedHashMap<>();
        PackageInfo packageInfo = QDAppInfoUtil.getPackageInfoByPackageName(getApplicationContext(), nodeInfo.getPackageName());
        String appName = null;
        if (packageInfo != null) {
            appName = getApplicationContext().getPackageManager().getApplicationLabel(packageInfo.applicationInfo).toString();// 得到应用名
        }
        stringMap.put("应用名称", "" + appName);
        stringMap.put("页面名称", "" + currentActivityName);
        stringMap.put("控件名称", "" + nodeInfo.getClassName());
        stringMap.put("包名", "" + nodeInfo.getPackageName());
        stringMap.put("唯一标识", "" + nodeInfo.getViewIdResourceName());
        stringMap.put("包含控件个数", "" + nodeInfo.getChildCount());
        stringMap.put("是否可用", "" + nodeInfo.isEnabled());
        stringMap.put("是否可点击", "" + nodeInfo.isClickable());
        stringMap.put("是否显示", "" + nodeInfo.isVisibleToUser());
        Rect rect = new Rect();
        nodeInfo.getBoundsInScreen(rect);
        stringMap.put("在屏幕中的位置", "" + rect);
        nodeInfo.getBoundsInParent(rect);
        stringMap.put("在父窗体中的位置", "" + rect);

        int i = 0;
        for (Map.Entry entry : stringMap.entrySet()) {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,
                    1.0f
            );
            LinearLayout row = new LinearLayout(getApplicationContext());
            row.setOrientation(LinearLayout.HORIZONTAL);
            TextView textView = new TextView(getApplicationContext());
            textView.setPadding(20, 20, 20, 20);
            textView.setText(entry.getKey() + "");
            textView.setTextColor(Color.BLACK);
            row.addView(textView);
            TextView textView2 = new TextView(getApplicationContext());
            textView2.setPadding(20, 20, 20, 20);
            textView2.setText(entry.getValue() + "");
            textView2.setTextColor(Color.BLACK);
            textView2.setGravity(Gravity.LEFT);
            //LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) textView2.getLayoutParams();

            //layoutParams.weight = 1f;
            //textView2.setLayoutParams(layoutParams);
            row.addView(textView2, layoutParams);
            if (i % 2 == 0) {
                row.setBackgroundColor(getResources().getColor(R.color.lightGray));
            }
            //row.addView(linearLayout,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            tableLayout.addView(row);
            i++;
        }
    }

    private void setViewPosition(View targetView, int left, int top) {
        if (targetView.getLayoutParams() instanceof WindowManager.LayoutParams) {
            WindowManager.LayoutParams layoutParams;
            layoutParams = (WindowManager.LayoutParams) targetView.getLayoutParams();
            layoutParams.x = left;
            layoutParams.y = top;
            windowManager.updateViewLayout(targetView, layoutParams);
        } else if (targetView.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams layoutParams;
            layoutParams = (ViewGroup.MarginLayoutParams) targetView.getLayoutParams();
            layoutParams.leftMargin = left;
            layoutParams.topMargin = top;
            targetView.setLayoutParams(layoutParams);
        }
    }

    @Override
    public PointF getOriginPoint() {
        return new PointF(0, DisplayUtil.getScreenHeight(getApplicationContext()) / 2);
    }

    @Override
    public boolean getTouchAble() {
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        removeView(linearLayout);
    }
}
