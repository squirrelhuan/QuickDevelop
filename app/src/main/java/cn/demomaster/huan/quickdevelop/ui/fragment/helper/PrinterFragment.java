package cn.demomaster.huan.quickdevelop.ui.fragment.helper;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdevelop.util.NetPrinter;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ActivityPager;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ResType;
import cn.demomaster.huan.quickdeveloplibrary.helper.toast.QdToast;
import cn.demomaster.huan.quickdeveloplibrary.widget.button.QDButton;
import cn.demomaster.qdlogger_library.QDLogger;
import cn.demomaster.qdrouter_library.base.fragment.QuickFragment;

/**
 * Squirrel桓
 * 2021/6/21
 */

@ActivityPager(name = "打印机", preViewClass = TextView.class, resType = ResType.Custome)
public class PrinterFragment extends QuickFragment {

    //Components
    @BindView(R.id.btn_print)
    QDButton btn_print;

    @NonNull
    @Override
    public View onGenerateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_layout_printer, null);
        return mView;
    }

    private NetPrinter printer;
    public void initView(View rootView) {
        ButterKnife.bind(this, rootView);
        //getActionBarTool().setHeaderBackgroundColor(Color.RED);
        btn_print.setOnClickListener(v -> {
            QdToast.show("printer");

            Thread thread =  new Thread(new Runnable() {
                @Override
                public void run() {
                    printer = new NetPrinter();
                    printer.Open("172.16.11.183", 9100);
                    QDLogger.e("printer.IFOpen="+printer.IFOpen);
                    if(printer.IFOpen){

                    }
                }
            });
            thread.start();
        });
    }

}