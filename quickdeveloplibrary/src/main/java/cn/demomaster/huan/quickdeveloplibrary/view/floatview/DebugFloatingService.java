package cn.demomaster.huan.quickdeveloplibrary.view.floatview;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.graphics.PointF;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.demomaster.huan.quickdeveloplibrary.R;
import cn.demomaster.huan.quickdeveloplibrary.helper.PermissionManager;
import cn.demomaster.huan.quickdeveloplibrary.helper.QDActivityManager;
import cn.demomaster.huan.quickdeveloplibrary.helper.QdThreadHelper;
import cn.demomaster.huan.quickdeveloplibrary.util.DisplayUtil;
import cn.demomaster.huan.quickdeveloplibrary.util.system.QDAppInfoUtil;
import cn.demomaster.huan.quickdeveloplibrary.view.floator.DebugFloating2;
import cn.demomaster.huan.quickdeveloplibrary.view.floator.FloatHelper;
import cn.demomaster.huan.quickdeveloplibrary.widget.dialog.QDSheetDialog;
import cn.demomaster.qdlogger_library.QDLogBean;
import cn.demomaster.qdlogger_library.QDLogInterceptor;
import cn.demomaster.qdlogger_library.QDLogger;

import static cn.demomaster.huan.quickdeveloplibrary.view.floator.DebugFloating2.logTagNames;
import static cn.demomaster.huan.quickdeveloplibrary.view.floator.DebugFloating2.logTagfilters;
import static cn.demomaster.huan.quickdeveloplibrary.view.floator.DebugFloating2.tagFilter;

/**
 * Created
 */
public class DebugFloatingService extends QDFloatingService {

    static Context mcontext;
    static int screenWidth;
    static int screenHeight;

    @Override
    public View setContentView(final Context context) {

        View view = getView(context, onTouchListener);
        return view;
    }

    static RichTextView tv_log;
    static ScrollView scrollView;
    static ImageView iv_drag;
    static ImageView iv_logo;
    static TextView tv_title;
    static TextView tv_log_tag;

    public View getView(Context context, View.OnTouchListener onTouchListener) {
        mcontext = context;
        screenWidth = DisplayUtil.getScreenWidth(mcontext);
        screenHeight = DisplayUtil.getScreenHeight(mcontext);
        View view = LayoutInflater.from(context).inflate(R.layout.layout_debug_console, null, false);
        contentView = view;
        iv_drag = view.findViewById(R.id.iv_drag);
        iv_drag.setOnTouchListener(onTouchListener);
        iv_logo = view.findViewById(R.id.iv_logo);
        iv_logo.setImageDrawable(QDAppInfoUtil.getAppIconDrawable(mcontext));
        scrollView = view.findViewById(R.id.scrollView);

        tv_title = view.findViewById(R.id.tv_title);
        tv_title.setText("" + QDAppInfoUtil.getVersionName(context));
        TextView tv_close = view.findViewById(R.id.tv_close);
        tv_log = view.findViewById(R.id.tv_log);
        tv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dissmissWindow(context, DebugFloatingService.class);
            }
        });
        tv_log_tag = view.findViewById(R.id.tv_log_tag);
        tv_log_tag.setText(tagFilter.name());
        tv_log_tag.setTextColor(QDLogger.getColor(tagFilter.value()));
        tv_log_tag.setOnClickListener(onClickTagListenernew);
        initLog();
        return view;
    }

    View.OnClickListener onClickTagListenernew = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            showTagFilter((TextView) v);
        }
    };

    private void showTagFilter(TextView textView) {
        new QDSheetDialog.MenuBuilder(QDActivityManager.getInstance().getCurrentActivity()).setData(logTagNames).setOnDialogActionListener(new QDSheetDialog.OnDialogActionListener() {
            @Override
            public void onItemClick(QDSheetDialog dialog, int position, List<String> data) {
                dialog.dismiss();
                textView.setText(data.get(position));
                tagFilter = logTagfilters[position];
                textView.setTextColor(QDLogger.getColor(tagFilter.value()));
            }
        }).create().show();
    }

    static int strMaxLen = 20000;
    static List<QDLogBean> logList = new ArrayList<>();
    static String logStr = "";

    private static void initLog() {
        tv_log.setText(logStr);
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(ScrollView.FOCUS_DOWN);//滚动到底部
            }
        });
        QDLogger.setInterceptor(new QDLogInterceptor() {
            @Override
            public void onLog(QDLogBean msg) {
                QdThreadHelper.runOnUiThread(new Runnable() {
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
                        ForegroundColorSpan tagColorSpan = new ForegroundColorSpan(QDLogger.getColor(msg.getType().value()));
                        builder.setSpan(tagColorSpan, (lineNum + "").length() + 1, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);//包含两端start和end所在的端点
                        // Spannable.SPAN_EXCLUSIVE_INCLUSIVE);//不包含端start，但包含end所在的端点
                        tv_log.append(builder);
                        logStr = tv_log.getText().toString();
                        scrollView.fullScroll(ScrollView.FOCUS_DOWN);//滚动到底部
                    }
                });
            }
        });
    }

    int drag_X;
    int drag_Y;
    public View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    drag_X = (int) event.getRawX();
                    drag_Y = (int) event.getRawY();
                    //isclick = false;//当按下的时候设置isclick为false
                    //startTime = System.currentTimeMillis();
                    //System.out.println("执行顺序down");
                    break;
                case MotionEvent.ACTION_MOVE:
                    //isclick = true;//当按钮被移动的时候设置isclick为true,就拦截掉了点击事件

                    int nowX = (int) event.getRawX();
                    int nowY = (int) event.getRawY();
                    int movedX = nowX - drag_X;
                    int movedY = nowY - drag_Y;
                    drag_X = nowX;
                    drag_Y = nowY;

                    int height = DebugFloatingService.mHeight + movedY;
                    height = Math.min(screenHeight, height);
                    setHeight(height);
                    int width = DebugFloatingService.mWidth + movedX;
                    width = Math.min(screenWidth, width);
                    setWidth(width);

                    break;
                case MotionEvent.ACTION_UP:
                    /*endTime = System.currentTimeMillis();
                    //当从点击到弹起小于半秒的时候,则判断为点击,如果超过则不响应点击事件
                    if ((endTime - startTime) > 0.15 * 1000L) {
                        isclick = true;
                    } else {
                        isclick = false;
                    }*/
                    break;
                default:
                    break;
            }
            return true;
        }
    };

    @Override
    public PointF getOriginPoint() {
        return new PointF(100, 100);
    }

    @Override
    public void init() {
        setWidth(800);
        setHeight(500);
    }

    static DebugFloating2 debugFloating2;

    public static void showConsole(Activity context) {
        if (QDAppInfoUtil.isApkInDebug(context)) {
            if (PermissionManager.getPermissionStatus(context.getApplicationContext(), Manifest.permission.SYSTEM_ALERT_WINDOW)) {
                DebugFloatingService.showWindow(context.getApplicationContext(), DebugFloatingService.class);
            } else {
                QDLogger.e(FloatHelper.Tag, "showConsole context= " + context);
                if (debugFloating2 == null) {
                    debugFloating2 = new DebugFloating2();
                }
                debugFloating2.show(context);
            }
        }
    }
}
