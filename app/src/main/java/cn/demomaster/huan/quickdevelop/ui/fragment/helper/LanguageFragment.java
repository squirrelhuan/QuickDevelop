package cn.demomaster.huan.quickdevelop.ui.fragment.helper;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdevelop.ui.fragment.BaseFragment;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ActivityPager;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ResType;
import cn.demomaster.huan.quickdeveloplibrary.widget.button.QDButton;

import static cn.demomaster.huan.quickdeveloplibrary.constant.EventBusConstant.EVENT_REFRESH_LANGUAGE;
import static cn.demomaster.huan.quickdeveloplibrary.util.system.QDLanguageUtil.setLanguageLocalForActivity;


/**
 * Squirrel桓
 * 2018/8/25
 */

@ActivityPager(name = "多语言", preViewClass = TextView.class, resType = ResType.Custome)
public class LanguageFragment extends BaseFragment {

    @BindView(R.id.btn_error_01)
    QDButton btn_error_01;

    @NonNull
    @Override
    public View onGenerateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_layout_language, null);
        return mView;
    }

    public void initView(View rootView) {
        ButterKnife.bind(this, rootView);
        getActionBarTool().setTitle(getString(R.string.select_language));

        final String[] cities = {getString(R.string.lan_chinese), getString(R.string.lan_en), getString(R.string.lan_ja), getString(R.string.lan_de)};
        final String[] locals = {"zh_CN", "en", "ja", "de"};
        //final String[] locals = {Locale.CHINA.getLanguage(),Locale.ENGLISH.getLanguage(),Locale.JAPAN.getLanguage(), Locale.KOREA.getLanguage()};

        btn_error_01.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setIcon(R.mipmap.ic_launcher);
            builder.setTitle(R.string.select_language);
            builder.setItems(cities, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    setLanguageLocalForActivity(mContext, locals[which]);
                    //changeAppLanguage(mContext);
                    EventBus.getDefault().post(EVENT_REFRESH_LANGUAGE);
                }
            });
            builder.show();
        });
    }

}