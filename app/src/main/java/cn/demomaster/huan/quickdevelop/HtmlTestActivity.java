package cn.demomaster.huan.quickdevelop;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import cn.demomaster.huan.quickdeveloplibrary.base.activity.QDActivity;
import cn.demomaster.quickpermission_library.PermissionHelper;
import cn.demomaster.quicksticker_annotations.BindView;
import cn.demomaster.quicksticker_annotations.QuickStickerBinder;

public class HtmlTestActivity extends QDActivity {

    @BindView(R.id.btn_go)
    Button btn_go;
    @BindView(R.id.btn_01)
    Button btn_01;
    @BindView(R.id.btn_02)
    Button btn_02;
    @BindView(R.id.btn_03)
    Button btn_03;
    @BindView(R.id.btn_04)
    Button btn_04;
    @BindView(R.id.et_input)
    EditText et_input;

    @Override
    public boolean isUseActionBarLayout() {
        return false;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_html_test);
        QuickStickerBinder.getInstance().bind(this);
        et_input.setText("http://ucipchatlib.astro.nxengine.com/webchat/chat.html?c=31&jId=79&from=active&hashid=24242123454224&initFlag=debug&devOneId=ttcf542c5112a03557ce961b51560130");

        btn_01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_input.setText("http://ucipchatlib.astro.nxengine.com/webchat/chat.html?c=31&jId=79&from=active&hashid=24242123454224&initFlag=debug&devOneId=ttcf542c5112a03557ce961b51560130");
            }
        });
        btn_02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_input.setText("http://ucipchatlib.astro.nxengine.com/webchat/chat.html?c=31&jId=85&from=active&hashid=24242123454224&initFlag=debug&devOneId=ttcf542c5112a03557ce961b51560130");
            }
        });
        btn_03.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_input.setText("https://cipchat.sa.mysvw.com/webchat/chat.html?c=31&jId=79&from=active");
            }
        });
        btn_04.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_input.setText("https://cipchat.sa.mysvw.com/webchat/chat.html?c=31&jId=85&from=active");
            }
        });
        btn_go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url= et_input.getText().toString();
                Bundle bundle = new Bundle();
                bundle.putString("url",url);
                startActivity(QDMainFragmentActivity2.class,bundle);
            }
        });

        PermissionHelper.requestPermission(mContext, new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE},null);
    }
}