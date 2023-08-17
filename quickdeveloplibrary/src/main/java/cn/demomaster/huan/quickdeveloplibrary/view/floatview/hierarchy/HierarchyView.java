package cn.demomaster.huan.quickdeveloplibrary.view.floatview.hierarchy;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.demomaster.huan.quickdeveloplibrary.R;
import cn.demomaster.huan.quickdeveloplibrary.util.DisplayUtil;
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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public HierarchyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }
    OnTouchListener onTouchListener = new OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (onClickListener2 != null) {
                onClickListener2.onClick(event.getX(), event.getY());
            }
            return true;
        }
    };

    PointF pointF ;
    boolean isAccessibility;
    private void init() {
        setOnTouchListener(onTouchListener);
        setOnClickListener2((x, y) -> {
            if (viewNodeInfo != null) {
                if(isAccessibility) {
                    List<ViewNodeInfo> nodeInfoList = findNodeInfoByPosition(x, y);
                    selectedNodeInfo = getMiniNodeInfo(nodeInfoList);
                }else {
                    List<ViewNodeInfo> nodeInfoList = findNodeInfoByPosition(x, y);
                    selectedNodeInfo = getMiniNodeInfo(nodeInfoList);
                }
                if (selectedNodeInfo != null && onNodeInfoClickListener != null) {
                    onNodeInfoClickListener.onClick(new RectF(x,y,getMeasuredWidth()-x,getMeasuredHeight()-y),selectedNodeInfo);
                }
                /*if (selectedNodeInfo != null && onNodeViewClickListener != null) {
                    onNodeViewClickListener.onClick(selectedNodeInfo);
                }*/

                pointF = new PointF(x,y);
                postInvalidate();
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
            int[] location = new int[2];
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
                    if (nodeInfoList.size() > 0) {
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

    public interface OnHierarchtClickListener {
        void onClick(float x, float y);
    }

    public interface OnNodeInfoClickListener {
        void onClick(RectF rect,ViewNodeInfo nodeInfo);
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
            paint.setStrokeWidth(3);
            paint.setColor(Color.GREEN);
            int[] location = new int[2];
            getLocationOnScreen(location);//获取在整个屏幕内的绝对坐标AccessibilityNodeInfo nodeInfo =selectedNodeInfo;
            Rect rect = new Rect();
            selectedNodeInfo.getBoundsInScreen(rect);//获取类名
            Rect rect1 = new Rect(rect.left - location[0], rect.top - location[1], rect.right - location[0], rect.bottom - location[1]);
            canvas.drawRect(rect1, paint);
            String tip = rect1.width()+"x"+rect1.height();

            paint.setXfermode(null);
            paint.setStrokeWidth(1);
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(Color.GREEN);
            paint.setTextSize(getContext().getResources().getDimensionPixelSize(R.dimen.quickdev_small_text_size));
            // 文字宽
            float textWidth = paint.measureText(tip);
            // 文字baseline在y轴方向的位置
            float baseLineY = Math.abs(paint.ascent() + paint.descent()) / 2f;
            float left = rect1.left+(rect1.width()-textWidth)/2;
            float top = rect1.bottom -baseLineY/2f;
            paint.setColor(Color.BLACK);
            paint.setStyle(Paint.Style.FILL);

            Paint.FontMetrics fm = paint.getFontMetrics();
            float height1 = fm.descent - fm.ascent;
            float lineHeight = fm.bottom - fm.top + fm.leading;
            float top1 = rect1.bottom - lineHeight;

            RectF rectF = new RectF(left,top1, left+textWidth,rect1.bottom);
            canvas.drawRect(rectF, paint);
            paint.setColor(Color.WHITE);
            canvas.drawText(tip, left, top, paint);
        }

        if(pointF!=null){
            int w = DisplayUtil.dip2px(getContext(),15);
            paint.setColor(Color.YELLOW);
            int w2 = DisplayUtil.dip2px(getContext(),2);
            paint.setStrokeWidth(w2);
            Shader mShader = new LinearGradient(pointF.x-w,pointF.y-w,pointF.x+w,pointF.y+w,new int[] {0xff000000,0xffffffff},null, Shader.TileMode.CLAMP);
//新建一个线性渐变，前两个参数是渐变开始的点坐标，第三四个参数是渐变结束的点的坐标。连接这2个点就拉出一条渐变线了，玩过PS的都懂。然后那个数组是渐变的颜色。下一个参数是渐变颜色的分布，如果为空，每个颜色就是均匀分布的。最后是模式，这里设置的是循环渐变
            paint.setShader(mShader);
            canvas.drawLine(pointF.x-w,pointF.y,pointF.x+w,pointF.y,paint);
            canvas.drawLine(pointF.x,pointF.y-w,pointF.x,pointF.y+w,paint);
        }
    }

    private void drawNode(Canvas canvas, ViewNodeInfo viewNodeInfo) {
        if (viewNodeInfo != null) {
            int[] location = new int[2];
            getLocationOnScreen(location);//获取在整个屏幕内的绝对坐标
            Rect rect = new Rect();
            viewNodeInfo.getBoundsInScreen(rect);//获取类名
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
     *
     * @param accessibilityNodeInfo
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void setAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        isAccessibility = true;
        generateViewNode(null, accessibilityNodeInfo);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void generateViewNode(ViewNodeInfo parent, AccessibilityNodeInfo accessibilityNodeInfo) {
        if (accessibilityNodeInfo != null) {
            if (parent == null) {
                parent = new ViewNodeInfo();
                parent.setClassName(accessibilityNodeInfo.getClassName().toString());
                parent.setPackageName(accessibilityNodeInfo.getPackageName().toString());
                parent.setViewIdResourceName(accessibilityNodeInfo.getViewIdResourceName());
                parent.setClickable(accessibilityNodeInfo.isClickable());
                parent.setEnabled(accessibilityNodeInfo.isEnabled());
                parent.setText(accessibilityNodeInfo.getText() + "");
                parent.setContentDescription(accessibilityNodeInfo.getContentDescription() == null ? null : accessibilityNodeInfo.getContentDescription().toString());
                Rect rect = new Rect();
                accessibilityNodeInfo.getBoundsInScreen(rect);
                parent.setBoundsInScreen(rect);
                Rect rect2 = new Rect();
                accessibilityNodeInfo.getBoundsInParent(rect2);
                parent.setBoundsInParent(rect2);
                viewNodeInfo = parent;
            }

            for (int i = 0; i < accessibilityNodeInfo.getChildCount(); i++) {
                AccessibilityNodeInfo child = accessibilityNodeInfo.getChild(i);
                //QDLogger.e("i=" + i + ",child=" + child);
                if (child != null) {
                    ViewNodeInfo viewNodeInfo1 = new ViewNodeInfo();
                    viewNodeInfo1.setAccessibilityNodeInfo(child);
                    viewNodeInfo1.setClassName(TextUtils.isEmpty(child.getClassName()) ? null : child.getClassName().toString());
                    viewNodeInfo1.setPackageName(TextUtils.isEmpty(child.getPackageName()) ? null : child.getPackageName().toString());
                    viewNodeInfo1.setViewIdResourceName(child.getViewIdResourceName());
                    viewNodeInfo1.setClickable(child.isClickable());
                    viewNodeInfo1.setEnabled(child.isEnabled());
                    viewNodeInfo1.setText(child.getText() + "");
                    viewNodeInfo1.setContentDescription(child.getContentDescription() == null ? null : child.getContentDescription().toString());
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

    View targetView;
    public void setNodeView(View view) {
        isAccessibility = false;
        targetView = view;
        viewNodeInfo = generateView(null, view);
    }

    private ViewNodeInfo generateView(ViewNodeInfo parent, View view) {
        if (view != null) {
            if (parent == null) {
                parent = new ViewNodeInfo();
                parent.setClassName(view.getClass().getName().toString());
                parent.setPackageName(view.getContext().getPackageName());
                parent.setViewIdResourceName(view.getId() + "");
                parent.setView(view);
                parent.setClickable(view.isClickable());
                parent.setEnabled(view.isEnabled());
                String str = "";
                if (view instanceof TextView) {
                    TextView textView = (TextView) view;
                    str = TextUtils.isEmpty(textView.getText()) ? "" : textView.getText().toString();
                }
                parent.setText(str + "");
                //parent.setContentDescription(accessibilityNodeInfo.getContentDescription() == null ? null : accessibilityNodeInfo.getContentDescription().toString());
                int[] location = new int[2];
                view.getLocationOnScreen(location);//获取在整个屏幕内的绝对坐标AccessibilityNodeInfo nodeInfo =selectedNodeInfo;
                Rect rect = new Rect(location[0], location[1], location[0]+view.getMeasuredWidth(), location[1]+view.getMeasuredHeight());

                //view.getBoundsInScreen(rect);
                parent.setBoundsInScreen(rect);
                Rect rect2 = new Rect();
                //view.getBoundsInParent(rect2);
                parent.setBoundsInParent(rect2);
                viewNodeInfo = parent;
            }
            if (view instanceof ViewGroup) {
                ViewGroup viewGroup = (ViewGroup) view;
                for (int i = 0; i < viewGroup.getChildCount(); i++) {
                    View child = viewGroup.getChildAt(i);
                    //QDLogger.e("i=" + i + ",child=" + child);
                    if (child != null&&child.getVisibility()==VISIBLE) {
                        ViewNodeInfo viewNodeInfo1 = new ViewNodeInfo();
                        viewNodeInfo1.setView(child);
                        viewNodeInfo1.setClassName(child.getClass().getName().toString());
                        viewNodeInfo1.setPackageName(child.getContext().getPackageName());
                        //viewNodeInfo1.setViewIdResourceName(child.getViewIdResourceName());
                        viewNodeInfo1.setClickable(child.isClickable());
                        viewNodeInfo1.setEnabled(child.isEnabled());
                        String str = "";
                        if (view instanceof TextView) {
                            TextView textView = (TextView) view;
                            str = TextUtils.isEmpty(textView.getText()) ? "" : textView.getText().toString();
                        }
                        parent.setText(str + "");
                        viewNodeInfo1.setContentDescription(child.getContentDescription() == null ? null : child.getContentDescription().toString());
                        int[] location = new int[2];
                        child.getLocationOnScreen(location);//获取在整个屏幕内的绝对坐标AccessibilityNodeInfo nodeInfo =selectedNodeInfo;
                        Rect rect = new Rect(location[0], location[1], location[0]+child.getMeasuredWidth(), location[1]+child.getMeasuredHeight());
                        //Rect rect = new Rect();
                        //child.getBoundsInScreen(rect);
                        viewNodeInfo1.setBoundsInScreen(rect);
                        int[] location2 = new int[2];
                        child.getLocationOnScreen(location2);//获取在整个屏幕内的绝对坐标AccessibilityNodeInfo nodeInfo =selectedNodeInfo;
                        Rect rect2 = new Rect(location2[0], location2[1], location2[0]+child.getMeasuredWidth(), location2[1]+child.getMeasuredHeight());
                        //child.getBoundsInParent(rect2);
                        viewNodeInfo1.setBoundsInParent(rect2);
                        viewNodeInfo1.setParent(parent);
                        parent.addChild(viewNodeInfo1);
                        if (child instanceof ViewGroup) {
                            generateView(viewNodeInfo1, child);
                        }
                    }
                }
            }
        }
        return parent;
    }

    public static class ViewNodeInfo implements Serializable {
        String contentDescription;
        String mClassName;
        String text;
        String mPackageName;
        String mViewIdResourceName;
        boolean enabled;
        boolean clickable;
        boolean visibleToUser;
        Rect mBoundsInScreen;
        Rect mBoundsInParent;
        List<ViewNodeInfo> childList = new ArrayList<>();
        AccessibilityNodeInfo accessibilityNodeInfo;
        private View view;

        public AccessibilityNodeInfo getAccessibilityNodeInfo() {
            return accessibilityNodeInfo;
        }

        public void setAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
            this.accessibilityNodeInfo = accessibilityNodeInfo;
        }

        public String getClassName() {
            return mClassName;
        }

        public void setClassName(String mClassName) {
            this.mClassName = mClassName;
        }

        public void addChild(ViewNodeInfo viewNodeInfo1) {
            childList.add(viewNodeInfo1);
        }

        public int getChildCount() {
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
            //QDLogger.e("boundsInScreen="+boundsInScreen);
            this.mBoundsInScreen = boundsInScreen;
            //QDLogger.e("mBoundsInScreen="+mBoundsInScreen);
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

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public void setView(View view) {
            this.view = view;
        }

        public View getView() {
            return view;
        }
    }

}
