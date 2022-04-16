package cn.demomaster.huan.quickdevelop.ui.fragment.component;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ActivityPager;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ResType;
import cn.demomaster.huan.quickdeveloplibrary.event.listener.OnSingleClickListener;
import cn.demomaster.huan.quickdeveloplibrary.widget.button.QDButton;
import cn.demomaster.qdrouter_library.base.fragment.QuickFragment;


/**
 * Squirrel桓
 * 2018/8/25
 */
@ActivityPager(name = "按钮", preViewClass = QDButton.class, resType = ResType.Custome)
public class QdButtonFragment extends QuickFragment {

    @Override
    public View onGenerateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_layout_button, null);
        return mView;
    }

    @Override
    public void initView(View rootView) {
        Button button = rootView.findViewById(R.id.btn_play1);
        button.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onClickEvent(View v) {
               /* Intent intent = new Intent(Intent.ACTION_PICK);
                intent.addFlags(Intents.FLAG_NEW_DOC);
                intent.setClassName(mContext, CaptureActivity.class.getName());
                startActivityForResult(intent, 123);
*/
            }
        });
        Button button2 = rootView.findViewById(R.id.btn_play2);
    }

}