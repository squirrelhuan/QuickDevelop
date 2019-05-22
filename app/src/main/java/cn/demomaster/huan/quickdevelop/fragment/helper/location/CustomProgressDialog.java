package cn.demomaster.huan.quickdevelop.fragment.helper.location;

import android.content.Context;
import android.widget.TextView;

import cn.demomaster.huan.quickdevelop.R;

public class CustomProgressDialog extends LBaseDialog {

	private TextView tvMsg;

	public CustomProgressDialog(Context context) {
		super(context);
	}

	@Override
	protected int getContentViewId() {
		return R.layout.dialog_customprogress;
	}

	@Override
	protected void InitView() {
		tvMsg = (TextView) mView.findViewById(R.id.id_tv_loadingmsg);
	}

	@Override
	protected void IniListener() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void InitData() {

	}

	public void setMessage(String strMessage) {
		if (tvMsg != null) {
			tvMsg.setText(strMessage);
		}
	}
}
