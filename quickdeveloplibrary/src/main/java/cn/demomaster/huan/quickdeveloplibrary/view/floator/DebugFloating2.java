package cn.demomaster.huan.quickdeveloplibrary.view.floator;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.demomaster.huan.quickdeveloplibrary.R;
import cn.demomaster.huan.quickdeveloplibrary.helper.QDActivityManager;
import cn.demomaster.huan.quickdeveloplibrary.helper.QdThreadHelper;
import cn.demomaster.huan.quickdeveloplibrary.util.DisplayUtil;
import cn.demomaster.huan.quickdeveloplibrary.util.system.QDAppInfoUtil;
import cn.demomaster.huan.quickdeveloplibrary.view.floatview.RichTextView;
import cn.demomaster.huan.quickdeveloplibrary.widget.dialog.QDSheetDialog;
import cn.demomaster.qdlogger_library.QDLogInterceptor;
import cn.demomaster.qdlogger_library.QDLogBean;
import cn.demomaster.qdlogger_library.QDLoggerType;
import cn.demomaster.qdlogger_library.QDLogger;

/**
 * 调试日志输出
 */
public class DebugFloating2 implements FloatView {

    static FloatHelper floatHelper;

    public void show(Activity mactivity) {
        if (floatHelper == null) {
            floatHelper = new FloatHelper();
        }
        floatHelper.addFloatView(mactivity, this);
    }

    static int screenWidth;
    static int screenHeight;

    public static QDLoggerType tagFilter = QDLoggerType.ALL;
    public static QDLoggerType[] logTagfilters = {QDLoggerType.ALL,
            QDLoggerType.VERBOSE,
            QDLoggerType.INFO,
            QDLoggerType.DEBUG,
            QDLoggerType.ERROR};
    public static String[] logTagNames = {QDLoggerType.ALL.name(),
            QDLoggerType.VERBOSE.name(),
            QDLoggerType.INFO.name(),
            QDLoggerType.DEBUG.name(),
            QDLoggerType.ERROR.name()};

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

    public View.OnTouchListener onTouchListener2 = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    drag_X = (int) event.getRawX();
                    drag_Y = (int) event.getRawY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    int nowX = (int) event.getRawX();
                    int nowY = (int) event.getRawY();
                    int movedX = nowX - drag_X;
                    int movedY = nowY - drag_Y;
                    drag_X = nowX;
                    drag_Y = nowY;

                    floatHelper.onWindowSizeChanged(movedX, movedY);
                    break;
                case MotionEvent.ACTION_UP:
                    break;
                default:
                    break;
            }
            return true;
        }
    };

    int strMaxLen = 5000;
    List<QDLogBean> logList = new ArrayList<>();

    private void initLog(View view) {
        RichTextView tv_log = view.findViewById(R.id.tv_log);
        ScrollView scrollView = view.findViewById(R.id.scrollView);
        String logStr = getLogText(view.getContext());
        if (!TextUtils.isEmpty(logStr)) {
            SpannableStringBuilder spannableStringBuilder = SpannableStringBuilder.valueOf(logStr);
            tv_log.setText(spannableStringBuilder);
            scrollView.post(new Runnable() {
                @Override
                public void run() {
                    if (scrollView != null)
                        scrollView.fullScroll(ScrollView.FOCUS_DOWN);//滚动到底部
                }
            });
        }
        QDLogger.setInterceptor(logInterceptor);
    }

    QDLogInterceptor logInterceptor = new QDLogInterceptor() {
        @Override
        public void onLog(QDLogBean msg) {
            View view = floatHelper.getCurrentContentView();
            if (view != null) {
                RichTextView tv_log = view.findViewById(R.id.tv_log);
                ScrollView scrollView = view.findViewById(R.id.scrollView);
                if (tv_log.getText().length() > strMaxLen) {
                    logList.clear();
                }
                logList.add(msg);
                int lineNum = (tv_log.getLineCount()+1);
                String str =lineNum + " " + msg.getTag() + " " + msg.getMessage() + " \n";

                SpannableStringBuilder builder = new SpannableStringBuilder(str);
                //ForegroundColorSpan 为文字前景色，BackgroundColorSpan为文字背景色
                ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(tv_log.getResources().getColor(R.color.orange));
                //AbsoluteSizeSpan smallSpan = new AbsoluteSizeSpan(12, true);
                StyleSpan styleSpan = new StyleSpan(Typeface.BOLD);
                builder.setSpan(foregroundColorSpan, 0, (lineNum + "").length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);//包含两端start和end所在的端点
                ForegroundColorSpan tagColorSpan = new ForegroundColorSpan(QDLogger.getColor(msg.getType().value()));
                builder.setSpan(tagColorSpan, (lineNum + "").length() + 1, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);//包含两端start和end所在的端点

                //logStr = builder.toString()+tv_log.getText();
                QdThreadHelper.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (msg.getType().value() == tagFilter.value() || tagFilter == QDLoggerType.ALL && tv_log != null) {
                            if (tv_log.getText().length() > strMaxLen) {
                                tv_log.setText("");
                            }
                            tv_log.append(builder);
                            if (scrollView != null) {
                                scrollView.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        scrollView.fullScroll(ScrollView.FOCUS_DOWN);//滚动到底部
                                    }
                                });
                            }
                        }
                    }
                });
            }
        }
    };

    static int drag_X;
    static int drag_Y;
    View.OnClickListener onClickTagListenernew = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            showTagFilter((TextView) v);
        }
    };

    @Override
    public View onCreateView(Activity context) {
        RichTextView tv_log;
        ImageView iv_drag;
        TextView tv_title;
        TextView tv_close;
        ImageView iv_logo;
        TextView tv_log_tag;
        screenWidth = DisplayUtil.getScreenWidth(context);
        screenHeight = DisplayUtil.getScreenHeight(context);
        ViewGroup contentView = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.layout_debug_console, null, false);
        iv_drag = contentView.findViewById(R.id.iv_drag);
        iv_drag.setOnTouchListener(onTouchListener2);
        iv_logo = contentView.findViewById(R.id.iv_logo);
        iv_logo.setImageDrawable(QDAppInfoUtil.getAppIconDrawable(context));

        tv_title = contentView.findViewById(R.id.tv_title);
        tv_title.setText("" + QDAppInfoUtil.getVersionName(context));
        tv_close = contentView.findViewById(R.id.tv_close);
        tv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                floatHelper.hideFloatView();
            }
        });

        tv_log_tag = contentView.findViewById(R.id.tv_log_tag);
        tv_log_tag.setText(tagFilter.name());
        tv_log_tag.setTextColor(QDLogger.getColor(tagFilter.value()));
        tv_log_tag.setOnClickListener(onClickTagListenernew);

        initLog(contentView);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        contentView.setLayoutParams(layoutParams);
        return contentView;
    }

    @Override
    public void onResume(Activity activity) {
        ViewGroup windowView = floatHelper.getCurrentContentView();
        if (windowView == null || windowView.getChildCount() == 0) {
            return;
        }

        RichTextView tv_log;
        ScrollView scrollView;
        ImageView iv_drag;
        TextView tv_title;
        TextView tv_close;
        ImageView iv_logo;
        TextView tv_log_tag;
        iv_drag = windowView.findViewById(R.id.iv_drag);
        iv_logo = windowView.findViewById(R.id.iv_logo);
        scrollView = windowView.findViewById(R.id.scrollView);

        tv_title = windowView.findViewById(R.id.tv_title);
        tv_close = windowView.findViewById(R.id.tv_close);
        tv_log = windowView.findViewById(R.id.tv_log);
        tv_log_tag = windowView.findViewById(R.id.tv_log_tag);
        if (tv_log_tag != null) {
            tv_log_tag.setText(tagFilter.name());
            tv_log_tag.setTextColor(QDLogger.getColor(tagFilter.value()));
        }

        String logStr = getLogText(activity);
        if (!TextUtils.isEmpty(logStr) && tv_log != null) {
            SpannableStringBuilder spannableStringBuilder = SpannableStringBuilder.valueOf(logStr);
            tv_log.setText(spannableStringBuilder);
        }
        if (scrollView != null)
            scrollView.post(new Runnable() {
                @Override
                public void run() {
                    scrollView.fullScroll(ScrollView.FOCUS_DOWN);//滚动到底部
                }
            });
    }

    private String getLogText(Context context) {
        StringBuffer stringBuffer = new StringBuffer();
        int i=0;
        for (QDLogBean QDLogBean : logList) {
            if(tagFilter== QDLogBean.getType()||tagFilter== QDLoggerType.ALL) {
                int lineNum =(i + 1);
                String str = lineNum + " " + QDLogBean.getTag() + " " + QDLogBean.getMessage() + " \n";
                SpannableStringBuilder builder = new SpannableStringBuilder(str);
                //ForegroundColorSpan 为文字前景色，BackgroundColorSpan为文字背景色
                ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(context.getResources().getColor(R.color.orange));
                //AbsoluteSizeSpan smallSpan = new AbsoluteSizeSpan(12, true);
                StyleSpan styleSpan = new StyleSpan(Typeface.BOLD);
                builder.setSpan(foregroundColorSpan, 0, (lineNum + "").length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);//包含两端start和end所在的端点
                ForegroundColorSpan tagColorSpan = new ForegroundColorSpan(QDLogger.getColor(QDLogBean.getType().value()));
                builder.setSpan(tagColorSpan, (lineNum + "").length() + 1, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);//包含两端start和end所在的端点
                stringBuffer.append(builder.toString());
                i++;
            }
        }
        return stringBuffer.toString();
    }

    /**
     * 此处保存当前数据
     *
     * @param activity
     */
    @Override
    public void onPause(Activity activity) {
        ViewGroup windowView = floatHelper.getFloatView(activity);
        if (windowView == null || windowView.getChildCount() == 0) {
            return;
        }

        RichTextView tv_log;
        tv_log = windowView.findViewById(R.id.tv_log);
       /* if (!TextUtils.isEmpty(logStr) && tv_log != null) {
            logStr = tv_log.getText().toString();
        }*/
    }

    static int windowWidth = 600;
    static int windowHeight = 400;

    @Override
    public Point getSize() {
        return new Point(700, 400);
    }

    @Override
    public Point leftTopPoint() {
        return new Point(100, 100);
    }

}
