package cn.demomaster.huan.quickdevelop.ui.fragment.component;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.URLSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ActivityPager;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ResType;
import cn.demomaster.huan.quickdeveloplibrary.helper.toast.QdToast;
import cn.demomaster.huan.quickdeveloplibrary.ui.fragment.WebViewFragment;
import cn.demomaster.huan.quickdeveloplibrary.util.SpannableUtil;
import cn.demomaster.huan.quickdeveloplibrary.widget.button.QDButton;
import cn.demomaster.huan.quickdeveloplibrary.widget.dialog.QDDialog;
import cn.demomaster.qdrouter_library.base.fragment.QuickFragment;


/**
 * Squirrel桓
 * 2018/8/25
 */
@ActivityPager(name = "文本", preViewClass = QDButton.class, resType = ResType.Custome)
public class TextSpanFragment extends QuickFragment {

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
                SpannableUtil.SpanModel spanModel = new SpannableUtil.SpanModel(content, "百度一下", Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                spanModel.addSpan(foregroundColorSpan1);
                String url_baidu = "https://www.baidu.com/";
                URLSpan urlSpan_baidu = new URLSpan(url_baidu) {
                    //这里重写点击事件，默认会跳转到系统浏览器并打开url
                    @Override
                    public void onClick(View widget) {
                        //super.onClick(widget);
                        showScanDialog(spanModel.getKeyText(), url_baidu);
                    }
                };
                spanModel.addSpan(urlSpan_baidu);
                spanModelList.add(spanModel);

                SpannableUtil.SpanModel spanModel123 = new SpannableUtil.SpanModel(content, "微信支付H5开发指引", Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                String url3 = "https://pay.weixin.qq.com/wiki/doc/api/H5.php?chapter=15_4";
                URLSpan urlSpan2 = new URLSpan(url3) {
                    //这里重写点击事件，默认会跳转到系统浏览器并打开url
                    @Override
                    public void onClick(View widget) {
                        //super.onClick(widget);
                        showScanDialog(spanModel123.getKeyText(), url3);
                    }
                };
                spanModel123.addSpan(urlSpan2);
                spanModel123.addSpan(foregroundColorSpan1);
                spanModelList.add(spanModel123);

                SpannableUtil.SpanModel spanModel12 = new SpannableUtil.SpanModel(content, "微信支付H5测试", Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                String url = "https://wx.tenpay.com/cgi-bin/mmpayweb-bin/checkmweb?prepay_id=wx20161110163838f231619da20804912345&package=1037687096&redirect_url=https%3A%2F%2Fwww.wechatpay.com.cn";
                URLSpan urlSpan = new URLSpan(url) {
                    //这里重写点击事件，默认会跳转到系统浏览器并打开url
                    @Override
                    public void onClick(View widget) {
                        //super.onClick(widget);
                        showScanDialog(spanModel12.getKeyText(), url);
                    }
                };
                spanModel12.addSpan(urlSpan);
                spanModel12.addSpan(foregroundColorSpan1);
                spanModelList.add(spanModel12);

                spanModelList.add(new SpannableUtil.SpanModel(content, 7, 11, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE));
                spanModelList.add(new SpannableUtil.SpanModel(content, "点击1", Spanned.SPAN_EXCLUSIVE_EXCLUSIVE));
                spanModelList.add(new SpannableUtil.SpanModel(content, "点击2", Spanned.SPAN_EXCLUSIVE_EXCLUSIVE));
                ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.BLUE);
                spanModelList.add(new SpannableUtil.SpanModel(foregroundColorSpan, content, "点击2", Spanned.SPAN_EXCLUSIVE_EXCLUSIVE));
                return spanModelList;
            }

            @Override
            public void onClick(View widget, int start, int end, int flags, String clickText) {
                QdToast.showToast(getActivity(), "点击了“" + clickText + "”");
                showScanDialog(clickText, null);
            }
        };

        SpannableUtil.setText(tv_content, "请阅读并同意《用户协议》\n 超链接测试《百度一下》\n 点击响应测试《点击1》《点击2》 \n 微信支付H5开发指引 \n 微信支付H5测试", spannableFactory);
    }

    private void showScanDialog(String clickText, String url) {
        QDDialog.Builder builder = new QDDialog.Builder(mContext)
                .setTitle("点击了“" + clickText + "”")
                .setText_color_body(Color.BLUE)
                .setText_size_foot(14)
                .addAction("取消", (dialog, view, tag) -> dialog.dismiss());
        if (!TextUtils.isEmpty(url)) {
            builder.setMessage("链接" + url);
            builder.addAction("APP内打开", (dialog, view, tag) -> {
                dialog.dismiss();
                WebViewFragment webViewFragment = new WebViewFragment();
                Bundle bundle = new Bundle();
                bundle.putString("URL", url);
                webViewFragment.setArguments(bundle);
                Intent intent = new Intent();
                intent.putExtras(bundle);
                startFragment(webViewFragment, R.id.container1, intent);
            });
            builder.addAction("浏览器打开", (dialog, view, tag) -> {
                dialog.dismiss();
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            });
        }
        QDDialog qdDialog = builder.create();
        //.setMinHeight_body((int) getResources().getDimension(R.dimen.dp_100))
        //.setGravity_body(Gravity.CENTER).setText_size_body((int) getResources().getDimension(R.dimen.sp_10))
        //.setWidth((int) getResources().getDimension(R.dimen.dp_120))
        //.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT)
        //.setText_size_foot((int) getResources().getDimension(R.dimen.sp_6))
        //.setPadding_foot((int) getResources().getDimension(R.dimen.sp_6))
        //.setActionButtonPadding((int) getResources().getDimension(R.dimen.sp_6))
        //.setText_color_foot(Color.GREEN)
        //.setLineColor(Color.RED)
        //.setBackgroundRadius(backgroundRadio)
        qdDialog.show();
    }
}