package cn.demomaster.huan.quickdeveloplibrary.view.floatview;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.PointF;
import android.graphics.Typeface;
import android.os.Build;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.demomaster.huan.quickdeveloplibrary.R;
import cn.demomaster.huan.quickdeveloplibrary.helper.QdThreadHelper;
import cn.demomaster.huan.quickdeveloplibrary.util.system.QDAppInfoUtil;
import cn.demomaster.huan.quickdeveloplibrary.view.floator.DebugFloating2;
import cn.demomaster.huan.quickdeveloplibrary.widget.dialog.QDSheetDialog;
import cn.demomaster.qdlogger_library.model.LogBean;
import cn.demomaster.qdlogger_library.interceptor.QDLogInterceptor;
import cn.demomaster.qdlogger_library.QDLogger;
import cn.demomaster.qdrouter_library.manager.QDActivityManager;
import cn.demomaster.quickpermission_library.PermissionHelper;

import static cn.demomaster.huan.quickdeveloplibrary.view.floator.DebugFloating2.getTagColor;
import static cn.demomaster.huan.quickdeveloplibrary.view.floator.DebugFloating2.logTagNames;
import static cn.demomaster.huan.quickdeveloplibrary.view.floator.DebugFloating2.logTagfilters;
import static cn.demomaster.huan.quickdeveloplibrary.view.floator.DebugFloating2.tagFilter;
import static cn.demomaster.qdlogger_library.QDLogger.isDebug;

/**
 * Created
 */
public class DebugFloatingService extends QDFloatingService2 {

    static Context mcontext;
    static RichTextView tv_log;
    static ScrollView scrollView;
    static ImageView iv_drag;
    static ImageView iv_logo;
    static TextView tv_title;
    static TextView tv_log_tag;
    View view;

    @Override
    public void onCreateView(Context context, WindowManager windowManager) {
        mcontext = context;
        view = LayoutInflater.from(context).inflate(R.layout.layout_debug_console, null, false);
        iv_drag = view.findViewById(R.id.iv_drag);
        onTouchListener = new QDFloatingService.FloatingOnTouchListener(iv_drag);
        iv_drag.setOnTouchListener(onTouchListener);
        iv_logo = view.findViewById(R.id.iv_logo);
        iv_logo.setImageDrawable(QDAppInfoUtil.getAppIconDrawable(mcontext));
        scrollView = view.findViewById(R.id.scrollView);

        tv_title = view.findViewById(R.id.tv_title);
        tv_title.setText("" + QDAppInfoUtil.getVersionName(context));
        TextView tv_close = view.findViewById(R.id.tv_close);
        tv_log = view.findViewById(R.id.tv_log);
        tv_close.setOnClickListener(v -> dissmissWindow());
        tv_log_tag = view.findViewById(R.id.tv_log_tag);
        tv_log_tag.setText(tagFilter.name());
        tv_log_tag.setTextColor(getTagColor(tagFilter.value()));
        tv_log_tag.setOnClickListener(onClickTagListenernew);
        initLog();

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
        layoutParams.width = 800;
        layoutParams.height = 500;
        this.windowManager = windowManager;
        windowManager.addView(view, layoutParams);
        onTouchListener1 = new QDFloatingService.FloatingOnTouchListener(view);
        view.setOnTouchListener(onTouchListener1);
    }

    View.OnClickListener onClickTagListenernew = v -> showTagFilter((TextView) v);

    private void showTagFilter(TextView textView) {
        new QDSheetDialog.MenuBuilder(QDActivityManager.getInstance().getCurrentActivity()).setData(logTagNames).setOnDialogActionListener((dialog, position, data) -> {
            dialog.dismiss();
            textView.setText(data.get(position));
            tagFilter = logTagfilters[position];
            textView.setTextColor(getTagColor(tagFilter.value()));
        }).create().show();
    }

    static int strMaxLen = 20000;
    static List<LogBean> logList = new ArrayList<>();
    static String logStr = "";
    static QDLogInterceptor qdLogInterceptor;

    private static void initLog() {
        tv_log.setText(logStr);
        scrollView.post(() -> {
            scrollView.fullScroll(ScrollView.FOCUS_DOWN);//滚动到底部
        });

        qdLogInterceptor = msg -> QdThreadHelper.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (tv_log.getText().length() > strMaxLen) {
                    logList.clear();
                    tv_log.setText("");
                }
                logList.add(msg);
                int lineNum = tv_log.getLineCount();
                String str = lineNum + " " + msg.getTag() + " " + msg.getMessage() + " \n";

                SpannableStringBuilder builder = new SpannableStringBuilder(str);
                //ForegroundColorSpan 为文字前景色，BackgroundColorSpan为文字背景色
                ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(tv_log.getResources().getColor(R.color.orange));
                //AbsoluteSizeSpan smallSpan = new AbsoluteSizeSpan(12, true);
                StyleSpan styleSpan = new StyleSpan(Typeface.BOLD);
                builder.setSpan(foregroundColorSpan, 0, (lineNum + "").length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);//包含两端start和end所在的端点
                ForegroundColorSpan tagColorSpan = new ForegroundColorSpan(Color.WHITE);
                if (msg.getLevel() != null) {
                    tagColorSpan = new ForegroundColorSpan(getTagColor(msg.getLevel().value()));
                }
                builder.setSpan(tagColorSpan, (lineNum + "").length() + 1, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);//包含两端start和end所在的端点
                //Spannable.SPAN_EXCLUSIVE_INCLUSIVE);//不包含端start，但包含end所在的端点
                tv_log.append(builder);
                logStr = tv_log.getText().toString();
                scrollView.fullScroll(ScrollView.FOCUS_DOWN);//滚动到底部
            }
        });
        QDLogger.setInterceptor(qdLogInterceptor);
    }

    public QDFloatingService.FloatingOnTouchListener onTouchListener1;
    public QDFloatingService.FloatingOnTouchListener onTouchListener;
    /*

    int drag_X;
    int drag_Y;
    new QDFloatingService.FloatingOnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    drag_X = (int) event.getRawX();
                    drag_Y = (int) event.getRawY();
                    //isclick = false;//当按下的时候设置isclick为false
                    //startTime = System.currentTimeMillis();
                    System.out.println("执行顺序down");
                    break;
                case MotionEvent.ACTION_MOVE:
                    //isclick = true;//当按钮被移动的时候设置isclick为true,就拦截掉了点击事件
                    int nowX = (int) event.getRawX();
                    int nowY = (int) event.getRawY();
                    int movedX = nowX - drag_X;
                    int movedY = nowY - drag_Y;
                    drag_X = nowX;
                    drag_Y = nowY;
                    WindowManager.LayoutParams layoutParams = (WindowManager.LayoutParams) view.getLayoutParams();
                    int height = layoutParams.height + movedY;
                    height = Math.min(DisplayUtil.getScreenHeight(view.getContext()), height);
                    int width = layoutParams.width + movedX;
                    width = Math.min(DisplayUtil.getScreenWidth(view.getContext()), width);
                    //QDLogger.i("movedX="+movedX+",movedY="+movedY+",width="+ width+",height="+ height);
                    updateViewLayout(view, width, height);
                    break;
                case MotionEvent.ACTION_UP:
                    *//*endTime = System.currentTimeMillis();
                    //当从点击到弹起小于半秒的时候,则判断为点击,如果超过则不响应点击事件
                    if ((endTime - startTime) > 0.15 * 1000L) {
                        isclick = true;
                    } else {
                        isclick = false;
                    }*//*
                    break;
                default:
                    break;
            }
            return true;
        }
    };*/

    @Override
    public PointF getOriginPoint() {
        return new PointF(100, 100);
    }

    static DebugFloating2 debugFloating2;

    public static void showConsole(Activity context) {
        if (isDebug(context)) {
            if (PermissionHelper.getPermissionStatus(context.getApplicationContext(), Manifest.permission.SYSTEM_ALERT_WINDOW)) {
                //DebugFloatingService.showWindow(context, DebugFloatingService.class);
                ServiceHelper.startService(context, DebugFloatingService.class);
            } else {
                //QDLogger.e(FloatHelper.Tag, "showConsole context= " + context);
                if (debugFloating2 == null) {
                    debugFloating2 = new DebugFloating2();
                }
                debugFloating2.show(context);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (tv_log_tag != null) {
            tv_log_tag.setOnClickListener(null);
        }
        onClickTagListenernew = null;
        if (iv_drag != null) {
            iv_drag.setOnTouchListener(null);
        }
        if (onTouchListener != null) {
            onTouchListener.onRelease(null);
        }
        if (onTouchListener1 != null) {
            onTouchListener1.onRelease(null);
        }

        QDLogger.setInterceptor(null);
        qdLogInterceptor = null;

        if (view != null) {
            view.setOnTouchListener(null);
            removeView(view);
        }
    }
}
