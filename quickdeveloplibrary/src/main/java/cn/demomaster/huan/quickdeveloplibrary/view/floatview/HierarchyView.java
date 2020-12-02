package cn.demomaster.huan.quickdeveloplibrary.view.floatview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.accessibility.AccessibilityNodeInfo;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.demomaster.qdlogger_library.QDLogger;

/**
 * 视图结构显示控件
 */
public class HierarchyView extends View {
    public HierarchyView(Context context) {
        super(context);
        init();
    }

    public HierarchyView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public HierarchyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public HierarchyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (onClickListener2 != null) {
                    onClickListener2.onClick(event.getX(), event.getY());
                }
                return true;
            }
        });

        setOnClickListener2(new OnHierarchtClickListener() {
            @Override
            public void onClick(float x, float y) {
                if (viewNodeInfo != null) {
                    List<ViewNodeInfo> nodeInfoList = findNodeInfoByPosition(x, y);
                    selectedNodeInfo = getMiniNodeInfo(nodeInfoList);
                    if (selectedNodeInfo != null && onNodeInfoClickListener != null) {
                        onNodeInfoClickListener.onClick(selectedNodeInfo);
                    }
                    postInvalidate();
                }
            }
        });
    }

    /**
     * 获取最小的控件
     *
     * @param nodeInfoList
     * @return
     */
    private ViewNodeInfo getMiniNodeInfo(List<ViewNodeInfo> nodeInfoList) {
        ViewNodeInfo miniInfo = null;
        if (nodeInfoList != null) {
            for (ViewNodeInfo nodeInfo : nodeInfoList) {
                if (miniInfo == null) {
                    miniInfo = nodeInfo;
                } else {
                    Rect rect = new Rect();
                    miniInfo.getBoundsInScreen(rect);
                    Rect rect2 = new Rect();
                    nodeInfo.getBoundsInScreen(rect2);
                    if (rect2.width() * rect2.height() < rect.width() * rect.height()) {
                        miniInfo = nodeInfo;
                    }
                }
            }
        }
        return miniInfo;
    }

    ViewNodeInfo selectedNodeInfo;//标记选择的控件

    /**
     * 根据位置获取控件，存在叠加情况所以有可能得到多个结果
     *
     * @param x
     * @param y
     * @return
     */
    private List<ViewNodeInfo> findNodeInfoByPosition(float x, float y) {
        if (viewNodeInfo != null) {
            int[] location;
            location = new int[2];
            getLocationOnScreen(location);//获取在整个屏幕内的绝对坐标
            return findNodeInfoByPosition(viewNodeInfo, x + location[0], y + location[1]);
        }
        return null;
    }

    private List<ViewNodeInfo> findNodeInfoByPosition(ViewNodeInfo accessibilityNodeInfo, float x, float y) {
        if (accessibilityNodeInfo != null) {
            Rect rect = new Rect();
            accessibilityNodeInfo.getBoundsInScreen(rect);
            if (rect.left <= x && rect.top <= y && rect.right >= x && rect.bottom >= y) {
                List<ViewNodeInfo> nodeInfoList = new ArrayList<>();
                if (accessibilityNodeInfo.getChildCount() > 0) {
                    for (int i = 0; i < accessibilityNodeInfo.getChildCount(); i++) {
                        ViewNodeInfo nodeInfo1 = accessibilityNodeInfo.getChild(i);
                        List<ViewNodeInfo> nodeInfoList2 = findNodeInfoByPosition(nodeInfo1, x, y);
                        if (nodeInfoList2 != null) {
                            nodeInfoList.addAll(nodeInfoList2);
                        }
                    }
                    if (nodeInfoList != null && nodeInfoList.size() > 0) {
                        return nodeInfoList;
                    }
                }
                nodeInfoList.add(accessibilityNodeInfo);
                return nodeInfoList;
            }
            return null;
        }
        return null;
    }

    OnHierarchtClickListener onClickListener2;

    public void setOnClickListener2(OnHierarchtClickListener onClickListener) {
        this.onClickListener2 = onClickListener;
    }

    public static interface OnHierarchtClickListener {
        void onClick(float x, float y);
    }

    public static interface OnNodeInfoClickListener {
        void onClick(ViewNodeInfo nodeInfo);
    }

    OnNodeInfoClickListener onNodeInfoClickListener;

    public void setOnNodeInfoClickListener(OnNodeInfoClickListener onNodeInfoClickListener) {
        this.onNodeInfoClickListener = onNodeInfoClickListener;
    }

    Paint paint;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint = new Paint();
        paint.setStrokeWidth(2);
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);
        drawNode(canvas, viewNodeInfo);

        if (selectedNodeInfo != null) {
            paint.setColor(Color.GREEN);
            int[] location = new int[2];
            getLocationOnScreen(location);//获取在整个屏幕内的绝对坐标AccessibilityNodeInfo nodeInfo =selectedNodeInfo;
            Rect rect = new Rect();
            selectedNodeInfo.getBoundsInScreen(rect);//获取类名
            Rect rect1 = new Rect(rect.left - location[0], rect.top - location[1], rect.right - location[0], rect.bottom - location[1]);
            canvas.drawRect(rect1, paint);
        }
    }

    private void drawNode(Canvas canvas, ViewNodeInfo viewNodeInfo) {
        if (viewNodeInfo != null) {
            int[] location = new int[2];
            getLocationOnScreen(location);//获取在整个屏幕内的绝对坐标
            Rect rect = new Rect();
            viewNodeInfo.getBoundsInScreen(rect);//获取类名
            QDLogger.e("drawNode="+rect);
            Rect rect1 = new Rect(rect.left - location[0], rect.top - location[1], rect.right - location[0], rect.bottom - location[1]);

            canvas.drawRect(rect1, paint);
            if (viewNodeInfo.getChildCount() > 0) {
                for (int i = 0; i < viewNodeInfo.getChildCount(); i++) {
                    drawNode(canvas, viewNodeInfo.getChild(i));
                }
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }
    ViewNodeInfo viewNodeInfo;

    /**
     * 设置选中的控件
     * @param accessibilityNodeInfo
     */
    public void setAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        generateViewNode(null,accessibilityNodeInfo);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void generateViewNode(ViewNodeInfo parent, AccessibilityNodeInfo accessibilityNodeInfo) {
        if(accessibilityNodeInfo!=null){
            if(parent==null) {
                parent = new ViewNodeInfo();
                parent.setClassName(accessibilityNodeInfo.getClassName().toString());
                parent.setPackageName(accessibilityNodeInfo.getPackageName().toString());
                parent.setViewIdResourceName(accessibilityNodeInfo.getViewIdResourceName());
                parent.setClickable(accessibilityNodeInfo.isClickable());
                parent.setEnabled(accessibilityNodeInfo.isEnabled());
                parent.setContentDescription(accessibilityNodeInfo.getContentDescription()==null?null:accessibilityNodeInfo.getContentDescription().toString());
                Rect rect =new Rect();
                accessibilityNodeInfo.getBoundsInScreen(rect);
                parent.setBoundsInScreen(rect);
                Rect rect2 =new Rect();
                accessibilityNodeInfo.getBoundsInParent(rect2);
                parent.setBoundsInParent(rect2);
                viewNodeInfo = parent;
            }

            for(int i = 0;i<accessibilityNodeInfo.getChildCount();i++){
                AccessibilityNodeInfo child = accessibilityNodeInfo.getChild(i);
                QDLogger.e("i="+i+",child="+child);
                if(child!=null) {
                    ViewNodeInfo viewNodeInfo1 = new ViewNodeInfo();
                    viewNodeInfo1.setClassName(TextUtils.isEmpty(child.getClassName()) ? null : child.getClassName().toString());
                    viewNodeInfo1.setPackageName(TextUtils.isEmpty(child.getPackageName()) ? null : child.getPackageName().toString());
                    viewNodeInfo1.setViewIdResourceName(child.getViewIdResourceName());
                    viewNodeInfo1.setClickable(child.isClickable());
                    viewNodeInfo1.setEnabled(child.isEnabled());
                    viewNodeInfo1.setContentDescription(child.getContentDescription()==null?null:child.getContentDescription().toString());
                    Rect rect = new Rect();
                    child.getBoundsInScreen(rect);
                    viewNodeInfo1.setBoundsInScreen(rect);
                    Rect rect2 = new Rect();
                    child.getBoundsInParent(rect2);
                    viewNodeInfo1.setBoundsInParent(rect2);
                    viewNodeInfo1.setParent(parent);
                    parent.addChild(viewNodeInfo1);
                    if (child.getChildCount() > 0) {
                        generateViewNode(viewNodeInfo1, child);
                    }
                }
            }
        }
    }

    public static class ViewNodeInfo implements Serializable {
        String contentDescription;
        String mClassName;
        String mPackageName;
        String mViewIdResourceName;
        boolean enabled;
        boolean clickable;
        boolean visibleToUser;
        Rect mBoundsInScreen;
        Rect mBoundsInParent;
        List<ViewNodeInfo> childList = new ArrayList<>();
        public String getClassName() {
            return mClassName;
        }

        public void setClassName(String mClassName) {
            this.mClassName = mClassName;
        }

        public void addChild(ViewNodeInfo viewNodeInfo1) {
            childList.add(viewNodeInfo1);
        }

        public int getChildCount(){
            return childList.size();
        }

        public ViewNodeInfo getChild(int i) {
            return childList.get(i);
        }
        public void getBoundsInScreen(Rect rect) {
            rect.set(mBoundsInScreen.left, mBoundsInScreen.top,
                    mBoundsInScreen.right, mBoundsInScreen.bottom);
        }

        public void setBoundsInScreen(Rect boundsInScreen) {
            QDLogger.e("boundsInScreen="+boundsInScreen);
            this.mBoundsInScreen = boundsInScreen;
            QDLogger.e("mBoundsInScreen="+mBoundsInScreen);
        }

        public void setPackageName(String mPackageName) {
            this.mPackageName = mPackageName;
        }

        public String getPackageName() {
            return mPackageName;
        }

        public String getViewIdResourceName() {
            return mViewIdResourceName;
        }

        public void setViewIdResourceName(String mViewIdResourceName) {
            this.mViewIdResourceName = mViewIdResourceName;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public boolean isEnabled() {
            return enabled;
        }

        public boolean isClickable() {
            return clickable;
        }

        public void setClickable(boolean clickable) {
            this.clickable = clickable;
        }

        public boolean isVisibleToUser() {
            return visibleToUser;
        }

        public void setVisibleToUser(boolean visibleToUser) {
            this.visibleToUser = visibleToUser;
        }

        public void getBoundsInParent(Rect rect) {
            rect.set(mBoundsInParent.left, mBoundsInParent.top,
                    mBoundsInParent.right, mBoundsInParent.bottom);
        }

        public void setBoundsInParent(Rect boundsInParent) {
            this.mBoundsInParent = boundsInParent;
        }
        ViewNodeInfo parent;
        public void setParent(ViewNodeInfo viewNodeInfo) {
            this.parent = viewNodeInfo;
        }

        public ViewNodeInfo getParent() {
            return parent;
        }

        public String getContentDescription() {
            return contentDescription;
        }

        public void setContentDescription(String contentDescription) {
            this.contentDescription = contentDescription;
        }
    }
}
