package cn.demomaster.huan.quickdeveloplibrary.view.floatview;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import cn.demomaster.huan.quickdeveloplibrary.R;
import cn.demomaster.huan.quickdeveloplibrary.event.listener.OnSingleClickListener;
import cn.demomaster.huan.quickdeveloplibrary.helper.toast.QdToast;
import cn.demomaster.huan.quickdeveloplibrary.service.AccessibilityHelper;
import cn.demomaster.huan.quickdeveloplibrary.service.QDAccessibilityService;
import cn.demomaster.huan.quickdeveloplibrary.util.ClipboardUtil;
import cn.demomaster.huan.quickdeveloplibrary.util.DisplayUtil;
import cn.demomaster.huan.quickdeveloplibrary.util.system.QDAppInfoUtil;
import cn.demomaster.huan.quickdeveloplibrary.view.floatview.hierarchy.HierarchyView;
import cn.demomaster.qdlogger_library.QDLogger;
import cn.demomaster.treeviewlibrary.Node;

/**
 * 页面视图结构查看
 */
public class HierarchyFlotingService extends QDFloatingService2 {
    HierarchyView hierarchyView;
    View dialogView;
    TextView button;
    String currentActivityName;
    FrameLayout linearLayout;
    ViewGroup tab_info, ll_view_info, ll_view_info2;
    RecyclerView recyclerView;
    Button btn_action_click, btn_action_longclick, btn_action_input;
    CheckBox cb_tree_view;
    List<Node> mDatas = new ArrayList<>();
    SimpleTreeRecyclerAdapter mAdapter;
    
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
        button.setOnClickListener(v -> {
            if (hierarchyView.getVisibility() == View.VISIBLE) {
                //layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
                stopCapture();
            } else {
                //layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|WindowManager.LayoutParams.FLAG_FULLSCREEN;
                startCapture();
            }
        });
        
        hierarchyView.setOnNodeInfoClickListener(new HierarchyView.OnNodeInfoClickListener() {
                                                     @Override
                                                     public void onClick(RectF rect, HierarchyView.ViewNodeInfo nodeInfo) {
                                                         ViewGroup.LayoutParams layoutParams = dialogView.getLayoutParams();
                                                         layoutParams.width = DisplayUtil.getScreenWidth(context);
                                                         layoutParams.height = DisplayUtil.getScreenHeight(context);
                                                         dialogView.setLayoutParams(layoutParams);
                                                         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                                                             showNodeInfo(nodeInfo);
                                                         }
                                                     }
            
            /*View view = QDActivityManager.getInstance().getCurrentActivity().getWindow().getDecorView().getRootView();
            Bitmap bitmap = ScreenShotUitl.getCacheBitmapFromView(view);
            int top = DisplayUtil.getStatusBarHeight(QDActivityManager.getInstance().getCurrentActivity());
            Bitmap bitmap2 = Bitmap.createBitmap(bitmap, 0, top, bitmap.getWidth(), bitmap.getHeight() - top, null, false);
            Bitmap bitmap3 = ScreenShotUitl.getCacheBitmapFromView(linearLayout);
            Bitmap bitmap4 = QDBitmapUtil.mergeBitmap(bitmap2, bitmap3);
            File dir = new File(QDFileUtil.getDiskCacheDir(context), "img/abc.jpg");
            QDFileUtil.saveBitmap(bitmap4, dir.getAbsolutePath());*/
                });

        dialogView = LayoutInflater.from(context).inflate(cn.demomaster.huan.quickdeveloplibrary.R.layout.layout_floating_hierachy, null);
        dialogView.setBackgroundColor(context.getResources().getColor(R.color.transparent_dark_55));
        dialogView.setVisibility(View.GONE);

        ll_view_info2 = dialogView.findViewById(R.id.ll_view_info2);
        recyclerView = dialogView.findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //第一个参数  RecyclerView
        //第二个参数  上下文
        //第三个参数  数据集
        //第四个参数  默认展开层级数 0为不展开
        //第五个参数  展开的图标
        //第六个参数  闭合的图标
        mAdapter = new SimpleTreeRecyclerAdapter(recyclerView, context,
                mDatas, 1);
        recyclerView.setAdapter(mAdapter);
        ll_view_info = dialogView.findViewById(R.id.ll_view_info);
        tab_info = dialogView.findViewById(R.id.tab_info);
        cb_tree_view = dialogView.findViewById(R.id.cb_tree_view);
        cb_tree_view.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ll_view_info2.setVisibility(isChecked ? View.VISIBLE : View.GONE);
            }
        });

        btn_action_click = dialogView.findViewById(R.id.btn_action_click);
        btn_action_longclick = dialogView.findViewById(R.id.btn_action_longclick);
        btn_action_input = dialogView.findViewById(R.id.btn_action_input);

        btn_action_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentNodeInfo.getAccessibilityNodeInfo() != null) {
                    currentNodeInfo.getAccessibilityNodeInfo().performAction(AccessibilityNodeInfo.ACTION_CLICK);
                }
                //QDAccessibilityService qdAccessibilityService = QDAccessibilityService.instance;
                //qdAccessibilityService.performGlobalAction(AccessibilityService.GLOBAL_ACTION_HOME);
            }
        });
        btn_action_longclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentNodeInfo.getAccessibilityNodeInfo() != null) {
                    currentNodeInfo.getAccessibilityNodeInfo().performAction(AccessibilityNodeInfo.ACTION_LONG_CLICK);
                }
            }
        });
        btn_action_input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentNodeInfo.getAccessibilityNodeInfo() != null) {
                    Bundle arguments = new Bundle();
                    arguments.putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, "文字");
                    currentNodeInfo.getAccessibilityNodeInfo().performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments);
                }
            }
        });

        TextView tv_copy = dialogView.findViewById(R.id.tv_copy);
        tv_copy.setOnClickListener(v -> {
            ClipboardUtil.setClip(getApplicationContext(), stringMap.toString());
            QdToast.show(getApplicationContext(), "copy success", 1000);
        });
        ImageView iv_colse = dialogView.findViewById(R.id.iv_colse);
        iv_colse.setOnClickListener(v -> dialogView.setVisibility(View.GONE));
        linearLayout.addView(dialogView);

        ViewGroup viewGroup = dialogView.findViewById(R.id.rl_bg);
        viewGroup.setOnClickListener(v -> dialogView.setVisibility(View.GONE));

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
        QDLogger.println("HierarchyFlotingService");
    }

    public Point getPosition() {
        layoutParams = (WindowManager.LayoutParams) linearLayout.getLayoutParams();
        return new Point(layoutParams.x, layoutParams.y);
    }

    public void setPosition(Point point) {
        layoutParams = (WindowManager.LayoutParams) linearLayout.getLayoutParams();
        layoutParams.x = point.x;
        layoutParams.y = point.y;
        linearLayout.setLayoutParams(layoutParams);
    }

    /**
     * 结束捕获
     */
    private void stopCapture() {
        button.setOnTouchListener(new QDFloatingService.FloatingOnTouchListener(linearLayout));
        Point point = new Point(button.getLeft(), button.getTop());
        hierarchyView.setVisibility(View.GONE);
        setViewPosition(button, 0, 0);
        setPosition(point);
        button.setBackgroundColor(Color.GREEN);
        button.setText("捕获");
    }

    /**
     * 开始捕获
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void startCapture() {
        if (!AccessibilityHelper.isEnable(getApplicationContext(), QDAccessibilityService.class)) {
            //跳转系统自带界面 辅助功能界面
            QDAccessibilityService.startSettintActivity(getApplicationContext());
        } else {
            Point point = getPosition();
            hierarchyView.setVisibility(View.VISIBLE);
            setViewPosition(button, Math.min(Math.max(0, point.x), DisplayUtil.getScreenWidth(getApplicationContext())), Math.min(Math.max(0, point.y), DisplayUtil.getScreenHeight(getApplicationContext())));
            button.setOnTouchListener(new QDFloatingService.FloatingOnTouchListener(button));
            button.setBackgroundColor(Color.RED);
            button.setText("结束");
            if (ServiceHelper.serverIsRunning(getApplicationContext(), QDAccessibilityService.class.getName())) {
                QDAccessibilityService qdAccessibilityService = QDAccessibilityService.instance;
                currentActivityName = qdAccessibilityService.getCurrentActivityName();
                hierarchyView.setAccessibilityNodeInfo(qdAccessibilityService.getRootInActiveWindow());
            }
        }
    }

    Map<String, String> stringMap;
    HierarchyView.ViewNodeInfo currentNodeInfo;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void showNodeInfo(HierarchyView.ViewNodeInfo nodeInfo) {
        currentNodeInfo = nodeInfo;
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
        stringMap.put("文本", "" + nodeInfo.getText());
        Rect rect = new Rect();
        nodeInfo.getBoundsInScreen(rect);
        stringMap.put("在屏幕中的位置", "" + rect);
        nodeInfo.getBoundsInParent(rect);
        stringMap.put("在父窗体中的位置", "" + rect);

        int i = 0;
        for (Map.Entry entry : stringMap.entrySet()) {
            View row = LayoutInflater.from(getApplicationContext()).inflate(R.layout.layout_floating_hierachy_item, null);
            TextView textView = row.findViewById(R.id.tv_name);//new TextView(getApplicationContext());
            TextView tv_content = row.findViewById(R.id.tv_content);//new TextView(getApplicationContext());
            Button btn_copy = row.findViewById(R.id.btn_copy);
            btn_copy.setOnClickListener(new OnSingleClickListener() {
                @Override
                public void onClickEvent(View v) {
                    String str = ((TextView)((ViewGroup)v.getParent()).findViewById(R.id.tv_content)).getText().toString();
                    ClipboardUtil.setClip(getApplicationContext(), str);
                    QdToast.show(getApplicationContext(), "copy success", 1000);
                }
            });

            textView.setText(entry.getKey() + "");
            tv_content.setText(entry.getValue() + "");
            if (i % 2 == 0) {
                row.setBackgroundColor(getResources().getColor(R.color.lightGray));
            }
            //row.addView(linearLayout,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            tableLayout.addView(row);
            i++;
        }
        if (ServiceHelper.serverIsRunning(getApplicationContext(), QDAccessibilityService.class.getName())) {
            QDAccessibilityService qdAccessibilityService = QDAccessibilityService.instance;
            setTreeData(qdAccessibilityService.getRootInActiveWindow());
        }
        //mAdapter.notifyDataSetChanged();

        mAdapter = new SimpleTreeRecyclerAdapter(recyclerView, dialogView.getContext(),
                mDatas, 1);
        recyclerView.setAdapter(mAdapter);
    }

    /**
     * 設置树状结构视图数据
     *
     * @param nodeInfo
     */
    private void setTreeData(AccessibilityNodeInfo nodeInfo) {
        mDatas.clear();
        String pid = "-1";
        String id = "1";
        //添加根节点
        mDatas.add(new Node(id, pid, "" + nodeInfo.getClassName()));
        int childCount = nodeInfo.getChildCount();
        for (int i = 0; i < childCount; i++) {
            addTreeData(i, id, nodeInfo.getChild(i));
        }
    }
    
    /**
     * 遍历节点添加
     *
     * @param index    当前节点在父节点中索引
     * @param pid      父节点id
     * @param nodeInfo 父节点实例
     */
    private void addTreeData(int index, String pid, AccessibilityNodeInfo nodeInfo) {
        if(nodeInfo!=null) {
            String id = pid + "_" + index;
            if(mDatas!=null) {
                mDatas.add(new Node(id, pid, "" + nodeInfo.getClassName()));
                int childCount = nodeInfo.getChildCount();
                for (int i = 0; i < childCount; i++) {
                    addTreeData(i, id, nodeInfo.getChild(i));
                }
            }
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
        return new PointF(0, DisplayUtil.getScreenHeight(getApplicationContext()) / 2f);
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
