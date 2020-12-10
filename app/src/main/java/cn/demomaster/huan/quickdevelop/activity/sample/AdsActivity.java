package cn.demomaster.huan.quickdevelop.activity.sample;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.demomaster.huan.quickdevelop.activity.BaseActivity;
import cn.demomaster.huan.quickdeveloplibrary.util.xml.DomSaxHandler;
import cn.demomaster.huan.quickdeveloplibrary.util.xml.NodeElement;
import cn.demomaster.huan.quickdeveloplibrary.view.banner.qdlayout.AjsLayoutInflater;
import cn.demomaster.huan.quickdeveloplibrary.view.banner.qdlayout.Element;
import cn.demomaster.huan.quickdeveloplibrary.view.banner.Banner;
import cn.demomaster.huan.quickdeveloplibrary.view.banner.NormalPageTransformer;
import cn.demomaster.huan.quickdeveloplibrary.view.banner.ZoomOutPageTransformer;
import cn.demomaster.huan.quickdeveloplibrary.view.banner.ZoomOutPageTransformer2;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ActivityPager;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ResType;
import cn.demomaster.huan.quickdeveloplibrary.base.activity.QDActivity;

import androidx.viewpager2.widget.ViewPager2;

import java.lang.annotation.Annotation;
import java.util.List;

import cn.demomaster.huan.quickdevelop.R;

import static cn.demomaster.huan.quickdeveloplibrary.view.banner.qdlayout.AjsLayoutInflater.generateLayout;

/**
 * 广告banner
 */
@ActivityPager(name = "广告控件", preViewClass = TextView.class, resType = ResType.Custome)
public class AdsActivity extends BaseActivity {

    @BindView(R.id.binner)
    Banner binner;
    @BindView(R.id.binner2)
    Banner binner2;

    @BindView(R.id.radio_group)
    RadioGroup radio_group;
    @BindView(R.id.frameLayout_01)
    FrameLayout frameLayout_01;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ads);
        ButterKnife.bind(this);

        NodeElement nodeElement = AjsLayoutInflater.parseXmlAssets(this, "config/layout_test.xml",null);
        Element myElement = getElement(nodeElement);
        frameLayout_01.addView(generateLayout(this,null, myElement));
        binner.setRadius(20);
        radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
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
            }
        });
        binner2.setOrientation(ViewPager2.ORIENTATION_VERTICAL);
        binner2.setPageTransformer(true,new ZoomOutPageTransformer2());
    }

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

}