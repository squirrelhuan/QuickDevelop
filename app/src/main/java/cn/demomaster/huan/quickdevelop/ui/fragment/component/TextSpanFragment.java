package cn.demomaster.huan.quickdevelop.ui.fragment.component;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdevelop.ui.fragment.BaseFragment;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ActivityPager;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ResType;
import cn.demomaster.huan.quickdeveloplibrary.helper.toast.PopToastUtil;
import cn.demomaster.huan.quickdeveloplibrary.util.SpannableUtil;
import cn.demomaster.huan.quickdeveloplibrary.widget.button.QDButton;


/**
 * Squirrel桓
 * 2018/8/25
 */
@ActivityPager(name = "文本", preViewClass = QDButton.class, resType = ResType.Custome)
public class TextSpanFragment extends BaseFragment {

    @Override
    public int getBackgroundColor() {
        return Color.WHITE;
    }

    @Override
    public View onGenerateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_layout_textspan, null);
        return mView;
    }

    SpannableUtil.SpannableFactory spannableFactory;

    @Override
    public void initView(View rootView) {
        TextView tv_content = rootView.findViewById(R.id.tv_content);
        spannableFactory = new SpannableUtil.SpannableFactory() {
            @Override
            public List<SpannableUtil.SpanModel> getSpans(String content) {
                // 设置url链接
                //URLSpan urlSpan = new URLSpan("https://www.baidu.com/");
                // 一参：url对象； 二参三参：url生效的字符起始位置； 四参：模式
                //spanModelList.add(new SpannableUtil.SpanModel(urlSpan, content,"百度一下", Spanned.SPAN_EXCLUSIVE_EXCLUSIVE));
                //spannableString.setSpan(urlSpan, 13, 17, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                List<SpannableUtil.SpanModel> spanModelList = new ArrayList<>();
                ForegroundColorSpan foregroundColorSpan1 = new ForegroundColorSpan(Color.GREEN);
                SpannableUtil.SpanModel spanModel= new SpannableUtil.SpanModel(content,"百度一下", Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                spanModel.addSpan(foregroundColorSpan1);
                spanModelList.add(spanModel);

                spanModelList.add(new SpannableUtil.SpanModel( content,7, 11, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE));
                spanModelList.add(new SpannableUtil.SpanModel( content,"点击1", Spanned.SPAN_EXCLUSIVE_EXCLUSIVE));
                spanModelList.add(new SpannableUtil.SpanModel( content,"点击2", Spanned.SPAN_EXCLUSIVE_EXCLUSIVE));
                ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.BLUE);
                spanModelList.add(new SpannableUtil.SpanModel(foregroundColorSpan, content,"点击2", Spanned.SPAN_EXCLUSIVE_EXCLUSIVE));
                return spanModelList;
            }

            @Override
            public void onClick(View widget, int start, int end, int flags, String clickText) {
                PopToastUtil.showToast(getActivity(),"点击了“"+clickText+"”");
            }
        };

        SpannableUtil.setText(tv_content,"请阅读并同意《用户协议》\n 超链接测试《百度一下》\n 点击响应测试《点击1》《点击2》",spannableFactory);
    }

}