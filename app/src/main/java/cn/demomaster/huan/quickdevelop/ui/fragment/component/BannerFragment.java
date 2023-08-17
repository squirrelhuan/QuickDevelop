package cn.demomaster.huan.quickdevelop.ui.fragment.component;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager2.widget.ViewPager2;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdevelop.ui.activity.BaseActivity;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ActivityPager;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ResType;
import cn.demomaster.huan.quickdeveloplibrary.util.xml.NodeElement;
import cn.demomaster.huan.quickdeveloplibrary.view.banner.Banner;
import cn.demomaster.huan.quickdeveloplibrary.view.banner.NormalPageTransformer;
import cn.demomaster.huan.quickdeveloplibrary.view.banner.ZoomOutPageTransformer;
import cn.demomaster.huan.quickdeveloplibrary.view.banner.ZoomOutPageTransformer2;
import cn.demomaster.huan.quickdeveloplibrary.view.banner.qdlayout.AjsLayoutInflater;
import cn.demomaster.huan.quickdeveloplibrary.view.banner.qdlayout.Element;
import cn.demomaster.qdrouter_library.base.fragment.QuickFragment;

import static cn.demomaster.huan.quickdeveloplibrary.view.banner.qdlayout.AjsLayoutInflater.generateLayout;

/**
 * 广告banner
 */
@ActivityPager(name = "Banner", preViewClass = TextView.class, resType = ResType.Resource,iconRes = R.drawable.ic_banner)
public class BannerFragment extends QuickFragment {

    @BindView(R.id.binner)
    Banner binner;
    @BindView(R.id.binner2)
    Banner binner2;

    @BindView(R.id.radio_group)
    RadioGroup radio_group;
    @BindView(R.id.frameLayout_01)
    FrameLayout frameLayout_01;

    public static Element getElement(NodeElement nodeElement) {
        Element myNodeElement = new Element();
        myNodeElement.setTag(nodeElement.getNodeName());
        List<NodeElement.NodeProperty> atts = nodeElement.getAttributes();
        if (atts != null) {
            for (int i = 0; i < atts.size(); i++) {
                String attsQName = atts.get(i).getName();
                String value = atts.get(i).getValue();
                //QDLogger.i("元素: attsQName=" + attsQName + ",value=" + value);
                myNodeElement.addProperty(attsQName, value);
            }
        }
        for (NodeElement element1 : nodeElement.getChildNodes()) {
            myNodeElement.addNode(getElement(element1));
        }
        return myNodeElement;
    }

    @Override
    public String getTitle() {
        return "Banner Sample";
    }

    @Override
    public View onGenerateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.activity_ads, null);
        return mView;
    }

    @Override
    public void initView(View rootView) {
        ButterKnife.bind(this,mContext);

        NodeElement nodeElement = AjsLayoutInflater.parseXmlAssets(mContext, "config/layout_test.xml",null);
        Element myElement = getElement(nodeElement);
        frameLayout_01.addView(generateLayout(mContext,null, myElement));
        binner.setRadius(20);
        radio_group.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId){
                case R.id.radio_button_01:
                    binner.setPageTransformer(true, new NormalPageTransformer());
                    binner.setGalleryPadding(0,0,0,0);
                    binner.setOffscreenPageLimit(1); //缓存页面数
                    break;
                case R.id.radio_button_02:
                    binner.setPageTransformer(true, new ZoomOutPageTransformer());
                    binner.setGalleryPadding(100,0,100,0);
                    binner.setOffscreenPageLimit(3); //缓存页面数
                    break;
            }
        });
        binner2.setOrientation(ViewPager2.ORIENTATION_VERTICAL);
        binner2.setPageTransformer(true,new ZoomOutPageTransformer2());
    }
}