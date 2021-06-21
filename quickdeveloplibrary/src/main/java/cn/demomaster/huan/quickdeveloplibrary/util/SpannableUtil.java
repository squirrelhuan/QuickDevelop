package cn.demomaster.huan.quickdeveloplibrary.util;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.CharacterStyle;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import cn.demomaster.qdlogger_library.QDLogger;

public class SpannableUtil {
    public static void addTextView(TextView tv_content) {
    }

    public static void setText(TextView tv_content, String content, SpannableFactory spannableFactory) {
        //创建可扩展字符串并输入内容
        SpannableString spannableString = new SpannableString(content);

        //获取样式列表
        List<SpanModel> spanList = spannableFactory.getSpans(content);
        //去除重复样式
        Map<String, SpanModel> spanModelMap_T = new LinkedHashMap<>();
        for (SpanModel spanModel : spanList) {
            String key = spanModel.getStart() + "_" + spanModel.getEnd();
            if (spanModelMap_T.containsKey(key)) {
                SpanModel model1 = spanModelMap_T.put(key, spanModel);
                model1.addAllSpanMap(spanModel.getSpanMap());
                spanModelMap_T.put(key, model1);
            } else {
                spanModelMap_T.put(key, spanModel);
            }
        }

        List<SpanModel> spanList2 = new ArrayList<>();
        for (Map.Entry entry : spanModelMap_T.entrySet()) {
            spanList2.add((SpanModel) entry.getValue());
        }
        spanList = spanList2;


        if (spanList != null) {
            for (SpanModel model : spanList) {
                // 一参：url对象； 二参三参：url生效的字符起始位置； 四参：模式
                List<CharacterStyle> styleList = model.getSpans();
                if (styleList != null) {
                    //要先处理点击事件，再添加其他样式，否则点击样式会覆盖后面追加的文本样式
                    ClickableSpan clickableSpan = model.getClickableSpan();
                    if (clickableSpan == null) {//是否包含点击事件监听
                        clickableSpan = new ClickableSpan() {
                            @Override
                            public void onClick(@NonNull View widget) {
                                String clickText = model.getKeyText(content);
                                spannableFactory.onClick(widget, model.getStart(), model.getEnd(), model.getFlags(), clickText);
                            }
                        };
                    }
                    spannableString.setSpan(clickableSpan, model.getStart(), model.getEnd(), model.getFlags());
                    for (CharacterStyle span : styleList) {
                        if (!(span instanceof ClickableSpan)) {
                            //QDLogger.e("添加样式："+model.getKeyText()+",start="+model.getStart()+",end="+model.getEnd());
                            spannableString.setSpan(span, model.getStart(), model.getEnd(), model.getFlags());
                        }
                    }
                }
            }
            // 设置textView可以点击链接进行跳转（不设置的话点击无反应）
            tv_content.setMovementMethod(LinkMovementMethod.getInstance());
        }

        //把可扩展字符串设置到textView
        tv_content.setText(spannableString);

        //1.识别特殊文本
        //2.获取特殊文本的样式和响应
    }

    public static interface SpannableFactory {
        List<SpanModel> getSpans(String content);
        // void onClick();

        void onClick(View widget, int start, int end, int flags, String clickText);
    }

    /**
     * 文本样式数据源
     */
    public static class SpanModel {
        int start;
        int end;
        int flags;
        String content;//原文
        String keyText;//关键词
        //ClickableSpan
        Map<String, CharacterStyle> spanMap = new LinkedHashMap<>();

        public Map<String, CharacterStyle> getSpanMap() {
            return spanMap;
        }

        public void addAllSpanMap(Map<String, CharacterStyle> characterStyleMap) {
            spanMap.putAll(characterStyleMap);
        }

        //一参：url对象； 二参三参：url生效的字符起始位置； 四参：模式
        public SpanModel(CharacterStyle span, int start, int end, int flags) {
            this.start = start;
            this.end = end;
            this.flags = flags;
            addSpan(span);
        }

        /**
         * 添加样式
         *
         * @param span
         */
        public void addSpan(CharacterStyle span) {
            String tag = String.format("%s_%s_%s_", start + "", end + "", flags + "") + span.getClass().getName();
            //QDLogger.e("addSpan tag="+tag);
            spanMap.put(tag, span);
        }

        public SpanModel(CharacterStyle span, String content, String keyText, int flags) {
            init(content, keyText, flags);
            addSpan(span);
        }

        //点击事件可以不用传span
        public SpanModel(String content, int start, int end, int flags) {
            this.content = content;
            this.start = start;
            this.end = end;
            this.flags = flags;
        }

        //关键词（适用于一段文本中仅出现一次的情况）
        public SpanModel(String content, String keyText, int flags) {
            init(content, keyText, flags);
        }

        public void init(String content, String keyText, int flags) {
            this.content = content;
            this.keyText = keyText;
            if (!TextUtils.isEmpty(content) && !TextUtils.isEmpty(keyText) && content.contains(keyText)) {
                int start = content.indexOf(keyText);
                int end = start + keyText.length();
                switch (flags) {
                    case Spanned.SPAN_EXCLUSIVE_EXCLUSIVE://(前后都不包括)
                        break;
                    case Spanned.SPAN_INCLUSIVE_EXCLUSIVE://(前面包括，后面不包括)
                        start = Math.max(0, start - 1);
                        break;
                    case Spanned.SPAN_EXCLUSIVE_INCLUSIVE://(前面不包括，后面包括)
                        end = Math.min(content.length(), end + 1);
                        break;
                    case Spanned.SPAN_INCLUSIVE_INCLUSIVE://(前后都包括)
                        start = Math.max(0, start - 1);
                        end = Math.min(content.length(), end + 1);
                        break;
                }
                this.start = start;
                this.end = end;
            }
        }

        public int getStart() {
            return start;
        }

        public void setStart(int start) {
            this.start = start;
        }

        public int getEnd() {
            return end;
        }

        public void setEnd(int end) {
            this.end = end;
        }

        public int getFlags() {
            return flags;
        }

        public void setFlags(int flags) {
            this.flags = flags;
        }

        public List<CharacterStyle> getSpans() {
            List<CharacterStyle> characterStyleList = new ArrayList<>();
            for (Map.Entry entry : spanMap.entrySet()) {
                characterStyleList.add((CharacterStyle) entry.getValue());
            }
            return characterStyleList;
        }

        public void setSpan(CharacterStyle span) {
            // this.spans = span;
        }

        public String getKeyText() {
            if (keyText != null) {
                return keyText;
            } else {
                keyText = getKeyText(content);
            }
            return keyText;
        }

        public void setKeyText(String keyText) {
            this.keyText = keyText;
        }

        public String getKeyText(String content) {
            int start = this.start;
            int end = this.end;
            switch (flags) {
                case Spanned.SPAN_EXCLUSIVE_EXCLUSIVE://(前后都不包括)
                    break;
                case Spanned.SPAN_INCLUSIVE_EXCLUSIVE://(前面包括，后面不包括)
                    start = Math.max(0, start - 1);
                    break;
                case Spanned.SPAN_EXCLUSIVE_INCLUSIVE://(前面不包括，后面包括)
                    end = Math.min(content.length(), end + 1);
                    break;
                case Spanned.SPAN_INCLUSIVE_INCLUSIVE://(前后都包括)
                    start = Math.max(0, start - 1);
                    end = Math.min(content.length(), end + 1);
                    break;
            }

            return content.substring(start, end);
        }

        /**
         * 获取点击事件
         *
         * @return
         */
        public ClickableSpan getClickableSpan() {
            for (Map.Entry entry : spanMap.entrySet()) {
                if (entry.getValue() instanceof ClickableSpan) {
                    return (ClickableSpan) entry.getValue();
                }
            }
            return null;
        }
    }
}
