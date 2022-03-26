package cn.demomaster.huan.quickdeveloplibrary.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.nio.file.Files;
import java.nio.file.Paths;

import cn.demomaster.huan.quickdeveloplibrary.util.DisplayUtil;
import cn.demomaster.huan.quickdeveloplibrary.util.QDFileUtil;
import cn.demomaster.qdlogger_library.QDLogger;

/**
 * 带回弹效果的NestedScrollView
 */
public class QDScrollTextView extends androidx.core.widget.NestedScrollView {
    public QDScrollTextView(Context context) {
        super(context);
        init();
    }

    public QDScrollTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public QDScrollTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    
    /*@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public QDScrollView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }*/

    TextView textView;
    private void init() {
        //setHorizontalFadingEdgeEnabled(false);
        //setVerticalFadingEdgeEnabled(false);
        //setOverScrollMode(OVER_SCROLL_NEVER);

        textView = new TextView(getContext());
        textView.setTextSize(12);
        //textView.setTextColor(Color.GREEN);
        addView(textView);
    }

    int pageIndex = 0;
    int pageSize = 100;
    String filePath;
    public void setText(String text) {
        textView.append(text);
    }
    public void loadTextFile(String path) {
        this.filePath = path;
        String text = loadTextFile(path, pageIndex, pageSize);
        textView.append(text);
    }

    StringBuffer stringBuffer;
    private void loadTextFile2(String path, int pageIndex) {
        try {
            stringBuffer = new StringBuffer();
            FileReader fileReader = new FileReader(path);
            BufferedReader br = new BufferedReader(fileReader);
            String textLine;
            int i = 0;
            W:
            while ((textLine = br.readLine()) != null) {
                stringBuffer.append(textLine + "\n");
                i++;
                if (i > pageSize * pageIndex) {
                    break W;
                }
            }
            br.close();
            fileReader.close();
        } catch (IOException e) {
            QDLogger.e(e);
        } finally {
            textView.append(stringBuffer);
        }
    }

    long totalLine = -1;
    private String loadTextFile(String path, int pageIndex, int pageSize) {
        if (totalLine == -1) {
            totalLine = getTotalLines(path);
        }
        StringBuffer stringBuffer = null;
        FileReader fileReader = null;
        LineNumberReader reader = null;
        try {
            fileReader = new FileReader(path);
            reader = new LineNumberReader(fileReader);
            /*long totalLine = Files.lines(Paths.get(path)).count();*/
            if (pageIndex < 0 || pageIndex > totalLine) {
                return "指定行【" + pageIndex + "】不在文件行数范围内";
            }
            int start = pageIndex * pageSize;
            int endNum = start + pageSize;
            QDLogger.println("start=" + start+",end="+endNum);
            reader.setLineNumber(start);
            String s = "";
            stringBuffer = new StringBuffer();
            while ((s = reader.readLine()) != null) {
                stringBuffer.append(s);
                if (reader.getLineNumber() == endNum) {
                    break;
                }
            }
            if (totalLine != -1) {
                QDLogger.println("totalLine = " + totalLine);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fileReader.close();
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return stringBuffer == null ? null : stringBuffer.toString();
    }

    // 文件内容的总行数
    static int getTotalLines(String fileName) {
        LineNumberReader reader = null;
        BufferedReader br = null;
        int lines = 0;
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName))); //使用缓冲区的方法将数据读入到缓冲区中
            reader = new LineNumberReader(br);
            String s = null; //定义行数
            s = reader.readLine();
            while (s != null) //确定行数
            {
                lines++;
                s = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                reader.close();
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return lines; //返回行数
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        long scrollRangeY = getScrollRange();
        //QDLogger.println("onScrollChanged " + getScrollRange() + ", top=" + t);
        if (scrollRangeY > 0 ){
            if(t > (scrollRangeY - getMeasuredHeight())) {
                pageIndex = pageIndex + 1;
                String text = loadTextFile(filePath, pageIndex, pageSize);
                textView.append(text);
            }
        }
    }

    int getScrollRange() {
        int scrollRange = 0;
        if (getChildCount() > 0) {
            View child = getChildAt(0);
            NestedScrollView.LayoutParams lp = (LayoutParams) child.getLayoutParams();
            int childSize = child.getHeight() + lp.topMargin + lp.bottomMargin;
            int parentSpace = getHeight() - getPaddingTop() - getPaddingBottom();
            scrollRange = Math.max(0, childSize - parentSpace);
        }
        return scrollRange;
    }

    Rect originalRect = new Rect();

    //记录初始位置
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        getChildlayout();
        //QDLogger.println("onLayout l="+l+",t="+t+",r="+r+",b="+b);
        /*if(getChildCount()>0) {
            View convertView = getChildAt(0);
            if (convertView != null) {
                //用rect记录 scrollview的子控件的上下左右
                originalRect.set(convertView.getLeft(), convertView.getTop(), convertView.getRight(), convertView.getBottom());
                QDLogger.println("onLayout left="+convertView.getLeft()+",top="+convertView.getTop()+",right="+convertView.getRight()+",bottom="+convertView.getBottom());
            }
        }*/
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean b = super.onInterceptTouchEvent(ev);
        //QDLogger.println("父视图 onInterceptTouchEvent2="+b);
        return b;
    }

    float startY = 0, startX = 0;
    int currentTop;
    //  事件分发
    int DEFAULT_CHILD_GRAVITY = Gravity.TOP | Gravity.START;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startY = ev.getY();
                startX = ev.getX();
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_MOVE:
                break;
        }
        boolean b = super.dispatchTouchEvent(ev);
        //QDLogger.println("父视图 dispatchTouchEvent ="+b);
        return b;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        //QDLogger.i("onTouchEvent y="+ev.getY());
        if (getChildCount() > 0) {
            View convertView = getChildAt(0);
            if (convertView != null) {
                switch (ev.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startY = ev.getY();
                        startX = ev.getX();
                        if (childAnimator != null && childAnimator.isRunning()) {
                            childAnimator.cancel();
                            currentTop = convertView.getTop() - originalRect.top;
                        } else {
                            currentTop = 0;
                        }
                        break;
                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_UP:
                        //还原位置，回弹动画， 可以自己定于需要的动画
                        /*TranslateAnimation animation = new TranslateAnimation(0, 0, convertView.getTop(), originalRect.top);
                        animation.setDuration(200);
                        animation.setRepeatCount(0);
                        animation.setInterpolator(new AccelerateDecelerateInterpolator());
                        //convertView.setAnimation(animation);
                        convertView.startAnimation(animation);*/
                        //getChildlayout();
                        getChildlayout();
                        if (childAnimator != null && childAnimator.isRunning()) {
                            childAnimator.cancel();
                        }
                        childAnimator = ValueAnimator.ofInt(convertView.getTop(), originalRect.top);
                        childAnimator.setDuration(200);
                        childAnimator.addUpdateListener(animation -> {
                                    convertView.layout(originalRect.left,
                                            (Integer) animation.getAnimatedValue(),
                                            originalRect.right,
                                            (Integer) animation.getAnimatedValue() + originalRect.height());
                                }
                        );
                        childAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
                        childAnimator.start();
                        //convertView.layout(originalRect.left, originalRect.top, originalRect.right, originalRect.bottom);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int detalY = (int) (ev.getY() - startY);
                        int detalX = (int) (ev.getX() - startX);
                        //QDLogger.i("detalY="+detalY+",startY="+startY+",ev.getY()="+ev.getY());
                        if (2 < Math.abs(detalY)) {
                            //detalY 乘以0.2 使得很难的效果
                            //getChildlayout();
                            int top = (int) (originalRect.top + detalY * 0.2) + currentTop;
                            convertView.layout(originalRect.left, top,
                                    originalRect.right, top + convertView.getMeasuredHeight());
                        }
                        break;
                }
            }
        }
        boolean b = super.onTouchEvent(ev);
        //QDLogger.i("父视图 onTouchEvent="+b);
        return b;
    }

    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        getChildlayout();
    }

    ValueAnimator childAnimator;

    private void getChildlayout() {
        if (getChildCount() > 0) {
            View convertView = getChildAt(0);
            final int width = convertView.getMeasuredWidth();
            final int height = convertView.getMeasuredHeight();
            final LayoutParams lp = (LayoutParams) convertView.getLayoutParams();
            int gravity = lp.gravity;
            if (gravity == -1) {
                gravity = DEFAULT_CHILD_GRAVITY;
            }

            int layoutDirection = 0;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
                layoutDirection = getLayoutDirection();
            }
            final int absoluteGravity = Gravity.getAbsoluteGravity(gravity, layoutDirection);
            final int verticalGravity = gravity & Gravity.VERTICAL_GRAVITY_MASK;
            int childLeft = 0;
            int childTop = 0;
            switch (absoluteGravity & Gravity.HORIZONTAL_GRAVITY_MASK) {
                case Gravity.CENTER_HORIZONTAL:
                    childLeft = getPaddingLeft() + lp.leftMargin + (getMeasuredWidth() - getPaddingLeft() - lp.leftMargin - getPaddingRight() - lp.rightMargin) / 2 - width / 2;
                    break;
                case Gravity.RIGHT:
                    // if (!forceLeftGravity) {
                    childLeft = getMeasuredWidth() - getPaddingRight() - width - lp.rightMargin;
                    break;
                // }
                case Gravity.LEFT:
                default:
                    childLeft = getPaddingLeft() + lp.leftMargin;
            }

            switch (verticalGravity) {
                case Gravity.TOP:
                    childTop = getPaddingTop() + lp.topMargin;
                    break;
                case Gravity.CENTER_VERTICAL:
                    childTop = lp.topMargin + getPaddingTop()
                            + (getMeasuredHeight() - lp.topMargin - getPaddingTop() - lp.bottomMargin - getPaddingBottom()) / 2 - height / 2;
                    break;
                case Gravity.BOTTOM:
                    childTop = getMeasuredHeight() - getPaddingBottom() - lp.bottomMargin - height;
                    break;
                default:
                    childTop = getPaddingTop() + lp.topMargin;
            }
            originalRect.set(childLeft, childTop, childLeft + width, childTop + height);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (childAnimator != null) {
            childAnimator.removeAllUpdateListeners();
            childAnimator.cancel();
        }
    }
}
